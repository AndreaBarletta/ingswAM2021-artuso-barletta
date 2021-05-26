package it.polimi.ingsw;

public enum GameState{
    UNKNOWN,
    PLAYER_CONNECTED,NICKNAME_CHOSEN,
    NUMBER_OF_PLAYERS_ASKED,GAME_CREATED,
    GAME_JOINED,WAITING_FOR_OTHER_PLAYERS, NEW_PLAYER, GAME_STARTED,
    LEADER_CARDS_SHOWN,LEADER_CARDS_CHOSEN,
    INKWELL_DISTRIBUTED,
    INITIAL_RESOURCES_ASKED,INITIAL_RESOURCES_CHOSEN,
    WAITING_FOR_YOUR_TURN,
    LEADER_ACTION_ASKED,
    LEADER_ACTION_ACTIVATED,LEADER_ACTION_DISCARDED,LEADER_ACTION_SKIPPED,
    TURN_ACTION_ASKED,
    PRODUCTIONS_ACTIVATED,
    DEV_CARD_GRID_SHOWN,DEV_CARD_CHOSEN,DEV_CARD_GRID_UPDATED,
    MARKET_SHOWN, ROW_CHOSEN, COLUMN_CHOSEN, MARKET_UPDATED;

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
                if(input.equals("ASK_INITIAL_RESOURCES")||input.equals("WAIT_FOR_YOUR_TURN"))
                    return true;
                break;
            case INITIAL_RESOURCES_ASKED:
                if(input.equals("CHOOSE_INITIAL_RESOURCES"))
                    return true;
                break;
            case INITIAL_RESOURCES_CHOSEN:
                if(input.equals("ASK_INITIAL_RESOURCES")||input.equals("WAIT_FOR_YOUR_TURN"))
                    return true;
                break;
            case WAITING_FOR_YOUR_TURN:
                if(input.equals("ASK_LEADER_ACTION"))
                    return true;
                break;
            case LEADER_ACTION_ASKED:
                if(input.equals("LEADER_ACTION_ACTIVATE")||input.equals("LEADER_ACTION_DISCARD")||input.equals("LEADER_ACTION_SKIP"))
                    return true;
                break;
            case LEADER_ACTION_ACTIVATED:
                if(input.equals("ASK_LEADER_ACTION")||input.equals("ASK_TURN_ACTION")||input.equals("WAIT_FOR_YOUR_TURN"))
                    return true;
                break;
            case LEADER_ACTION_DISCARDED:
                if(input.equals("ASK_LEADER_ACTION")||input.equals("ASK_TURN_ACTION")||input.equals("WAIT_FOR_YOUR_TURN"))
                    return true;
                break;
            case LEADER_ACTION_SKIPPED:
                if(input.equals("ASK_TURN_ACTION")||input.equals("WAIT_FOR_YOUR_TURN"))
                    return true;
                break;
            case TURN_ACTION_ASKED:
                if(input.equals("ACTIVATE_PRODUCTIONS")||input.equals("BUY_DEV_CARD")||input.equals("SHOW_MARKET"))
                    return true;
                break;
            case PRODUCTIONS_ACTIVATED:
                if(input.equals("ASK_TURN_ACTION")||input.equals("ASK_LEADER_ACTION"))
                    return true;
                break;
            case DEV_CARD_GRID_SHOWN:
                if(input.equals("CANCEL")||input.equals("CHOOSE_DEV_CARD"))
                    return true;
                break;
            case DEV_CARD_CHOSEN:
                if(input.equals("UPDATE_DEV_CARD_GRID")||input.equals("ASK_TURN_ACTION"))
                    return true;
                break;
            case DEV_CARD_GRID_UPDATED:
                if(input.equals("ASK_TURN_ACTION"))
                    return true;
                break;
            case MARKET_SHOWN:
                if(input.equals("CANCEL")||input.equals("CHOOSE_ROW")||input.equals("CHOOSE_COLUMN"))
                    return true;
                break;
            case ROW_CHOSEN:
            case COLUMN_CHOSEN:
                if(input.equals("UPDATE_MARKET"))
                    return true;
                break;
            case MARKET_UPDATED:
                if(input.equals("ASK_LEADER_ACTION"))
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
                if(input.equals("JOIN_GAME"))                       return GAME_JOINED;
                if(input.equals("ASK_NUMBER_OF_PLAYERS"))           return NUMBER_OF_PLAYERS_ASKED;
            case NUMBER_OF_PLAYERS_ASKED:
                return GAME_CREATED;
            case GAME_CREATED:
                if(input.equals("WAIT_FOR_OTHER_PLAYERS"))          return WAITING_FOR_OTHER_PLAYERS;
                if(input.equals("ASK_NUMBER_OF_PLAYERS"))           return NUMBER_OF_PLAYERS_ASKED;
            case GAME_JOINED:
                return WAITING_FOR_OTHER_PLAYERS;
            case WAITING_FOR_OTHER_PLAYERS:
                if(input.equals("NEW_PLAYER"))                      return NEW_PLAYER;
                if(input.equals("START_GAME"))                      return GAME_STARTED;
            case NEW_PLAYER:
                return WAITING_FOR_OTHER_PLAYERS;
            case GAME_STARTED:
                return LEADER_CARDS_SHOWN;
            case LEADER_CARDS_SHOWN:
                return LEADER_CARDS_CHOSEN;
            case LEADER_CARDS_CHOSEN:
                return INKWELL_DISTRIBUTED;
            case INKWELL_DISTRIBUTED:
                if(input.equals("ASK_INITIAL_RESOURCES"))           return INITIAL_RESOURCES_ASKED;
                if(input.equals("WAIT_FOR_YOUR_TURN"))              return WAITING_FOR_YOUR_TURN;
                return INITIAL_RESOURCES_ASKED;
            case INITIAL_RESOURCES_ASKED:
                if(input.equals("CHOOSE_INITIAL_RESOURCES"))        return INITIAL_RESOURCES_CHOSEN;
            case INITIAL_RESOURCES_CHOSEN:
                if(input.equals("ASK_INITIAL_RESOURCES"))           return INITIAL_RESOURCES_ASKED;
                if(input.equals("WAIT_FOR_YOUR_TURN"))              return WAITING_FOR_YOUR_TURN;
            case WAITING_FOR_YOUR_TURN:
                return LEADER_ACTION_ASKED;
            case LEADER_ACTION_ASKED:
                if(input.equals("LEADER_ACTION_ACTIVATE"))          return LEADER_ACTION_ACTIVATED;
                if(input.equals("LEADER_ACTION_DISCARD"))           return LEADER_ACTION_DISCARDED;
                if(input.equals("LEADER_ACTION_SKIP"))              return LEADER_ACTION_SKIPPED;
            case LEADER_ACTION_ACTIVATED:
                if(input.equals("ASK_LEADER_ACTION"))               return LEADER_ACTION_ASKED;
                if(input.equals("ASK_TURN_ACTION"))                 return TURN_ACTION_ASKED;
                if(input.equals("WAIT_FOR_YOUR_TURN"))              return WAITING_FOR_YOUR_TURN;
            case LEADER_ACTION_DISCARDED:
                if(input.equals("ASK_LEADER_ACTION"))               return LEADER_ACTION_ASKED;
                if(input.equals("ASK_TURN_ACTION"))                 return TURN_ACTION_ASKED;
                if(input.equals("WAIT_FOR_YOUR_TURN"))              return WAITING_FOR_YOUR_TURN;
            case LEADER_ACTION_SKIPPED:
                if(input.equals("ASK_TURN_ACTION"))                 return TURN_ACTION_ASKED;
                if(input.equals("WAIT_FOR_YOUR_TURN"))              return WAITING_FOR_YOUR_TURN;
            case TURN_ACTION_ASKED:
                if(input.equals("ACTIVATE_PRODUCTIONS"))            return PRODUCTIONS_ACTIVATED;
                if(input.equals("BUY_DEV_CARD"))                    return DEV_CARD_GRID_SHOWN;
                if(input.equals("SHOW_MARKET"))                     return MARKET_SHOWN;
            case PRODUCTIONS_ACTIVATED:
                if(input.equals("ASK_TURN_ACTION"))                 return TURN_ACTION_ASKED;
                if(input.equals("ASK_LEADER_ACTION"))               return LEADER_ACTION_ASKED;
            case DEV_CARD_GRID_SHOWN:
                if(input.equals("CANCEL"))                          return TURN_ACTION_ASKED;
                if(input.equals("UPDATE_DEV_CARD_GRID"))            return DEV_CARD_GRID_UPDATED;
            case DEV_CARD_CHOSEN:
                if(input.equals("UPDATE_DEV_CARD_GRID"))            return DEV_CARD_GRID_UPDATED;
                if(input.equals("ASK_TURN_ACTION"))                 return TURN_ACTION_ASKED;
            case DEV_CARD_GRID_UPDATED:
                return LEADER_ACTION_ASKED;
            case MARKET_SHOWN:
                if(input.equals("CANCEL"))                          return TURN_ACTION_ASKED;
                if(input.equals("CHOOSE_ROW"))                      return ROW_CHOSEN;
                if(input.equals("CHOOSE_COLUMN"))                   return COLUMN_CHOSEN;
            case ROW_CHOSEN:
            case COLUMN_CHOSEN:
                return MARKET_UPDATED;
            case MARKET_UPDATED:
                return LEADER_ACTION_ASKED;
        }
        return UNKNOWN;
    }
}
