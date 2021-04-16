package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
import it.polimi.ingsw.model.GameEventListener;

import java.util.*;

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
     * Ask the player to choose 2 leader cards among the 4 given
     * @param leaderCards 4 leader cards given by the game
     * @param playerName The name of the player
     * @return index of the choose cards
     */
    public int[] chooseLeaderCards(LeaderCard[] leaderCards,String playerName){
        System.out.println("Ask player "+playerName+" to choose 2 leader cards");
        return new int[]{1, 2};
    }

    /**
     * Ask the player to choose a development card from the grid
     * @param cardGrid The card grid
     * @param playerName The name of the player
     * @return The card selected
     */
    public DevelopmentCard chooseDevelopmentCard(DevelopmentCardGrid cardGrid, String playerName){
        System.out.println("Ask player "+playerName+" which card to buy");
        return cardGrid.getTopCard(1,CardType.GREEN);
    }

    public int chooseDevelopmentCardSlot(DevelopmentCardSlot[] developmentCardSlots, DevelopmentCard card, String playerName){
        System.out.println("Ask player "+playerName+" where to put the card");
        return 0;
    }

    /**
     * Sends an error message to the player
     * @param error Error message
     */
    public void error(String error){
        System.out.println(error);
    }
}
