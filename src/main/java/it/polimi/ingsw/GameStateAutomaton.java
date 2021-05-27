package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.CardTypeException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
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
                    Gson gson=new Gson();
                    clientHandler.send(new Message(MessageType.SET_DEV_CARD_GRID,new String[]{gson.toJson(controller.getDevCardsGridIds())}));
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
                    try {
                        controller.activateLeaderCard(clientHandler,params[0]);
                    } catch (CardNotFoundException e) {
                        errorMessage="Card not found";
                        state=GameState.LEADER_ACTION_ASKED;
                        return false;
                    } catch (CardTypeException e) {
                        errorMessage = "Not enough card of requested type";
                        state = GameState.LEADER_ACTION_ASKED;
                        return false;
                    } catch (ResourcesException e) {
                        errorMessage = "Not enough resources of requested type";
                        state = GameState.LEADER_ACTION_ASKED;
                        return false;
                    } catch (LevelException e) {
                        errorMessage = "Not enough card of requested level";
                        state = GameState.LEADER_ACTION_ASKED;
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_ACTIVATE,new String[]{clientHandler.getPlayerName(),params[0]}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case LEADER_ACTION_DISCARDED:
                    try {
                        controller.discardLeaderCard(clientHandler,params[0]);
                    } catch (CardNotFoundException e) {
                        errorMessage="Card not found";
                        state=GameState.LEADER_ACTION_ASKED;
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_DISCARD,new String[]{clientHandler.getPlayerName(),params[0]}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case LEADER_ACTION_SKIPPED:
                    controller.broadcast(new Message(MessageType.LEADER_ACTION_SKIP,new String[]{clientHandler.getPlayerName()}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case TURN_ACTION_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_TURN_ACTION,null));
                    return true;
                case PRODUCTIONS_ACTIVATED:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE,new String[]{clientHandler.getPlayerName(),"activate productions"}));
                    clientHandler.send(new Message(MessageType.SHOW_PRODUCTIONS,null));
                    return true;
                case PRODUCTION_CHOSEN:
                    //TODO
                    return true;
                case RESOURCE_UPDATED:
                    //TODO
                    return true;
                case DEV_CARD_GRID_SHOWN:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE,new String[]{clientHandler.getPlayerName(),"buy development card"}));
                    clientHandler.send(new Message(MessageType.SHOW_DEV_CARD_GRID,null));
                    return true;
                case DEV_CARD_CHOSEN:
                    try{
                        controller.canBuyDevCard(clientHandler,params[0]);
                    }catch(ResourcesException e){
                        state=GameState.DEV_CARD_GRID_SHOWN;
                        errorMessage="You don't have enough resources to buy the selected development card";
                        return false;
                    }catch(LevelException e){
                        state=GameState.DEV_CARD_GRID_SHOWN;
                        errorMessage="You don't have the required cards to buy the selected development card";
                        return false;
                    }
                    evolve("ASK_DEV_CARD_SLOT",null);
                    return true;
                case MARKET_SHOWN:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE, new String[]{clientHandler.getPlayerName(),"visit market"}));
                    clientHandler.send(new Message(MessageType.SHOW_MARKET, null));
                    return true;
                case RESOURCES_CHOSEN:
                    controller.broadcast(new Message(MessageType.CHOOSE_RESOURCES, new String[]{clientHandler.getPlayerName(), params[1]}));
                case MARKET_UPDATED:
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
