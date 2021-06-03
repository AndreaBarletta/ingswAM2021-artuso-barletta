package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.PopeFavourCard;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game implements ControllerEventListener {
    private List<PersonalBoard> personalBoards;
    private int currentPlayerOrdinal;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;
    private PopeFavourCard[] popeFavourCards;
    private List<LeaderCard> leaderCardsDeck;
    private List<DevelopmentCard> developmentCardsDeck;
    private List<GameEventListener> eventListeners;
    private int maximumPlayers;
    private boolean hasStarted;

    public Game(int maximumPlayers){
        personalBoards=new ArrayList<>();
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
        eventListeners=new ArrayList<>();
        leaderCardsDeck = new ArrayList<>();
        this.maximumPlayers=maximumPlayers;
        currentPlayerOrdinal=0;
        hasStarted=false;
    }

    /**
     * Adds a new game event listener to the listener list
     * @param newEventListener new game event listener to be added to the listeners list
     */
    public void addEventListener(GameEventListener newEventListener){
        eventListeners.add(newEventListener);
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
     * Loads developments cards from a json file and creates the development card grid,
     * putting the cards in the right cells based on the level and card type
     * @param path Path of the json file containing the  list of development cards
     * @return Whether or not the development cards were loaded successfully and the card grid was created
     */
    public boolean loadDevelopmentCardGridFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading development cards i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            DevelopmentCard[] developmentCards=gson.fromJson(content, DevelopmentCard[].class);
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
     * Loads the pope favour cards from a json file, which are then assigned to the players at the beginning
     * of the game
     * @param path Path of the json file containing the list of pope favour cards
     * @return Whether or not the pope favour cards were loaded successfully
     */
    public boolean loadPopeFavourCardsFromFile(String path) {
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading pope favour cards i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            popeFavourCards=gson.fromJson(content, PopeFavourCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for pope favour cards");
            return false;
        }

        return true;
    }

    /**
     * Loads the leader cards from a json file
     * @param path Path of the json file containing the list of leader cards
     * @return Whether or not the leader cards were loaded successfully
     */
    public boolean loadLeaderCardsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading leader cards i.e. wrong path");
            return false;
        }

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            LeaderCard[] leaderCardsArray=gson.fromJson(content, LeaderCard[].class);
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
    public boolean addPlayer(String playerName) throws GameSizeExceeded, ParsingException,DuplicatedIdException {
        if(personalBoards.size()<maximumPlayers){
            for (PersonalBoard personalBoard : personalBoards) {
                if (personalBoard.getPlayerName().equals(playerName)) {
                    throw new DuplicatedIdException();
                }
            }
            PersonalBoard newPersonalBoard=new PersonalBoard(playerName,developmentCardGrid,market);
            personalBoards.add(newPersonalBoard);
            if(!newPersonalBoard.loadFaithTrackFromFile(getClass().getClassLoader().getResource("faithTrack.json").getPath())){
                throw new ParsingException();
            }
        }else{
            throw new GameSizeExceeded();
        }
        return personalBoards.size()==maximumPlayers;
    }

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
        List<LeaderCard> leaderCardsToShow = new ArrayList<>();
        leaderCardsToShow= leaderCardsDeck.subList(playerNumber*4, (playerNumber+1)*4);
        personalBoards.get(playerNumber).setInitialLeaderCards(leaderCardsToShow);
        List<String> ids=new ArrayList<>();

        for(LeaderCard l:leaderCardsToShow){
            ids.add(Integer.toString(l.getId()));
        }

        return ids.toArray(String[]::new);
    }

    public String[] getLeaderCards(String playerName) {
        PersonalBoard player=getPersonalBoard(playerName);
        if(player!=null) {
            List<String> ids=new ArrayList<>();
            for (LeaderCard l : player.getLeaderCards()) {
                ids.add(Integer.toString(l.getId()));
            }
            return ids.toArray(String[]::new);
        }
        return null;
    }

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

        return personalBoards.get(playerNumber).setLeaderCards(leaderCardsToAdd);
    }

    /**
     * add the initial resource selected
     */
    public synchronized void addInitialResource(String playerName,ResType resource) {
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playerName)){
                try {
                    resource.effectOnAcquire(p);
                }catch(Exception e){}
            }
        }
    }

    public void activateLeaderCards(String playername, int leaderCardId) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException,AlreadyActiveException {
        PersonalBoard player=getPersonalBoard(playername);
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

    public void discardLeaderCards(String playername, int leaderCardId) throws CardNotFoundException {
        PersonalBoard player=getPersonalBoard(playername);

        if(player!=null) {
            player.discardLeaderCard(leaderCardId);
        }
    }

    public void activateProductions(String playerName, Production[] productions) throws ResourcesException {
        PersonalBoard player=getPersonalBoard(playerName);

        if(player!=null) {
            player.activateProductions(productions);
        }
    }

    public String getAllResource(String playerName) {
        PersonalBoard player=getPersonalBoard(playerName);

        if(player!=null) {
            return player.getAllResources().toString();
        } else {
            return null;
        }
    }

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

    public String[] removeDevCardFromMarket(String devCardId){
        DevelopmentCard devCardToRemove= developmentCardsDeck.get(Integer.parseInt(devCardId));
        int level=devCardToRemove.getLevel();
        CardType cardType=devCardToRemove.getCardType();

        developmentCardGrid.removeCard(level,cardType);

        return new String[]{
                String.valueOf(level),
                String.valueOf(cardType.name()),
                String.valueOf(developmentCardGrid.getTopCard(level,cardType).getId())};
    }

    public void buyDevCard(String playername, int devCardId, int slot) throws LevelException{
        PersonalBoard p=getPersonalBoard(playername);
        if(p!=null){
            p.addDevCardToSlot(developmentCardsDeck.get(devCardId),slot);
            //If addition was successfull (didn't throw), pay the cost
            Map<ResType,Integer> cost=developmentCardsDeck.get(devCardId).getCost();
            for(Map.Entry<ResType,Integer> entry:cost.entrySet())
                p.payResource(entry.getKey(),entry.getValue());
        }
    }

    /**
     * Acquires resources from the market
     * @param playername Name of the player that acquires the resources
     * @param row True is a row was selected, false if a column was selected
     * @param index Row / column index
     */
    public int acquireFromMarket(String playername, boolean row, int index){
        PersonalBoard p=getPersonalBoard(playername);
        int numberOfWhiteMarblesToChoose=0;
        if(p!=null){
            ResType[] acquiredResources;
            if(row){
                acquiredResources=market.acquireRow(index);
            }else{
                acquiredResources=market.acquireColumn(index);
            }
            for(ResType r:acquiredResources){
                try{
                    r.effectOnAcquire(p);
                }catch(DepotSpaceException e){}
            }
        }
        return numberOfWhiteMarblesToChoose;
    }

    public int getMaximumPlayers(){
        return maximumPlayers;
    }

    public String[] getPlayerOrder(){
        List<String> playerOrder=new ArrayList<>();
        for(PersonalBoard p:personalBoards){
            playerOrder.add(p.getPlayerName());
        }
        return playerOrder.toArray(String[]::new);
    }

    public int getPlayerOrdinal(String playerName){
        int i=0;
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playerName)) break;
            else i++;
        }
        return i;
    }

    public PersonalBoard getPersonalBoard(String playername){
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playername)) return p;
        }
        return null;
    }

    public String getCurrentPlayer(){
        return personalBoards.get(currentPlayerOrdinal).getPlayerName();
    }

    public void nextPlayer(){
        currentPlayerOrdinal=(currentPlayerOrdinal+1)%maximumPlayers;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

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
}