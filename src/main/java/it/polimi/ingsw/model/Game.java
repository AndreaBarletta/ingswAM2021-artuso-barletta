package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.ControllerEventListener;

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
     * Loads developments cards from a json file and creates the development card grid, putting the cards in the right cells based on the level and card type
     * @param path Path of the json file containing the  list of development cards
     * @throws IOException Error while reading file i.e. wrong path
     */
    public void loadDevelopmentCardsFromFile(String path) throws IOException {
        File file=new File(path);
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        Gson gson=new Gson();
        try{
            DevelopmentCard[] developmentCards=gson.fromJson(content, DevelopmentCard[].class);
            for(DevelopmentCard d:developmentCards){
                developmentCardGrid.addCard(d);
            }
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for development cards");
        }
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }
}