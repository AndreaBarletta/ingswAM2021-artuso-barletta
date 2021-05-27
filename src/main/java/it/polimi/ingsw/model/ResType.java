package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotSpaceException;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
import it.polimi.ingsw.view.Colors;

public enum ResType {
    COIN, STONE, SERVANT, SHIELD, FAITH, WHITEMARBLE, ANY;

    @Override
    public String toString() {
        switch (this) {
            case COIN:
                return Colors.YELLOW.escape()+"C"+Colors.RESET.escape();
            case STONE:
                return Colors.GRAY.escape()+"S"+Colors.RESET.escape();
            case SERVANT:
                return Colors.MAGENTA.escape()+"S"+Colors.RESET.escape();
            case SHIELD:
                return Colors.CYAN.escape()+"S"+Colors.RESET.escape();
            case FAITH:
                return Colors.RED.escape()+"F"+Colors.RESET.escape();
            case WHITEMARBLE:
                return Colors.WHITE.escape()+"W"+Colors.RESET.escape();
            case ANY:
                return Colors.BRIGHT_GREEN.escape()+"?"+Colors.RESET.escape();
            default:
                return "";
        }
    }

    public void effectOnAcquire(PersonalBoard personalboard) throws DepotSpaceException {
        switch(this){
            case COIN:
            case STONE:
            case SERVANT:
            case SHIELD:
                personalboard.addResourceToDepot(this);
                break;
            case FAITH:
                personalboard.incrementFaithTrack(1);
                break;
            default:
                break;
        }
    }
}
