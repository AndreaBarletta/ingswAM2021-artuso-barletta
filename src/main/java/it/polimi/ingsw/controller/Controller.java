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
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
import it.polimi.ingsw.model.GameEventListener;

import java.util.*;
import java.util.stream.Stream;

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

            if(!game.loadDevelopmentCardGridFromFile(getClass().getClassLoader().getResource("developmentCards.json").getPath())) return false;
            if(!game.loadPopeFavourCardsFromFile(getClass().getClassLoader().getResource("popeFavourCards.json").getPath())) return false;
            if(!game.loadLeaderCardsFromFile(getClass().getClassLoader().getResource("leaderCards.json").getPath())) return false;

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

    public synchronized String[][] getDevCardsGridIds(){
        return game.getDevelopmentCardsGridIds();
    }

    public synchronized ResType[][] getMarketTray(){
        return game.getMarketTray();
    }

    public synchronized ResType getLeftoverMarble(){
        return game.getLeftoverMarble();
    }

    public synchronized void showInitialLeaderCards(ClientHandler clientHandler){
        String[] ids=game.getInitialLeaderCards(clientHandlers.indexOf(clientHandler));
        clientHandler.send(new Message(MessageType.SHOW_LEADER_CARDS,ids));
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
            broadcast(new Message(MessageType.LEADER_CARDS_CHOSEN,
                    Stream.concat(
                            Arrays.stream(new String[]{clientHandler.getPlayerName()}),
                            Arrays.stream(leaderCardsId))
                            .toArray(String[]::new))
            );
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
                    Map<ResType,Integer> testResources=new HashMap<>();
                    testResources.put(ResType.SHIELD,100);
                    testResources.put(ResType.COIN,100);
                    testResources.put(ResType.SERVANT,100);
                    testResources.put(ResType.STONE,100);
                    game.getPersonalBoard(c.getPlayerName()).addResourcesToStrongbox(testResources);
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
        if(resource!=ResType.FAITH&&resource!=ResType.ANY&&resource!=ResType.WHITEMARBLE){
            String playerName=clientHandler.getPlayerName();
            broadcast(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{playerName,resource.name()}));
            game.addInitialResource(playerName,resource);

            //Check based on the player order, if they are allowed another additional resource
            //Third and fourth player are granted a faith point, fourth player is granted an additional resource
            switch(game.getPlayerOrdinal(playerName)){
                case 2:
                    game.getPersonalBoard(playerName).incrementFaithTrack(1);
                    broadcast(new Message(MessageType.INCREMENT_FAITH_TRACK,new String[]{playerName,Integer.toString(1)}));
                    clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    break;
                case 3:
                    if(!game.getPersonalBoard(playerName).hasAlreadyChosenInitialResources()){
                        game.getPersonalBoard(playerName).setHasAlreadyChosenInitialResources(true);
                        clientHandler.getAutomaton().evolve("ASK_INITIAL_RESOURCES",null);
                    }else{
                        game.getPersonalBoard(playerName).incrementFaithTrack(1);
                        broadcast(new Message(MessageType.INCREMENT_FAITH_TRACK,new String[]{playerName,Integer.toString(1)}));
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
     * Tell the other players who won the game
     * @param playerName Name of the player who won the game
     */
    public void announceWinner(String playerName){
        System.out.println("Player "+playerName+" has won the game");
    }

    public void activateLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException {
        game.activateLeaderCards(clientHandler.getPlayerName(),id);
    }

    public void discardLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException {
        game.discardLeaderCards(clientHandler.getPlayerName(), id);
    }

    public void addedLeaderProduction(String playerName) {
        broadcast(new Message(MessageType.LEADER_ACTIVATED,new String[]{playerName}));
    }

    public void activateProductions(String playerName, String[] productionsId) throws ResourcesException {
        //convert ids in productions
        Production[]  productions = new Production[productionsId.length];
        PersonalBoard pb=game.getPersonalBoard(playerName);
        int i=0;
        for(String p : productionsId) {
            if(p.equals("0")) {
                productions[i]=pb.getBaseProduction();
                i++;
            }
            else if(p.equals("1")) {
                productions[i]=pb.getDevelopmentCardSlots()[1].getTopCard().getProduction();
                i++;
            }
            else if(p.equals("2")) {
                productions[i]=pb.getDevelopmentCardSlots()[2].getTopCard().getProduction();
                i++;
            }
            else if(p.equals("3")) {
                productions[i]=pb.getDevelopmentCardSlots()[3].getTopCard().getProduction();
                i++;
            }
            else if(p.equals("4")) {
                productions[i]=pb.getLeaderProductions().get(1);
                i++;
            }
            else if(p.equals("5")) {
                productions[i]=pb.getLeaderProductions().get(2);
                i++;
            }
        }

        game.activateProductions(playerName, productions);
    }

    public void canBuyDevCard(ClientHandler clienthandler,String id) throws ResourcesException,LevelException {
        game.canBuyDevCard(clienthandler.getPlayerName(),id);
    }

    public void addDevCardToSlot(ClientHandler clientHandler,String id,String slot) throws LevelException{
        game.addDevCardToSlot(clientHandler.getPlayerName(),id,slot);
    }

    public void acquireFromMarket(ClientHandler clientHandler, String rowOrColumn, String index){
        game.acquireFromMarket(clientHandler.getPlayerName(), rowOrColumn.equals("row"), Integer.parseInt(index));
        broadcast(new Message(MessageType.UPDATE_MARKET,new String[]{rowOrColumn,index}));
    }

    public String[] removeDevCardFromMarket(String id){
        return game.removeDevCardFromMarket(id);
    }
}
