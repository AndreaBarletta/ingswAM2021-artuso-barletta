package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.ResType;

public class GameStateAutomaton {
    private GameState state;
    private String errorMessage;
    private ClientHandler clientHandler;
    private Controller controller;

    public GameStateAutomaton(Controller controller, ClientHandler clientHandler){
        state=GameState.PLAYER_CONNECTED;
        this.controller=controller;
        this.clientHandler=clientHandler;
    }

    /**
     * Evolve the game state automaton
     * @param input Transition message
     * @param params Additional parameters
     * @return Whether or not the evolve was successful
     */
    public boolean evolve(String input,String[] params){
        if(state.canEvolve(input)){
            state=state.next(input);
            System.out.println(clientHandler.getPlayerName()+" went into state "+state);
            switch(state){
                case PLAYER_CONNECTED:
                    return true;
                case NICKNAME_CHOSEN:
                    clientHandler.setPlayerName(params[0]);
                    if(!controller.addClientHandler(clientHandler)){
                        errorMessage="Player with the same name already exists";
                        state=GameState.PLAYER_CONNECTED;
                        return false;
                    }
                    return true;
                case NUMBER_OF_PLAYERS_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_NUMBER_OF_PLAYERS,null));
                    return true;
                case GAME_CREATED:
                    if(!controller.createGame(clientHandler,Integer.parseInt(params[0]))) {
                        errorMessage="Error while creating a new game";
                        state=GameState.NUMBER_OF_PLAYERS_ASKED;
                        return false;
                    }
                    return true;
                case GAME_JOINED:
                    String[] players=controller.getPlayers();
                    clientHandler.send(new Message(MessageType.GAME_JOINED,players));
                    return true;
                case WAITING_FOR_OTHER_PLAYERS:
                    clientHandler.send(new Message(MessageType.WAIT_FOR_OTHER_PLAYERS, null));
                    return true;
                case NEW_PLAYER:
                    clientHandler.send(new Message(MessageType.NEW_PLAYER,params));
                    state=GameState.WAITING_FOR_OTHER_PLAYERS;
                    return true;
                case GAME_STARTED:
                    clientHandler.send(new Message(MessageType.GAME_STARTED,null));
                    evolve("SHOW_LEADER_CARDS",null);
                    return true;
                case LEADER_CARDS_SHOWN:
                        controller.showInitialLeaderCards(clientHandler);
                        return true;
                case LEADER_CARDS_CHOSEN:
                        if(!controller.leaderCardsChosen(clientHandler,params)) {
                            errorMessage = "Invalid leader cards chosen";
                            state=GameState.LEADER_CARDS_SHOWN;
                            return false;
                        }
                        return true;
                case INKWELL_DISTRIBUTED:
                    clientHandler.send(new Message(MessageType.INKWELL_GIVEN,params));
                    return true;
                case INITIAL_RESOURCES_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_INITIAL_RESOURCES,null));
                    return true;
                case INITIAL_RESOURCES_CHOSEN:
                    if(!controller.addInitialResource(clientHandler,ResType.valueOf(params[0]))){
                        state=GameState.INITIAL_RESOURCES_ASKED;
                        errorMessage="Cannot add faith points as initial resource";
                        return false;
                    }
                    return true;
                case WAITING_FOR_YOUR_TURN:
                    clientHandler.send(new Message(MessageType.WAIT_YOUR_TURN,null));
                    controller.checkIfGameCanStart();
                    return true;
                case LEADER_ACTION_ASKED:
                    controller.showLeaderCards(clientHandler);
                    return true;
                case LEADER_ACTION_ACTIVATED:
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_ACTIVATE,new String[]{clientHandler.getPlayerName()}));
                    return true;
                case LEADER_ACTION_DISCARDED:
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_DISCARD,new String[]{clientHandler.getPlayerName()}));
                    return true;
                case LEADER_ACTION_SKIPPED:
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_SKIP,new String[]{clientHandler.getPlayerName()}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case TURN_ACTION_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_TURN_ACTION,null));
                    return true;
                case PRODUCTIONS_ACTIVATED:
                    clientHandler.send(new Message(MessageType.ERROR,new String[]{"ACTIVATE PRODUCTIONS"}));
                    return true;
                case DEV_CARD_BOUGHT:
                    clientHandler.send(new Message(MessageType.ERROR,new String[]{"BUY DEV CARDS"}));
                    return true;
                case MARKET_VISITED:
                    clientHandler.send(new Message(MessageType.ERROR,new String[]{"VISIT MARKET"}));
                    return true;
            }
            errorMessage="Unknown state";
            return false;
        }
        errorMessage="Cannot use that command now";
        return false;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public GameState getState() {
        return state;
    }
}
