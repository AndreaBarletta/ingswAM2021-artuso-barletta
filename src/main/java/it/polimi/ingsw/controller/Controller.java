package it.polimi.ingsw.controller;

import it.polimi.ingsw.ClientHandler;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
import it.polimi.ingsw.model.GameEventListener;
import it.polimi.ingsw.model.PersonalBoard.TurnAction;

import java.util.*;

public class Controller implements PersonalBoardEventListener,GameEventListener {
    private List<ControllerEventListener> eventListeners;
    private List<ClientHandler> clientHandlers;
    private Game game;

    public Controller(){
        eventListeners=new ArrayList<>();
        clientHandlers=new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
     */
    public void addEventListener(ControllerEventListener newEventListener){
        eventListeners.add(newEventListener);
    }


    public synchronized void createGame(ClientHandler clientHandler, String gameName,int maximumPlayers){
        clientHandlers.add(clientHandler);
        System.out.println("Player has created the game "+gameName);
        game=new Game(gameName,maximumPlayers);
        try {
            game.addPlayer(clientHandler.getPlayerName());
        }catch(Exception e){}
    }

    public synchronized void joinGame(ClientHandler clientHandler){
        System.out.println("Player has joined the game ");
        //Notify other players
        for(ClientHandler c:clientHandlers){
            c.send(new Message(MessageType.NEWPLAYER,new String[]{clientHandler.getPlayerName()}));
        }

        clientHandlers.add(clientHandler);

        try{
            game.addPlayer(clientHandler.getPlayerName());
        }catch(Exception e){}
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

    /**
     * Tell the other players who won the game
     * @param playerName Name of the player who won the game
     */
    public void announceWinner(String playerName){
        System.out.println("Player "+playerName+" has won the game");
    }

    /**
     * Ask the player if the want to play a leader action
     * @return Whether or not the player wants to play a leader action
     */
    public boolean askForLeaderAction(String playerName){
        System.out.println("Ask player "+playerName+" if they want to play a leader action");
        return true;
    }

    /**
     * Ask the player what turn action to play
     * @param playerName The name of the player playing the turn
     * @return 0 for market, 1 to buy development card, 2 to activate production
     */
    public TurnAction askForTurnAction(String playerName){
        System.out.println("Ask player "+playerName+" what turn action to play");
        return TurnAction.ACTIVATEPRODUCTION;
    }

    /**
     * Show the player the market
     * @param market The resource market to be shown
     */
    public void showMarket(Market market,String playerName){
        System.out.println("Show player "+playerName+" the market");
    }

    /**
     * Ask player which column or row they want to acquire resources from
     * @return Key indicates if the player wants to acquire from a row, value the row / column index
     */
    public  AbstractMap.SimpleEntry<Boolean,Integer> askMarketRowColumn(String playerName){
        System.out.println("Ask player "+playerName+" which column/row to acquire");
        return new AbstractMap.SimpleEntry<>(true,1);
    }

    /**
     * Tell player which resources could not be added
     * @param resources Resources that were tried to be added
     * @param playerName Name of the player
     */
    public void notEnoughDepotSpace(ResType[] resources, String playerName){
        System.out.println("Player "+playerName+" could not add resources");
        for(ControllerEventListener c:eventListeners){

        }
    }

    public List<Production> chooseProductions(List<Production> productions, String playerName){
        System.out.println("Ask player "+playerName+" what productions to activate");
        return productions.subList(0,1);
    }
}
