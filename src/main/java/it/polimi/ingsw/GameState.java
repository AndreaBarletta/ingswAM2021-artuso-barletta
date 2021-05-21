package it.polimi.ingsw;

public enum GameState{
    UNKNOWN,
    PLAYER_CONNECTED,NICKNAME_CHOSEN,
    NUMBER_OF_PLAYERS_ASKED,GAME_CREATED,
    GAME_JOINED,WAITING_FOR_OTHER_PLAYERS, NEW_PLAYER, GAME_STARTED,
    LEADER_CARDS_SHOWN,LEADER_CARDS_CHOSEN,
    INKWELL_DISTRIBUTED,
    DISTRIBUTE_ADDITIONAL_RESOURCES,
    LEADER_ACTION_BEGIN,ACTIVATE_PRODUCTION,BUY_DEV_CARD,GET_RESOURCES,LEADER_ACTION_END,
    WAIT_FOR_OTHERS_TURN;

    public boolean canEvolve(String input){
        switch(this){
            case PLAYER_CONNECTED:
                if(input.equals("PICK_PLAYERNAME"))
                    return true;
                break;
            case NICKNAME_CHOSEN:
                if(input.equals("JOIN_GAME")||input.equals("ASK_NUMBER_OF_PLAYERS"))
                    return true;
                break;
            case NUMBER_OF_PLAYERS_ASKED:
                if(input.equals("NUMBER_OF_PLAYERS"))
                    return true;
                break;
            case GAME_CREATED:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS")||(input.equals("ASK_NUMBER_OF_PLAYERS")))
                    return true;
                break;
            case GAME_JOINED:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS"))
                    return true;
                break;
            case WAITING_FOR_OTHER_PLAYERS:
                if(input.equals(("NEW_PLAYER")) || input.equals("START_GAME"))
                    return true;
                break;
            case NEW_PLAYER:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS"))
                    return true;
                break;
            case GAME_STARTED:
                if(input.equals("SHOW_LEADER_CARDS"))
                    return true;
                break;
            case LEADER_CARDS_SHOWN:
                if(input.equals("CHOOSE_LEADER_CARDS"))
                    return true;
                break;
            case LEADER_CARDS_CHOSEN:
                if(input.equals("DISTRIBUTE_INKWELL"))
                    return true;
                break;
            case INKWELL_DISTRIBUTED:
                if(input.equals("INKWELL_DISTRIBUTED"))
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
            case WAIT_FOR_OTHERS_TURN:
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
                if(input.equals("JOIN_GAME")) return GAME_JOINED;
                if(input.equals("ASK_NUMBER_OF_PLAYERS")) return NUMBER_OF_PLAYERS_ASKED;
            case NUMBER_OF_PLAYERS_ASKED:
                return GAME_CREATED;
            case GAME_CREATED:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS")) return WAITING_FOR_OTHER_PLAYERS;
                if(input.equals("ASK_NUMBER_OF_PLAYERS")) return NUMBER_OF_PLAYERS_ASKED;
            case GAME_JOINED:
                return WAITING_FOR_OTHER_PLAYERS;
            case WAITING_FOR_OTHER_PLAYERS:
                if(input.equals("NEW_PLAYER"))      return NEW_PLAYER;
                if(input.equals("START_GAME"))    return GAME_STARTED;
            case NEW_PLAYER:
                return WAITING_FOR_OTHER_PLAYERS;
            case GAME_STARTED:
                return LEADER_CARDS_SHOWN;
            case LEADER_CARDS_SHOWN:
                return LEADER_CARDS_CHOSEN;
            case LEADER_CARDS_CHOSEN:
                return INKWELL_DISTRIBUTED;
            case INKWELL_DISTRIBUTED:
                return DISTRIBUTE_ADDITIONAL_RESOURCES;
            case DISTRIBUTE_ADDITIONAL_RESOURCES:
                return LEADER_ACTION_BEGIN;
            case LEADER_ACTION_BEGIN:
                if (input.equals("GET_RESOURCES"))           return GET_RESOURCES;
                if (input.equals("ACTIVATE_PRODUCTION"))    return ACTIVATE_PRODUCTION;
                if (input.equals("BUY_DEV_CARD"))           return BUY_DEV_CARD;
            case GET_RESOURCES:
            case ACTIVATE_PRODUCTION:
            case BUY_DEV_CARD:
                return LEADER_ACTION_END;
            case LEADER_ACTION_END:
                return WAIT_FOR_OTHERS_TURN;
            case WAIT_FOR_OTHERS_TURN:
                return LEADER_ACTION_BEGIN;
        }
        return UNKNOWN;
    }
}
