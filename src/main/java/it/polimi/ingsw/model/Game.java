package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.Lorenzo.Lorenzo;
import it.polimi.ingsw.model.Lorenzo.LorenzoEventListener;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    private final List<PersonalBoard> personalBoards;
    private int currentPlayerOrdinal;
    private final Market market;
    private final DevelopmentCardGrid developmentCardGrid;
    private List<LeaderCard> leaderCardsDeck;
    private List<DevelopmentCard> developmentCardsDeck;
    private final int numOfPlayers;
    private boolean hasStarted;
    private boolean isLastTurn;
    private boolean hasEnded;
    private Production[] tempProduction;
    private Lorenzo lorenzo;

    /**
     * Create a game
     * @param numOfPlayers Number of players in the game
     */
    public Game(int numOfPlayers){
        personalBoards=new ArrayList<>();
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
        leaderCardsDeck = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
        currentPlayerOrdinal=0;
        hasStarted=false;
        isLastTurn=false;
        hasEnded=false;
    }

    /**
     * Adds a new personal board event listener to all the listener lists of the personal boards
     * @param newEventListener New event listener
     */
    public void addPersonalBoardsEventListener(PersonalBoardEventListener newEventListener){
        for(PersonalBoard p:personalBoards){
            p.addEventListener(newEventListener);
        }
    }

    /**
     * Adds a new lorezno event listener to the listener's list
     * @param newEventListener New listener to be added
     */
    public void addLorenzoEventListener(LorenzoEventListener newEventListener){
        lorenzo.addEventListener(newEventListener);
    }

    /**
     * Loads developments cards from a json file and creates the development card grid,
     * putting the cards in the right cells based on the level and card type
     * @param fileString Path of the json file containing the  list of development cards
     * @return Whether or not the development cards were loaded successfully and the card grid was created
     */
    public boolean loadDevelopmentCardGridFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading development cards i.e. wrong path");
            return false;
        }
        Reader reader=new InputStreamReader(inputStream);

        Gson gson=new Gson();
        try{
            DevelopmentCard[] developmentCards=gson.fromJson(reader, DevelopmentCard[].class);
            this.developmentCardsDeck =Arrays.asList(developmentCards);
            for(DevelopmentCard d:developmentCards){
                developmentCardGrid.addCard(d);
            }
            developmentCardGrid.shuffle();
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for development cards");
            return false;
        }
        return true;
    }

    /**
     * Loads the leader cards from a json file
     * @param fileString Path of the json file containing the list of leader cards
     * @return Whether or not the leader cards were loaded successfully
     */
    public boolean loadLeaderCardsFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading leader cards i.e. wrong path");
            return false;
        }
        Reader reader=new InputStreamReader(inputStream);

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            LeaderCard[] leaderCardsArray=gson.fromJson(reader, LeaderCard[].class);
            leaderCardsDeck =Arrays.asList(leaderCardsArray);
            //Shuffle the cards
            Collections.shuffle(leaderCardsDeck);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            return false;
        }
        return true;
    }

    /**
     * Adds a new player to the game and create their personal board
     * @param playerName Name of the new player
     * @return Whether or not the maximum number of players has been reached
     */
    /**
     * Adds a new player to the game and create their personal board
     * @param playerName Name of the new player
     * @return Whether or not the game can start
     * @throws GameSizeExceeded Game has already reached the maximum amount of players
     * @throws ParsingException Error parsing the jsons
     * @throws DuplicatedIdException Another player with the same name already exists
     */
    public boolean addPlayer(String playerName) throws GameSizeExceeded, ParsingException,DuplicatedIdException {
        if(personalBoards.size()< numOfPlayers){
            for (PersonalBoard personalBoard : personalBoards) {
                if (personalBoard.getPlayerName().equals(playerName)) {
                    throw new DuplicatedIdException();
                }
            }
            PersonalBoard newPersonalBoard=new PersonalBoard(playerName);
            personalBoards.add(newPersonalBoard);
            if(!newPersonalBoard.loadFaithTrackFromFile("/faithTrack.json")){
                throw new ParsingException();
            }
            newPersonalBoard.getFaithTrack().setVaticanReports();
        }else{
            throw new GameSizeExceeded();
        }
        return personalBoards.size()== numOfPlayers;
    }

    /**
     * Add lorenzo to the game
     * @throws ParsingException Error while parsing the jsons
     */
    public void addLorenzo() throws ParsingException {
        lorenzo=new Lorenzo(developmentCardGrid);
        if(!lorenzo.loadFaithTrackFromFile("/faithTrack.json")){
            throw new ParsingException();
        }
        lorenzo.setVaticanReports();
    }

    /**
     * Remove a player from the game
     * @param playerName Name of the player to be removed
     */
    public void removePlayer(String playerName){
        for(Iterator<PersonalBoard> pbIterator = personalBoards.iterator(); pbIterator.hasNext();){
            if(pbIterator.next().getPlayerName().equals(playerName)){
                pbIterator.remove();
                break;
            }
        }
    }

    public int getNumberOfPlayer(){
        return personalBoards.size();
    }

    public String[][] getDevelopmentCardsGridIds(){
        return developmentCardGrid.getTopCardsIds();
    }

    public ResType[][] getMarketTray(){
        return market.getMarketTray();
    }

    public ResType getLeftoverMarble(){
        return market.getLeftoverMarble();
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }

    /**
     * Shuffles the player order and gives the inkwell to the first player
     */
    public void giveInkwell(){
        Collections.shuffle(personalBoards);
        personalBoards.get(0).receiveInkwell();
    }

    /**
     * Gets the initial leader cards from which the player has to choose
     * @param playerNumber Progressive number of the player (0 to 3)
     * @return Array containing the 4 leaders cards
     */
    public String[] getInitialLeaderCards(int playerNumber){
        List<LeaderCard> leaderCardsToShow;
        leaderCardsToShow= leaderCardsDeck.subList(playerNumber*4, (playerNumber+1)*4);
        personalBoards.get(playerNumber).setInitialLeaderCards(leaderCardsToShow);
        List<String> ids=new ArrayList<>();

        for(LeaderCard l:leaderCardsToShow){
            ids.add(Integer.toString(l.getId()));
        }

        return ids.toArray(String[]::new);
    }

    /**
     * Add leader cards to a player given it's ordinal
     * @param playerNumber Ordinal of the player
     * @param leaderCardsId Ids of the leader cards
     * @return whether or not the cards were among the 4 choosen initially
     */
    public boolean addLeaderCards(int playerNumber,int[] leaderCardsId){
        List<LeaderCard> leaderCardsToAdd=new ArrayList<>();
        for(int i:leaderCardsId){
            for(LeaderCard l: leaderCardsDeck){
                if(l.getId()==i){
                    leaderCardsToAdd.add(l);
                    break;
                }
            }
        }
        if(leaderCardsToAdd.size()!=2)
            return false;
        return personalBoards.get(playerNumber).setLeaderCards(leaderCardsToAdd);
    }

    /**
     * Activate a player's leader card
     * @param playerName Name of the player
     * @param leaderCardId leader card to be activated
     * @throws CardNotFoundException The player doesn't have the selected leader card
     * @throws CardTypeException The player doesn't have enough development cards of a type required by the leader card
     * @throws ResourcesException The player doesn't have enough resources of a type required by the leader card
     * @throws LevelException The player doesn't have enough development cards of a level required by the leader card
     * @throws AlreadyActiveException The leader card has already been activated
     */
    public void activateLeaderCards(String playerName, int leaderCardId) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException,AlreadyActiveException {
        PersonalBoard player=getPersonalBoard(playerName);
        if(player!=null){
            if(leaderCardId<16&&leaderCardId>=0){
                player.activateLeaderCard(leaderCardsDeck
                        .stream()
                        .filter(
                                card->card.getId()==leaderCardId)
                        .collect(Collectors.toList())
                        .get(0)
                );
            }else {
                throw new CardNotFoundException();
            }
        }

    }

    /**
     * Discard a player's leader card
     * @param playerName Name of the player
     * @param leaderCardId leader card to be discarded
     * @throws CardNotFoundException The player doesn't have the selected leader card
     */
    public void discardLeaderCards(String playerName, int leaderCardId) throws CardNotFoundException {
        PersonalBoard player=getPersonalBoard(playerName);

        if(player!=null) {
            player.discardLeaderCard(leaderCardId);
        }
    }

    /**
     * Activate a player's productions
     * @param playerName Name of the player
     * @param productions Productions to activate
     * @throws ResourcesException The player doesn't have enough ingredients to activate all the productions
     * @throws AnyResourceException A production has a chooseable resource as ingredient or product
     */
    public void activateProductions(String playerName, Production[] productions) throws ResourcesException,AnyResourceException {
        PersonalBoard player=getPersonalBoard(playerName);

        if(player!=null) {
            try {
                player.activateProductions(tempProduction != null ? tempProduction : productions);
                tempProduction=null;
            }catch(AnyResourceException e){
                if(tempProduction==null)
                    tempProduction=productions;

                throw new AnyResourceException();
            }
        }
    }

    /**
     * Choose a resource to replace the chooseable resource in the productions, the productions are saved only within a player's turn
     * @param anyResource Resource to replace
     */
    public void chooseAnyResource(ResType anyResource){
        for(Production p:tempProduction){
            if(p.getIngredients().containsKey(ResType.ANY)){
                p.getIngredients().put(ResType.ANY,p.getIngredients().get(ResType.ANY)-1);
                p.getIngredients().compute(anyResource,(k,v)->v==null?1:v+1);
                if(p.getIngredients().get(ResType.ANY)==0){
                    p.getIngredients().remove(ResType.ANY);
                }
                break;
            }
            if(p.getProducts().containsKey(ResType.ANY)){
                p.getProducts().put(ResType.ANY,p.getProducts().get(ResType.ANY)-1);
                p.getProducts().compute(anyResource,(k,v)->v==null?1:v+1);
                if(p.getProducts().get(ResType.ANY)==0){
                    p.getProducts().remove(ResType.ANY);
                }
                break;
            }
        }
    }

    public Depot[] getDepots(String playerName) {
        PersonalBoard player=getPersonalBoard(playerName);
        assert player != null;
        return player.getDepots();
    }

    public List<Depot> getLeaderDepots(String playerName) {
        PersonalBoard player=getPersonalBoard(playerName);
        assert player != null;
        return player.getLeaderDepots();
    }

    public Map<ResType, Integer> getStrongboxContent(String playerName) {
        PersonalBoard player=getPersonalBoard(playerName);
        assert player != null;
        return player.getStrongboxContent();
    }

    /**
     * Checks if a card can be bought and placed in the player board
     * @param playername Name of the player
     * @param devCardId Id of the card to be bought
     * @param discountIds Optional discounts
     * @throws ResourcesException The player doesn't have enough resources to buy the development card selected
     * @throws LevelException The player doesn't have an high enough card in the selected slot to buy the development card selected
     */
    public void canBuyDevCard(String playername,int devCardId,int[] discountIds) throws ResourcesException, LevelException,CardNotFoundException{
        PersonalBoard player=getPersonalBoard(playername);

        if(player!=null){
            DevelopmentCard chosenCard=developmentCardsDeck.get(devCardId);
            if(chosenCard==developmentCardGrid.getTopCard(chosenCard.getLevel(),chosenCard.getCardType()))
                player.canBuyDevCard(chosenCard,discountIds);
            else
                throw new CardNotFoundException();
        }
    }

    /**
     * Remove a dev card from the grid
     * @param devCardId Id of the card
     * @return Level, card type and id of the new card on top of the stack, -1 if the stack is empty
     */
    public String[] removeDevCardFromGrid(String devCardId){
        DevelopmentCard devCardToRemove= developmentCardsDeck.get(Integer.parseInt(devCardId));
        int level=devCardToRemove.getLevel();
        CardType cardType=devCardToRemove.getCardType();

        developmentCardGrid.removeCard(level,cardType);

        DevelopmentCard devCard=developmentCardGrid.getTopCard(level,cardType);
        return new String[]{
                String.valueOf(level),
                cardType.name(),
                String.valueOf(devCard==null?-1:devCard.getId())};
    }

    /**
     * Buy a dev card
     * @param playername Name of the player that buys the dev card
     * @param devCardId Id of the card
     * @param slot Slot where to put the car
     * @param discountIds Optional discounts
     * @throws LevelException Cannot add the card to the selected slot
     */
    public void buyDevCard(String playername, int devCardId, int slot,int[] discountIds) throws LevelException{
        PersonalBoard p=getPersonalBoard(playername);
        if(p!=null){
            p.addDevCardToSlot(developmentCardsDeck.get(devCardId),slot);
            //If addition was successful (didn't throw), pay the cost
            Map<ResType,Integer> cost=developmentCardsDeck.get(devCardId).getCost();
            for(Map.Entry<ResType,Integer> entry:cost.entrySet())
                p.payResource(entry.getKey(),entry.getValue(),discountIds);
        }
    }

    /**
     * Acquires resources from the market
     * @param playerName Name of the player that acquires the resources
     * @param row True is a row was selected, false if a column was selected
     * @param index Row / column index
     */
    public ResType[] acquireFromMarket(String playerName, boolean row, int index){
        PersonalBoard p=getPersonalBoard(playerName);
        int numberOfWhiteMarblesToChoose=0;
        List<ResType> resources=new ArrayList<>();

        assert p!=null;
        ResType[] acquiredResources;
        if(row){
            acquiredResources=market.acquireRow(index);
        }else{
            acquiredResources=market.acquireColumn(index);
        }

        for(ResType r:acquiredResources){
            if(r.effectOnAcquire(p)!=null)
                resources.add(r.effectOnAcquire(p));
        }

        return resources.toArray(ResType[]::new);
    }

    /**
     * Check if a player can add resources to their depots
     * @param playerName Name of the player
     * @param resources Resources to be added
     * @return Whether or not the resources can be added
     */
    public boolean canAddToDepot(String playerName, ResType[] resources){
        PersonalBoard p=getPersonalBoard(playerName);
        assert p!=null;
        return p.canAddToDepot(resources);
    }

    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    /**
     * Get the order of the players
     * @return The name of the players, ordered from the first (the one with the inkwell) to the last
     */
    public String[] getPlayerOrder(){
        List<String> playerOrder=new ArrayList<>();
        for(PersonalBoard p:personalBoards){
            playerOrder.add(p.getPlayerName());
        }
        return playerOrder.toArray(String[]::new);
    }

    /**
     * Get the amount of turns the player has to wait after the player with the inkwell plays their turn
     * @param playerName Name of the player
     * @return The amount of turns the player has to wait after the player with the inkwell plays their turn
     */
    public int getPlayerOrdinal(String playerName){
        int i=0;
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playerName)) break;
            else i++;
        }
        return i;
    }

    /**
     * Get a personal board
     * @param playername Name of the player
     * @return Personal board of the player
     */
    public PersonalBoard getPersonalBoard(String playername){
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playername)) return p;
        }
        return null;
    }

    public String getCurrentPlayer(){
        return personalBoards.get(currentPlayerOrdinal).getPlayerName();
    }

    /**
     * Changes the current player to the next player. If it's a singleplayer game, lorenzo does his action, and the current player stays the same
     */
    public void nextPlayer(){
        if(numOfPlayers ==1) {
            lorenzo.lorenzoAction();
        } else {
            currentPlayerOrdinal = (currentPlayerOrdinal + 1) % numOfPlayers;
            if (isLastTurn && currentPlayerOrdinal == 0) {
                hasEnded = true;
            }
        }
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    /**
     * Chec if the player can activate the discounts
     * @param playerName Name of the player
     * @param ids id of the discounts
     * @return Whether or not the player can activate the discounts (they are avaiable and different)
     */
    public boolean canDiscount(String playerName,int[] ids){
        //Check if discount ids are different
        if(ids.length != IntStream.of(ids).boxed().collect(Collectors.toSet()).size())
            return false;

        PersonalBoard player=getPersonalBoard(playerName);
        if(player!=null){
            return player.canDiscount(ids);
        }
        return false;
    }

    /**
     * Get the number of development cards a player has
     * @param playerName Name of the player
     * @return Amount of development cards
     */
    public int getNumberOfDevCards(String playerName){
        DevelopmentCardSlot[] devCardSlots=getPersonalBoard(playerName).getDevelopmentCardSlots();
        int count=0;
        for(DevelopmentCardSlot dcs:devCardSlots){
            count+=dcs.getCardsInSlot();
        }
        return count;
    }

    public void lastTurn(){
        isLastTurn=true;
    }

    public boolean hasEnded(){
        return hasEnded;
    }

    /**
     * Calculate the victory points of each player and get the winner
     * @return The name of the winner
     */
    public String getWinner() {
        String winner="";
        int victoryPointsMax=0;
        for(PersonalBoard pb:personalBoards){
            if(pb.getVictoryPoints()>victoryPointsMax){
                victoryPointsMax=pb.getVictoryPoints();
                winner=pb.getPlayerName();
            }
        }
        return winner;
    }

    /**
     * Check if a player or lorenzo can activate a vatican report
     * @return Index of the vatican report that can be activated, or -1 if none can be activated
     */
    public int canSendVaticanReport(){
        for(PersonalBoard pb:personalBoards){
            int vaticanReport=pb.getFaithTrack().canSendVaticanReport();
            if(vaticanReport!=-1) return vaticanReport;
        }
        if(lorenzo!=null){
            int lvr=lorenzo.canSendVaticanReport();
            if(lvr!=-1) return lvr;
        }
        return -1;
    }

    /**
     * Send a vatican report, activating or discarding the pope cards
     * @param k index of the vatican report to send
     * @return Map containing the name of the players and whether or not they activated the pope favour card (lorenzo
     */
    public Map<String,Boolean> sendVaticanReport(int k){
        Map<String,Boolean> vaticanReportResults=new HashMap<>();
        for(PersonalBoard pb:personalBoards){
            vaticanReportResults.put(pb.getPlayerName(),pb.getFaithTrack().sendVaticanReport(k));
        }
        if(lorenzo!=null){
            lorenzo.sendVaticanReport(k);
        }
        return vaticanReportResults;
    }

    /**
     * Check if any player has reached the end of their faith track
     * @return Whether or not a player has reached the end of their faith track
     */
    public boolean isFaithTrackEnd(){
        for(PersonalBoard pb:personalBoards)
            if(pb.getFaithTrack().isAtEnd()) return true;

        if(lorenzo!=null){
            return lorenzo.isAtEnd();
        }
        return false;
    }
}