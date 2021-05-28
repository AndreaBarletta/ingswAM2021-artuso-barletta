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

public class Game implements ControllerEventListener {
    private List<PersonalBoard> personalBoards;
    private int currentPlayerOrdinal;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;
    private PopeFavourCard[] popeFavourCards;
    private List<LeaderCard> leaderCards;
    private List<DevelopmentCard> developmentCards;
    private List<GameEventListener> eventListeners;
    private int maximumPlayers;

    public Game(int maximumPlayers){
        personalBoards=new ArrayList<>();
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
        eventListeners=new ArrayList<>();
        leaderCards= new ArrayList<>();
        this.maximumPlayers=maximumPlayers;
        currentPlayerOrdinal=0;
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
            this.developmentCards=Arrays.asList(developmentCards);
            for(DevelopmentCard d:developmentCards){
                developmentCardGrid.addCard(d);
            }
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
            leaderCards=Arrays.asList(leaderCardsArray);
            //Shuffle the cards
            Collections.shuffle(leaderCards);
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
            for(Iterator<PersonalBoard> pbIterator=personalBoards.iterator();pbIterator.hasNext();){
                if(pbIterator.next().getPlayerName().equals(playerName)){
                    throw new DuplicatedIdException();
                }
            }
            PersonalBoard newPersonalBoard=new PersonalBoard(playerName,developmentCardGrid,market);
            personalBoards.add(newPersonalBoard);
            if(!newPersonalBoard.loadFaithTrackFromFile("src/main/resources/faithTrack.json")){
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
     * Assign the inkwell to a random player
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
        leaderCardsToShow=leaderCards.subList(playerNumber*4, (playerNumber+1)*4);
        personalBoards.get(playerNumber).setInitialLeaderCards(leaderCardsToShow);
        List<String> ids=new ArrayList<>();

        for(LeaderCard l:leaderCardsToShow){
            ids.add(Integer.toString(l.getId()));
        }

        return ids.toArray(String[]::new);
    }

    public String[] getLeaderCards(String playerName) {
        List<LeaderCard> leaderCardsToShow = null;
        for(PersonalBoard p: personalBoards) {
            if(p.getPlayerName().equals(playerName)) {
                 leaderCardsToShow = p.getLeaderCards();
                break;
            }
        }
        if(leaderCardsToShow!=null) {
            List<String> ids=new ArrayList<>();
            for (LeaderCard l : leaderCardsToShow) {
                ids.add(Integer.toString(l.getId()));
            }
            return ids.toArray(String[]::new);
        }
        return null;
    }

    public boolean addLeaderCards(int playerNumber,String[] leaderCardsId){
        List<LeaderCard> leaderCardsToAdd=new ArrayList<>();
        for(String s:leaderCardsId){
            for(LeaderCard l:leaderCards){
                if(l.getId()==Integer.parseInt(s)){
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

    public void activateLeaderCards(String playername, String leaderCardId) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException {
        PersonalBoard player=getPersonalBoard(playername);
        if(Integer.parseInt(leaderCardId)<16&&Integer.parseInt(leaderCardId)>=0){
            if(player!=null){
                player.activateLeaderCard(leaderCards
                        .stream()
                        .filter(
                                card->card.getId()==Integer.parseInt(leaderCardId))
                        .collect(Collectors.toList())
                        .get(0)
                );
            }
        }else
            throw new CardNotFoundException();
    }

    public void discardLeaderCards(String playername, String leaderCardId) throws CardNotFoundException {
        PersonalBoard player=getPersonalBoard(playername);

        if(player!=null) {
            player.discardLeaderCard(Integer.parseInt(leaderCardId));
        }
    }

    public void canBuyDevCard(String playername,String devCardId) throws ResourcesException, LevelException{
        PersonalBoard player=getPersonalBoard(playername);

        if(player!=null){
            player.canBuyDevCard(developmentCards.get(Integer.parseInt(devCardId)));
        }
    }

    public String[] removeDevCardFromMarket(String devCardId){
        DevelopmentCard devCardToRemove=developmentCards.get(Integer.parseInt(devCardId));
        int level=devCardToRemove.getLevel();
        CardType cardType=devCardToRemove.getCardType();

        developmentCardGrid.removeCard(level,cardType);

        return new String[]{
                String.valueOf(level),
                String.valueOf(cardType.ordinal()),
                String.valueOf(developmentCardGrid.getTopCard(level,cardType).getId())};
    }

    public void addDevCardToSlot(String playername, String devCardId, String slot) throws LevelException{
        PersonalBoard p=getPersonalBoard(playername);
        if(p!=null){
            p.addDevCardToSlot(developmentCards.get(Integer.parseInt(devCardId)),Integer.parseInt(slot));
        }
    }

    /**
     * Acquires resources from the market
     * @param playername Name of the player that acquires the resources
     * @param row True is a row was selected, false if a column was selected
     * @param index Row / column index
     */
    public void acquireFromMarket(String playername, boolean row, int index){
        PersonalBoard p=getPersonalBoard(playername);
        if(p!=null){
            ResType[] acquiredResources;
            if(row){
                acquiredResources=market.acquireRow(index);
            }else{
                acquiredResources=market.acquireColumn(index);
            }
            for(LeaderCard l:p.getLeaderCards()){
                l.effectOnMarketBuy(p,acquiredResources);
            }
            for(ResType r:acquiredResources){
                try{
                    r.effectOnAcquire(p);
                }catch(DepotSpaceException e){}
            }
        }
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
}