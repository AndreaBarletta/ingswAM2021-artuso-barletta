package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
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

    /**
     * Inform the other players a new player has joined the game and add the controller to the newly-creates personal board's event listener
     * @param newPersonalBoard New personal board
     */
    public void newPersonalBoard(PersonalBoard newPersonalBoard){
        System.out.println("Player "+newPersonalBoard.getPlayerName()+" has joined");
        newPersonalBoard.addEventListener(this);
    }

    /**
     * Inform the other player who has recieved the inkwell
     * @param playerName name of the player that recieved the inkwell
     */
    public void inkwellGiven(String playerName){
        System.out.println("Player "+playerName+" has recieved the inkwell");
    }

    /**
     * Ask for the player to choose 2 leader cards among the 4 given
     * @param leaderCards 4 leader cards given by the game
     * @return index of the choose cards
     */
    public int[] chooseLeaderCards(LeaderCard[] leaderCards){
        return new int[]{1, 2};
    }
}
