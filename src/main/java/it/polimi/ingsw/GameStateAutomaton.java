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
                    return true;
                case GAME_CREATED:
                    controller.createGame(clientHandler,message.params[0],Integer.parseInt(message.params[1]));
                    return true;
                case GAME_JOINED:
                    controller.joinGame(clientHandler);
                    return true;
            }
            errorMessage="Unknown state";
        }
        return false;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

}
