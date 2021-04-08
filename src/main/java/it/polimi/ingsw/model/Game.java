package it.polimi.ingsw.model;

import com.google.gson.Gson;
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

    public void loadDevelopmentCardsFromFile(String path) throws IOException {
        File file=new File(path);
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        Gson gson=new Gson();
        DevelopmentCard[] developmentCards=gson.fromJson(content, DevelopmentCard[].class);

        for(DevelopmentCard d:developmentCards){
            developmentCardGrid.addCard(d);
        }
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }
}