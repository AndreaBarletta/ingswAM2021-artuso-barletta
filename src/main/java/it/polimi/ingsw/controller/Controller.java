package it.polimi.ingsw.controller;

import it.polimi.ingsw.ClientHandler;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.exceptions.DuplicatedIdException;
import it.polimi.ingsw.exceptions.GameSizeExceeded;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
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

    /**
     * Creates a game and add the player who created it to the game
     * @param clientHandler Player that created the game
     * @param gameName Name of the game
     * @param maximumPlayers Maximum number of player (between 2 and 4)
     */
    public synchronized void createGame(ClientHandler clientHandler, String gameName,int maximumPlayers){
        if(maximumPlayers>=2&&maximumPlayers<=4){
            clientHandlers.add(clientHandler);
            System.out.println("Player has created the game "+gameName);
            game=new Game(gameName,maximumPlayers);
            addEventListener(game);
            game.addEventListener(this);
            try {
                game.addPlayer(clientHandler.getPlayerName());
            }catch(Exception e){}
            clientHandler.setExpectedMessageType(new MessageType[]{});
        }else{
            clientHandler.send(new Message(MessageType.ERROR,new String[]{"Invalid number of players"}));
        }

    }

    /**
     * Adds a player to an existing game
     * @param clientHandler The player that wants to join
     */
    public synchronized void joinGame(ClientHandler clientHandler){
        System.out.println("Player has joined the game ");

        try{
            boolean canStart=game.addPlayer(clientHandler.getPlayerName());
            //Notify other players
            for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.NEWPLAYER,new String[]{clientHandler.getPlayerName()}));
            }

            clientHandlers.add(clientHandler);
            clientHandler.setExpectedMessageType(new MessageType[]{});
            if(canStart){
                //Notify all the players that the game can start
                for(ClientHandler c:clientHandlers){
                    c.send(new Message(MessageType.STARTGAME,new String[]{}));
                }
                game.addPersonalBoardsEventListener(this);
                game.start();
                clientHandler.setExpectedMessageType(new MessageType[]{MessageType.CHOOSELEADERCARDS});
            }
        }catch(DuplicatedIdException e){
            clientHandler.send(new Message(MessageType.ERROR,new String[]{"Player name taken"}));
        }catch(GameSizeExceeded e){
            clientHandler.send(new Message(MessageType.ERROR,new String[]{"Game is full"}));
        }catch(Exception e){}
    }

    /**
     * A player has disconnected from the game, notify the others
     * @param clientHandler Player that disconnected from the game
     */
    public synchronized void disconnected(ClientHandler clientHandler){
        clientHandlers.remove(clientHandler);
        game.removePlayer(clientHandler.getPlayerName());
        for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.DISCONNECTED,new String[]{clientHandler.getPlayerName()}));
        }
    }
    /**
     * Inform the other player who has recieved the inkwell
     * @param playerName name of the player that recieved the inkwell
     */
    public void inkwellGiven(String playerName){
        System.out.println("Player "+playerName+" has received the inkwell");
        for(ClientHandler c:clientHandlers){
            c.send(new Message(MessageType.INKWELLGIVEN,new String[]{playerName}));
        }
    }


    public synchronized void leaderCardsChosen(ClientHandler clientHandler,int[] leaderCardsId){
    }
    /**
     * Add to the players to chosen resource
     */
    public void addInitialResource(String playerName, ResType[] resources){
        //add
        System.out.println("resource chosen has been added to player "+playerName);
        for(ClientHandler c:clientHandlers){
            c.send(new Message(MessageType.GIVENINITIALRESOURCES,new String[]{playerName}));
        }
    }

    /**
     * Ask the player to choose 2 leader cards among the 4 given
     * @param leaderCards 4 leader cards given by the game
     * @param playerName The name of the player
     * @return index of the choose cards
     */
    public void chooseLeaderCards(LeaderCard[] leaderCards,String playerName){
        for(ClientHandler c:clientHandlers){
            if(c.getPlayerName().equals(playerName)){
                c.setExpectedMessageType(new MessageType[]{MessageType.LEADERCARDSCHOSEN});
                List<Integer> idList=new ArrayList<>();
                for(LeaderCard l:leaderCards){
                    idList.add(l.getId());
                }

                c.send(new Message(MessageType.CHOOSELEADERCARDS,new String[]{Arrays.toString(idList.toArray())}));
                c.send(new Message(MessageType.OK,new String[]{"true"}));
                break;
            }
        }
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

    public void chooseOneInitialResource(String playerName) {

    }

    public void chooseTwoInitialResource(String playerName) {

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
