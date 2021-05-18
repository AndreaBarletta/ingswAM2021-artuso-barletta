package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;

public class GameStateAutomaton {
    private GameState state;
    private String errorMessage;

    public GameStateAutomaton(){
        state=GameState.PLAYER_CONNECTED;
    }

    public boolean evolve(Controller controller, ClientHandler clientHandler,Message message){
        if(state.canEvolve(message.messageType.toString())){
            state=state.next(message.messageType.toString());
            switch(state){
                case PLAYER_CONNECTED:
                    return true;
                case NICKNAME_CHOSEN:
                    clientHandler.setPlayerName(message.params[0]);
                    if(!controller.addClientHandler(clientHandler)){
                        errorMessage="Player with the same name already exists";
                        state=GameState.PLAYER_CONNECTED;
                        return false;
                    }
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
