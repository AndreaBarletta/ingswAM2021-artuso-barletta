package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public enum ResType {
    COIN, STONE, SERVANT, SHIELD, FAITH, WHITEMARBLE, ANY;

    @Override
    public String toString() {
        switch (this) {
            case COIN:
                return "C";
            case STONE:
                return "S";
            case SERVANT:
                return "S";
            case SHIELD:
                return "S";
            case FAITH:
                return "F";
            case WHITEMARBLE:
                return "W";
        }
        return "a";
    }
}
