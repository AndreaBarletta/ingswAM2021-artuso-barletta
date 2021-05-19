package it.polimi.ingsw;

public enum GameState{
    UNKNOWN,
    PLAYER_CONNECTED,NICKNAME_CHOSEN,
    GAME_CREATED,GAME_JOINED,GAME_STARTED,
    ACTIVATE_PRODUCTION,BUY_DEV_CARD,GET_RESOURCES;

    public boolean canEvolve(String input){
        switch(this){
            case PLAYER_CONNECTED:
                if(input.equals("PICK_PLAYERNAME")){
                    return true;
                }
                break;
            case NICKNAME_CHOSEN:
                if(input.equals("CREATE_GAME")||input.equals("JOIN_GAME")){
                    return true;
                }
                break;
            case GAME_CREATED:
            case GAME_JOINED:
                if(input.equals("START_GAME")){
                    return true;
                }
                break;
        }
        return false;
    }

    public GameState next(String input){
        switch(this){
            case PLAYER_CONNECTED:
                    return NICKNAME_CHOSEN;
            case NICKNAME_CHOSEN:
                if(input.equals("CREATE_GAME")) {
                    return GAME_CREATED;
                }else{
                    return GAME_JOINED;
                }
            case GAME_CREATED:
            case GAME_JOINED:
                return GAME_STARTED;
        }
        return UNKNOWN;
    }
}
