package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.PopeFavourCard;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.exceptions.DuplicatedIdException;
import it.polimi.ingsw.exceptions.GameSizeExceeded;
import it.polimi.ingsw.exceptions.ParsingException;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Game implements ControllerEventListener,Runnable {
    private List<PersonalBoard> personalBoards;
    private int currentPlayerOrdinal;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;
    private PopeFavourCard[] popeFavourCards;
    private List<LeaderCard> leaderCards;
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
     * Start the game
     */
    public void run(){
        /*while(!canProceed){
            try{
                this.wait();
            }catch(Exception e){}
        }
        chooseInitialResource();
        int i=0;
        do{
            personalBoards.get(i).playTurn();
            i=(i+1)%personalBoards.size();
        }while(!gameDone);
        //Play the rest of the turns
        for(;i<personalBoards.size();i++){
            personalBoards.get(i).playTurn();
        }

        //Pick a winner
        int winner=0;
        int maxPoints=0;
        for(int j=0;j<personalBoards.size();j++){
            int points=personalBoards.get(j).getVictoryPoints();
            if(points>maxPoints){
                winner=j;
                maxPoints=points;
            }
        }

        for(GameEventListener g:eventListeners){
            g.announceWinner(personalBoards.get(winner).getPlayerName());
        }*/
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
        leaderCards=leaderCards.subList(playerNumber*4, (playerNumber+1)*4);
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

    public void discardResources(int numberOfResources,String playerName){

    }

    /**
     * add the initial resource selected
     */
    public synchronized void addInitialResource(String playerName,ResType resource) {
        for(PersonalBoard p:personalBoards){
            if(p.getPlayerName().equals(playerName)){
                try {
                    p.addResourcesToDepot(new ResType[]{resource});
                }catch(Exception e){}
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