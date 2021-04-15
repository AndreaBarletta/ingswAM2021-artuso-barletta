package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.PersonalBoardEventListener;
import it.polimi.ingsw.model.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public class Controller implements PersonalBoardEventListener,GameEventListener {
    private List<ControllerEventListener> eventListeners;

    public Controller(){
        eventListeners=new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
     */
    public void addEventListener(ControllerEventListener newEventListener){
        eventListeners.add(newEventListener);
    }
}
