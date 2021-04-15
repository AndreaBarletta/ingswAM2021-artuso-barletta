package it.polimi.ingsw.view;
import it.polimi.ingsw.controller.ControllerEventListener;

import java.util.ArrayList;
import java.util.List;

public class CliView{
    private List<ViewEventListener> eventListeners;

    public CliView(){
        eventListeners=new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
     */
    public void addEventListener(ViewEventListener newEventListener){
        eventListeners.add(newEventListener);
    }
}
