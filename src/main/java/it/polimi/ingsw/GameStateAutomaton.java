package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.CardTypeException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

public class GameStateAutomaton {
    private GameState state;
    private String errorMessage;
    private ClientHandler clientHandler;
    private Controller controller;
    private int tempId;

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
                    clientHandler.send(new Message(MessageType.SET_PLAYERNAME,new String[]{params[0]}));
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
                    clientHandler.send(new Message(MessageType.SET_MARKET,new String[]{gson.toJson(controller.getMarketTray()),controller.getLeftoverMarble().name()}));
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
                    try{
                        ResType.valueOf(params[0]);
                    }catch(IllegalArgumentException e){
                        state=GameState.INITIAL_RESOURCES_ASKED;
                        errorMessage="Unknown resource specified";
                        return false;
                    }
                    if(!controller.addInitialResource(clientHandler,ResType.valueOf(params[0]))){
                        state=GameState.INITIAL_RESOURCES_ASKED;
                        errorMessage="Cannot add the specified resource";
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
                    controller.broadcast(new Message(MessageType.LEADER_ACTIVATED,new String[]{clientHandler.getPlayerName(),params[0]}));
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
                    controller.broadcast(new Message(MessageType.LEADER_DISCARDED,new String[]{clientHandler.getPlayerName(),params[0]}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case LEADER_ACTION_SKIPPED:
                    controller.broadcast(new Message(MessageType.LEADER_SKIPPED,new String[]{clientHandler.getPlayerName()}));
                    evolve("ASK_TURN_ACTION",null);
                    return true;
                case TURN_ACTION_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_TURN_ACTION,null));
                    return true;
                case PRODUCTIONS_SHOWN:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE,new String[]{clientHandler.getPlayerName(),"activate productions"}));
                    clientHandler.send(new Message(MessageType.SHOW_PRODUCTIONS,new String[]{}));
                    return true;
                case PRODUCTION_CHOSEN:
                    try {
                        controller.activateProductions(clientHandler.getPlayerName(), params);
                    } catch (ResourcesException e) {
                        state=GameState.PRODUCTIONS_SHOWN;
                        errorMessage="You don't have enough resources to activate the selected productions";
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.SHOW_CHOSEN_PRODUCTIONS, Stream.concat(Arrays.stream(new String[]{clientHandler.getPlayerName()}), Arrays.stream(params)).toArray(String[]::new)));
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
                    tempId=Integer.parseInt(params[0]);
                    evolve("UPDATE_DEV_CARD_GRID",null);
                    return true;
                case DEV_CARD_GRID_UPDATED:
                    String[] messageParams=controller.removeDevCardFromMarket(String.valueOf(tempId));
                    controller.broadcast(new Message(MessageType.UPDATE_DEV_CARD_GRID,messageParams));
                    evolve("ASK_DEV_CARD_SLOT",null);
                    return true;
                case DEV_CARD_SLOT_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_DEV_CARD_SLOT,null));
                    return true;
                case DEV_CARD_SLOT_CHOSEN:
                    try {
                        controller.addDevCardToSlot(clientHandler, String.valueOf(tempId),params[0]);
                    }catch(LevelException e){
                        errorMessage="Cannot add the card to the selected slot";
                        state=GameState.DEV_CARD_SLOT_ASKED;
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.UPDATE_DEV_CARD_SLOT,new String[]{clientHandler.getPlayerName(),String.valueOf(tempId),params[0]}));
                    return true;
                case MARKET_SHOWN:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE, new String[]{clientHandler.getPlayerName(),"visit market"}));
                    clientHandler.send(new Message(MessageType.SHOW_MARKET, null));
                    return true;
                case ROW_OR_COLUMN_CHOSEN:
                    if(!params[0].equals("row")&&!params[0].equals("column")){
                        state=GameState.MARKET_SHOWN;
                        errorMessage="Choose (row) or (column)";
                        return false;
                    }
                    if(params[0].equals(("row")))
                        if(Integer.parseInt(params[1])>2||Integer.parseInt(params[1])<0){
                            state=GameState.MARKET_SHOWN;
                            errorMessage="Choose a number between 0 and 2";
                            return false;
                        }
                    else
                        if(Integer.parseInt(params[1])>3||Integer.parseInt(params[1])<0){
                            state=GameState.MARKET_SHOWN;
                            errorMessage="Choose a number between 0 and 3";
                            return false;
                        }
                    controller.acquireFromMarket(clientHandler, params[0],params[1]);
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
