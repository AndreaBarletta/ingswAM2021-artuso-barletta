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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Game implements ControllerEventListener {
    private String gameName;
    private List<PersonalBoard> personalBoards;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;
    private PopeFavourCard[] popeFavourCards;
    private LeaderCard[] leaderCards;
    private List<GameEventListener> eventListeners;
    private boolean gameDone;
    private int maximumPlayers;

    public Game(String gameName,int maximumPlayers){
        this.gameName=gameName;
        personalBoards=new ArrayList<>();
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
        eventListeners=new ArrayList<>();
        gameDone=false;
        this.maximumPlayers=maximumPlayers;
    }

    /**
     * Adds a new game event listener to the listener list
     * @param newEventListener new game event listener to be added to the listeners list
     */
    public void addEventListener(GameEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * Loads developments cards from a json file and creates the development card grid,
     * putting the cards in the right cells based on the level and card type
     * @param path Path of the json file containing the  list of development cards
     * @return Whether or not the development cards were loaded successfully and the card grid was created
     */
    public boolean loadDevelopmentCardsFromFile(String path){
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
            leaderCards=gson.fromJson(content, LeaderCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            return false;
        }

        return true;
    }

    /**
     * Start the game
     */
    public void start(){
        giveInkwell();
        showLeaderCard();
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
        }
    }

    /**
     * Adds a new player to the game and create their personal board
     * @param playerName Name of the new player
     * @return Whether or not the maximum number of players has been reached
     */
    public boolean addPlayer(String playerName) throws GameSizeExceeded, ParsingException, DuplicatedIdException {
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

        for(GameEventListener g:eventListeners){
            g.inkwellGiven(personalBoards.get(0).getPlayerName());
        }
    }

    /**
     * Shows the 4 leader cards to the players and ask them to choose 2
     */
    public void showLeaderCard(){
        List<LeaderCard> leaderCardList= Arrays.asList(leaderCards);
        Collections.shuffle(leaderCardList);
        for(int i=0;i<personalBoards.size();i++){
            LeaderCard[] leaderCardsToShow = new LeaderCard[4];
            leaderCardList.subList(i*4, (i+1)*4).toArray(leaderCardsToShow);
            personalBoards.get(i).chooseLeaderCards(leaderCardsToShow);
        }
    }

    public void discardResources(int numberOfResources,String playerName){

    }

    private void chooseInitialResource() {
        for(int playerNumber = 1; playerNumber<personalBoards.size(); playerNumber++) {
            if(playerNumber == 1 || playerNumber == 2) {
                for (GameEventListener g : eventListeners) {
                    g.chooseOneInitialResource(personalBoards.get(playerNumber).getPlayerName());
                }
            }
            if (playerNumber == 3) {
                for (GameEventListener g : eventListeners) {
                    g.chooseTwoInitialResource(personalBoards.get(playerNumber).getPlayerName());
                }
            }
        }
    }

    /**
     * add the initial resource selected
     */
    public void addInitialResource(ResType resource) {

    }
}