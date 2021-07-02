package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ResType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameStateAutomaton {
    private GameState state;
    private String errorMessage;
    private final ClientHandler clientHandler;
    private final Controller controller;
    private int tempId;
    private int[] tempDiscountIds;
    private int tempLeftToConvert;
    private int tempDiscarded;
    private List<ResType> tempAcquiredResources;

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
                    if(!controller. addClientHandler(clientHandler)){
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
                    if(params[0].equals(params[1])){
                        errorMessage="Choose two different leader cards";
                        state=GameState.LEADER_CARDS_SHOWN;
                        return false;
                    }
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
                    controller.playersWaiting();
                    return true;
                case LEADER_ACTION_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_LEADER_ACTION,null));
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
                    }catch(AlreadyActiveException e){
                        errorMessage = "The selected card has already been activated";
                        state = GameState.LEADER_ACTION_ASKED;
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.LEADER_ACTIVATED,new String[]{clientHandler.getPlayerName(),params[0]}));
                    controller.endLeaderAction(clientHandler);
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
                    controller.endLeaderAction(clientHandler);
                    return true;
                case LEADER_ACTION_SKIPPED:
                    controller.broadcast(new Message(MessageType.LEADER_SKIPPED,new String[]{clientHandler.getPlayerName()}));
                    controller.endLeaderAction(clientHandler);
                    return true;
                case TURN_ACTION_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_TURN_ACTION,null));
                    return true;
        //DEBUG ONLY
                case TURN_SKIPPED:
                    controller.endTurnAction(clientHandler);
                    evolve("ASK_LEADER_ACTION",null);
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
                    controller.broadcast(new Message(MessageType.SHOW_CHOSEN_PRODUCTIONS,
                            Stream.concat(
                                    Arrays.stream(new String[]{clientHandler.getPlayerName()}),
                                    Arrays.stream(params)
                            ).toArray(String[]::new))
                    );
                    evolve("UPDATE_RESOURCES",null);
                    return true;
                case RESOURCES_UPDATED:
                    String playerName = clientHandler.getPlayerName();
                    String lightDepotsAsJson = controller.getLightDepotsAsJson(playerName);
                    String leaderLightDepotsAsJson = controller.getLeaderLightDepotsAsJson(playerName);
                    String lightStrongboxAsJson = controller.getLightStrongboxAsJson(playerName);
                    controller.broadcast(new Message(MessageType.UPDATE_RESOURCES,new String[]{playerName,lightDepotsAsJson,leaderLightDepotsAsJson,lightStrongboxAsJson}));
                    controller.endTurnAction(clientHandler);
                    evolve("ASK_LEADER_ACTION",null);
                    return true;
                case DEV_CARD_GRID_SHOWN:
                    controller.broadcast(new Message(MessageType.TURN_CHOICE,new String[]{clientHandler.getPlayerName(),"buy development card"}));
                    clientHandler.send(new Message(MessageType.SHOW_DEV_CARD_GRID,null));
                    return true;
                case DEV_CARD_CHOSEN:
                    try{
                        if(params.length==2||params.length==3) {
                            if (!controller.canDiscount(clientHandler, Arrays.asList(params).subList(2, params.length).toArray(String[]::new))) {
                                errorMessage = "Invalid discount id";
                                state = GameState.DEV_CARD_GRID_SHOWN;
                                return false;
                            }
                            tempDiscountIds= Arrays.stream(
                                    Arrays.asList(params).
                                    subList(1, params.length).
                                    toArray(String[]::new)
                            ).mapToInt(Integer::parseInt).toArray();
                        }else{
                            controller.canBuyDevCard(clientHandler,params[0],null);
                            tempDiscountIds=null;
                        }

                    }catch(ResourcesException e){
                        state=GameState.DEV_CARD_GRID_SHOWN;
                        errorMessage="You don't have enough resources to buy the selected development card";
                        return false;
                    }catch(LevelException e){
                        state=GameState.DEV_CARD_GRID_SHOWN;
                        errorMessage="You don't have the required cards to buy the selected development card";
                        return false;
                    }catch(CardNotFoundException e){
                        state=GameState.DEV_CARD_GRID_SHOWN;
                        errorMessage="You have selected an invalid card";
                        return false;
                    }
                    tempId=Integer.parseInt(params[0]);
                    evolve("UPDATE_DEV_CARD_GRID",null);
                    return true;
                case DEV_CARD_GRID_UPDATED:
                    String[] messageParams=controller.removeDevCardFromGrid(String.valueOf(tempId));
                    controller.broadcast(new Message(MessageType.UPDATE_DEV_CARD_GRID,messageParams));
                    evolve("ASK_DEV_CARD_SLOT",null);
                    return true;
                case DEV_CARD_SLOT_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_DEV_CARD_SLOT,null));
                    return true;
                case DEV_CARD_SLOT_CHOSEN:
                    if(Integer.parseInt(params[0])!=0&&Integer.parseInt(params[0])!=1&&Integer.parseInt(params[0])!=2){
                        errorMessage="Invalid slot selected";
                        state=GameState.DEV_CARD_SLOT_ASKED;
                        return false;
                    }
                    try {
                        controller.buyDevCard(clientHandler, String.valueOf(tempId),params[0],tempDiscountIds);
                    }catch(LevelException e){
                        errorMessage="Cannot add the card to the selected slot";
                        state=GameState.DEV_CARD_SLOT_ASKED;
                        return false;
                    }
                    controller.broadcast(new Message(MessageType.UPDATE_DEV_CARD_SLOT,new String[]{clientHandler.getPlayerName(),String.valueOf(tempId),params[0]}));
                    //Check if the 7th development card as been picked
                    controller.checkDevCardEnd(clientHandler);
                    evolve("UPDATE_RESOURCES",null);
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
                    if(params[0].equals(("row"))){
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
                    }

                    ResType[] acquiredResources=controller.acquireFromMarket(clientHandler, params[0],params[1]);
                    tempLeftToConvert=0;
                    for(ResType r:acquiredResources){
                        if(r==ResType.WHITEMARBLE){
                            tempLeftToConvert+=1;
                        }
                    }

                    if(tempLeftToConvert!=0){
                        tempAcquiredResources=new ArrayList<>(Arrays.asList(acquiredResources));
                        evolve("ASK_CONVERT_RESOURCE",null);
                        return true;
                    }

                    if(!controller.canAddToDepot(clientHandler,acquiredResources)){
                        tempAcquiredResources=new ArrayList<>(Arrays.asList(acquiredResources));
                        tempDiscarded=0;
                        evolve("ASK_DISCARD_RESOURCE",null);
                        return true;
                    }

                    controller.addResourcesToDepot(clientHandler,acquiredResources);
                    evolve("UPDATE_RESOURCES",null);
                    return true;
                case RESOURCE_CONVERT_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_CONVERT_RESOURCE,new String[]{String.valueOf(tempLeftToConvert)}));
                    return true;
                case RESOURCE_CONVERTED:
                case RESOURCE_DISCARD_ASKED:
                    clientHandler.send(new Message(MessageType.ASK_DISCARD_RESOURCE,new String[]{tempAcquiredResources.toString()}));
                    return true;
                case RESOURCE_DISCARDED:
                    try{
                        ResType toRemove=ResType.valueOf(params[0]);
                        if(toRemove!=ResType.WHITEMARBLE&&tempAcquiredResources.contains(toRemove)){
                            tempAcquiredResources.remove(ResType.valueOf(params[0]));
                            tempDiscarded++;
                            controller.broadcast(new Message(MessageType.RESOURCE_DISCARDED,new String[]{clientHandler.getPlayerName(),params[0]}));
                            if(!controller.canAddToDepot(clientHandler,tempAcquiredResources.toArray(ResType[]::new))){
                                evolve("ASK_DISCARD_RESOURCE",null);
                                return true;
                            }
                            controller.addResourcesToDepot(clientHandler,tempAcquiredResources.toArray(ResType[]::new));
                            controller.discardResource(clientHandler,tempDiscarded);
                            evolve("UPDATE_RESOURCES",null);
                            return true;
                        }else{
                            errorMessage="Cannot discard the selected resource";
                            state=GameState.RESOURCE_DISCARD_ASKED;
                            return false;
                        }
                    }catch(IllegalArgumentException e){
                        errorMessage="Select a valid resource type";
                        state=GameState.RESOURCE_DISCARD_ASKED;
                        return false;
                    }

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
