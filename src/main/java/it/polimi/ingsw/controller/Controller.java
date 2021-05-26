package it.polimi.ingsw.controller;

import it.polimi.ingsw.ClientHandler;
import it.polimi.ingsw.GameState;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.exceptions.*;
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

    /**
     * Send a message to all the client handlers
     * @param message Message to be sent to all the client handlers
     */
    public synchronized void broadcast(Message message){
        for(ClientHandler ch:clientHandlers)
            ch.send(message);
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

    public synchronized void showInitialLeaderCards(ClientHandler clientHandler){
        String[] ids=game.getInitialLeaderCards(clientHandlers.indexOf(clientHandler));
        clientHandler.send(new Message(MessageType.SHOW_LEADER_CARDS,ids));
    }

    public synchronized void showLeaderCards(ClientHandler clientHandler) {
        String[] ids=game.getLeaderCards(clientHandler.getPlayerName());
        clientHandler.send(new Message(MessageType.ASK_LEADER_ACTION,ids));
    }

    /**
     * Inform the other player who has received the inkwell
     * @param playerName name of the player that received the inkwell
     */
    public void inkwellGiven(String playerName){
        game.giveInkwell();
        for(ClientHandler c:clientHandlers){
            c.getAutomaton().evolve("INKWELL_DISTRIBUTED",new String[]{c.getPlayerName()});
        }
    }

    /**
     * Player has chosen the leader cards to keep and those to discard. Add them to the personal board
     * @param clientHandler Client handler that sent the request
     * @param leaderCardsId Ids of the leader cards chosen
     * @return Whether or not the leader cards could be added to the personal board (ie. they were among the 4 given)
     */
    public synchronized boolean leaderCardsChosen(ClientHandler clientHandler,String[] leaderCardsId){
        if(game.addLeaderCards(clientHandlers.indexOf(clientHandler),leaderCardsId)){
            boolean ok=true;
            for(ClientHandler c:clientHandlers){
                if(c.getAutomaton().getState()!=GameState.LEADER_CARDS_CHOSEN){
                    ok=false;
                    break;
                }
            }
            if(ok){
                game.giveInkwell();
                String[] playerOrder=game.getPlayerOrder();
                for (ClientHandler c:clientHandlers){
                    c.getAutomaton().evolve("DISTRIBUTE_INKWELL",playerOrder);
                    if(game.getPlayerOrdinal(c.getPlayerName())!=0){
                        c.getAutomaton().evolve("ASK_INITIAL_RESOURCES",null);
                    }else{
                        c.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    }

                }
            }
            return true;
        }
        return false;
    }

    /**
     * Add the initial additional resource to a player's depot
     * @param clientHandler Client handler of the player
     * @param resource Type of resource to be added
     */
    public synchronized boolean addInitialResource(ClientHandler clientHandler, ResType resource){
        if(resource!=ResType.FAITH){
            String playerName=clientHandler.getPlayerName();
            for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{playerName,resource.toString()}));
            }
            game.addInitialResource(playerName,resource);

            //Check based on the player order, if they are allowed another additional resource
            //Third and fourth player are granted a faith point, fourth player is granted an additional resource
            switch(game.getPlayerOrdinal(playerName)){
                case 2:
                    game.getPersonalBoard(playerName).incrementFaithTrack(1);
                    for(ClientHandler c:clientHandlers){
                        c.send(new Message(MessageType.INCREMENT_FAITH_TRACK,new String[]{playerName,Integer.toString(1)}));
                    }
                    clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    break;
                case 3:
                    if(!game.getPersonalBoard(playerName).hasAlreadyChosenInitialResources()){
                        game.getPersonalBoard(playerName).setHasAlreadyChosenInitialResources(true);
                        clientHandler.getAutomaton().evolve("ASK_INITIAL_RESOURCES",null);
                    }else{
                        game.getPersonalBoard(playerName).incrementFaithTrack(1);
                        for(ClientHandler c:clientHandlers){
                            c.send(new Message(MessageType.INCREMENT_FAITH_TRACK,new String[]{playerName,Integer.toString(1)}));
                        }
                        clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    }
                    break;
                default:
                    clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
            }
            return true;
        }
        return false;
    }

    public synchronized void checkIfGameCanStart(){
        boolean ok=true;
        for(ClientHandler c:clientHandlers){
            if(c.getAutomaton().getState()!=GameState.WAITING_FOR_YOUR_TURN){
                ok=false;
                break;
            }
        }
        if(ok){
            //Start the game
            String activePlayer=game.getCurrentPlayer();
            for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.TURN_START,new String[]{activePlayer}));
                if(c.getPlayerName().equals(activePlayer)){
                    c.getAutomaton().evolve("ASK_LEADER_ACTION",null);
                }
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

    /**
     * Ask the player if the want to play a leader action
     * @return Whether or not the player wants to play a leader action
     */
    public boolean askForLeaderAction(String playerName){
        System.out.println("Ask player "+playerName+" if they want to play a leader action");
        return true;
    }

    public void activateLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException {
        game.activateLeaderCards(clientHandler.getPlayerName(),id);
    }

    public void discardLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException {
        game.discardLeaderCards(clientHandler.getPlayerName(), id);
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
