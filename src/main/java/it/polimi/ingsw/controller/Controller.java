package it.polimi.ingsw.controller;

import it.polimi.ingsw.ClientHandler;
import it.polimi.ingsw.GameState;
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

import java.util.*;

public class Controller implements PersonalBoardEventListener,GameEventListener {
    private List<ControllerEventListener> eventListeners;
    private List<ClientHandler> clientHandlers;
    private Game game;
    private Thread gameThread;

    public Controller(){
        eventListeners = new ArrayList<>();
        clientHandlers = new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
     */
    public void addEventListener(ControllerEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * Adds a new client handler to the client handler list
     * @param clientHandler New client handler
     * @return Whether or not another client handler with the same name is not already present
     */
    public synchronized boolean addClientHandler(ClientHandler clientHandler){
        if(clientHandlers.size()==0){
            //First player connected, ask for size of the game
            clientHandlers.add(clientHandler);
            clientHandler.getAutomaton().evolve("ASK_NUMBER_OF_PLAYERS",null);
            return true;
        }else{
            clientHandler.getAutomaton().evolve("JOIN_GAME",null);
            try {
                game.addPlayer(clientHandler.getPlayerName());
            }catch(Exception e){
                return false;
            }

            for(ClientHandler c:clientHandlers){
                c.getAutomaton().evolve("NEW_PLAYER",new String[]{clientHandler.getPlayerName()});
            }

            clientHandlers.add(clientHandler);

            clientHandler.getAutomaton().evolve("WAIT_FOR_OTHER_PLAYERS",null);
            if(clientHandlers.size() == game.getMaximumPlayers()) {
                for (ClientHandler c : clientHandlers)
                    c.getAutomaton().evolve( "START_GAME", null);
            }
            return true;
        }
    }

    public synchronized boolean createGame(ClientHandler clientHandler,int numberOfPlayers){
        if(numberOfPlayers>=2&&numberOfPlayers<=4){
            game=new Game(numberOfPlayers);

            if(!game.loadDevelopmentCardGridFromFile("src/main/resources/developmentCards.json")) return false;
            if(!game.loadPopeFavourCardsFromFile("src/main/resources/popeFavourCards.json")) return false;
            if(!game.loadLeaderCardsFromFile("src/main/resources/leaderCards.json")) return false;

            try{
                game.addPlayer(clientHandler.getPlayerName());
            }catch(Exception e){
                return false;
            }
            clientHandler.getAutomaton().evolve("WAIT_FOR_OTHER_PLAYERS",null);
            return true;
        }
        return false;
    }

    /**
     * Get an array containing the players already present in the game
     * @return Array of players already present in the game
     */
    public synchronized String[] getPlayers() {
        List<String> players=new ArrayList<>();
        for(ClientHandler c:clientHandlers){
            players.add(c.getPlayerName());
        }
        
        String[] playersArray = new String[players.size()];
        players.toArray(playersArray);
        return playersArray;
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

    public synchronized void showLeaderCards(ClientHandler clientHandler){
        String[] ids=game.getInitialLeaderCards(clientHandlers.indexOf(clientHandler));
        clientHandler.send(new Message(MessageType.SHOW_LEADER_CARDS,ids));
    }

    /**
     * Inform the other player who has recieved the inkwell
     * @param playerName name of the player that recieved the inkwell
     */
    public void inkwellGiven(String playerName){
        game.giveInkwell();
        for(ClientHandler c:clientHandlers){
            c.getAutomaton().evolve("INKWELL_DISTRIBUTED",new String[]{c.getPlayerName()});
        }
    }


    public synchronized void leaderCardsChosen(ClientHandler clientHandler,int[] leaderCardsId){
        game.setCanProceed(true);
        game.notify();
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
    public GameState askForTurnAction(String playerName){
        System.out.println("Ask player "+playerName+" what turn action to play");
        return GameState.ACTIVATE_PRODUCTION;
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
