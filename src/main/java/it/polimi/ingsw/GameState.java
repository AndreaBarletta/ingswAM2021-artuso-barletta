package it.polimi.ingsw;

public enum GameState{
    UNKNOWN,
    PLAYER_CONNECTED,NICKNAME_CHOSEN,GAME_CREATED,WAITING_FOR_OTHER_PLAYERS, NEW_PLAYER,
    GAME_STARTED, INKWELL_DISTRIBUTE,LEADER_CARDS_CHOICE,DISTRIBUTE_ADDITIONAL_RESOURCES,
    LEADER_ACTION_BEGIN,ACTIVATE_PRODUCTION,BUY_DEV_CARD,GET_RESOURCES,LEADER_ACTION_END,
    WAIT_FOR_OTHERS;

    public boolean canEvolve(String input){
        switch(this){
            case PLAYER_CONNECTED:
                if(input.equals("PICK_PLAYERNAME"))
                    return true;
                break;
            case NICKNAME_CHOSEN:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS")||input.equals("CREATE_GAME"))
                    return true;
                break;
            case GAME_CREATED:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS"))
                    return true;
                break;
            case WAITING_FOR_OTHER_PLAYERS:
                if(input.equals(("NEW_PLAYER")) || input.equals("GAME_STARTED"))
                    return true;
                break;
            case NEW_PLAYER:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS"))
                    return true;
            case INKWELL_DISTRIBUTE:
                if(input.equals("INKWELL_DISTRIBUTED"))
                    return true;
                break;
            case LEADER_CARDS_CHOICE:
                if(input.equals("LEADER_CARDS_CHOSEN"))
                    return true;
                break;
            case DISTRIBUTE_ADDITIONAL_RESOURCES:
                if(input.equals("ADDITIONAL_RESOURCES_DISTRIBUTED"))
                    return true;
                break;
            case LEADER_ACTION_BEGIN:
                if(input.equals("ACTIVATE") || input.equals("DISCARD") || input.equals("NO"))
                    return true;
                break;
            case GET_RESOURCES:
                if(input.equals("RESOURCES_ACQUIRED"))
                    return true;
                break;
            case ACTIVATE_PRODUCTION:
                if(input.equals("PRODUCTION_COMPLETED"))
                    return true;
                break;
            case BUY_DEV_CARD:
                if(input.equals("CARD_BUYED"))
                    return true;
            case LEADER_ACTION_END:
                if(input.equals("ACTIVATE") || input.equals("DISCARD") || input.equals("NO"))
                    return true;
                break;
            case WAIT_FOR_OTHERS:
                if(input.equals("MY_TURN"))
                    return true;
                break;
        }
        return false;
    }

    public GameState next(String input){
        switch(this){
            case PLAYER_CONNECTED:
                    return NICKNAME_CHOSEN;
            case NICKNAME_CHOSEN:
                return WAITING_FOR_OTHER_PLAYERS;
            case WAITING_FOR_OTHER_PLAYERS:
                if(input.equals("NEW_PLAYER"))      return NEW_PLAYER;
                if(input.equals("GAME_STARTED"))    return GAME_STARTED;
            case NEW_PLAYER:
                return WAITING_FOR_OTHER_PLAYERS;
            case INKWELL_DISTRIBUTE:
                return LEADER_CARDS_CHOICE;
            case LEADER_CARDS_CHOICE:
                return DISTRIBUTE_ADDITIONAL_RESOURCES;
            case DISTRIBUTE_ADDITIONAL_RESOURCES:
                return LEADER_ACTION_BEGIN;
            case LEADER_ACTION_BEGIN:
                if(input.equals("GET_RESOURCES"))           return GET_RESOURCES;
                if (input.equals("ACTIVATE_PRODUCTION"))    return ACTIVATE_PRODUCTION;
                if (input.equals("BUY_DEV_CARD"))           return BUY_DEV_CARD;
            case GET_RESOURCES:
            case ACTIVATE_PRODUCTION:
            case BUY_DEV_CARD:
                return LEADER_ACTION_END;
            case LEADER_ACTION_END:
                return WAIT_FOR_OTHERS;
            case WAIT_FOR_OTHERS:
                return LEADER_ACTION_BEGIN;
        }
        return UNKNOWN;
    }
}
