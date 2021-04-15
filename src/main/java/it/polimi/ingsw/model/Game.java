package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.PopeFavourCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Game implements GameEventListener {
    private PersonalBoard[] personalBoards;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;
    private PopeFavourCard[] popeFavourCards;
    private List<GameEventListener> eventListeners;

    public Game(){
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
        eventListeners=new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
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
            System.out.println("Error loading json file for development cards");
            return false;
        }

        return true;
    }

    /**
     * Loads the pope favour cards from a json file, which are then assigned to the players at the beginning
     * of the game
     * @param path Path of the json file containing the list of pope favour cards
     */
    public boolean loadPopeFavorCardsFromFile(String path) {
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading popefavour cards i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            DevelopmentCard[] developmentCards=gson.fromJson(content, DevelopmentCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for development cards");
            return false;
        }

        return true;
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }
}