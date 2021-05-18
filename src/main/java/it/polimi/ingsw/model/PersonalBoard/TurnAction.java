package it.polimi.ingsw.model.PersonalBoard;

public enum TurnAction {
    LEADER_ACTION_BEGIN,CHOOSE_TURN_ACTION,GET_RESOURCES,ACTIVATE_PRODUCTION,BUY_DEV_CARD,LEADER_ACTION_END,WAIT_FOR_OTHERS,UNKNOWN;

        TurnAction next(String s) {
            switch (this) {

                case LEADER_ACTION_BEGIN:
                    return CHOOSE_TURN_ACTION;

                case CHOOSE_TURN_ACTION:
                    switch (s) {
                        case "market":
                            return GET_RESOURCES;

                        case "production":
                            return ACTIVATE_PRODUCTION;

                        case "buy dev card":
                            return BUY_DEV_CARD;
                    }

                    return UNKNOWN;

                case GET_RESOURCES:
                    return LEADER_ACTION_END;

                case ACTIVATE_PRODUCTION:
                    return LEADER_ACTION_END;

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
