package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public enum ResType {
    COIN,STONE,SERVANT,SHIELD,FAITH,WHITEMARBLE,ANY;

    public void effectOnAcquire(PersonalBoard personalBoard){
        switch(this){
            case COIN:
                break;
            case STONE:
                break;
            case SERVANT:
                break;
            case SHIELD:
                break;
            case FAITH:
                break;
            case WHITEMARBLE:
                break;
        }
    }
}
