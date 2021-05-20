package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;

public class GameStateAutomaton {
    private GameState state;
    private String errorMessage;

    public GameStateAutomaton(){
        state=GameState.PLAYER_CONNECTED;
    }

    public boolean evolve(Controller controller, ClientHandler clientHandler,String input,String[] params){
        if(state.canEvolve(input)){
            state=state.next(input);
            System.out.println("Go into state "+state);
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
                case WAITING_FOR_OTHER_PLAYERS:
                    clientHandler.send(new Message(MessageType.WAIT_FOR_OTHER_PLAYERS, new String[]{}));
                    return true;
                case NEW_PLAYER:
                    clientHandler.send(new Message(MessageType.NEW_PLAYER,params));
                    state=GameState.WAITING_FOR_OTHER_PLAYERS;
                    return true;
                case INKWELL_DISTRIBUTE:
                    controller.inkwellGiven(clientHandler.getPlayerName());
                    clientHandler.send(new Message(MessageType.INKWELLGIVEN,params));
                    state=state.next("");
                    return true;
            }
            errorMessage="Unknown state";
        }
        errorMessage="Cannot use that command now";
        return false;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

}
