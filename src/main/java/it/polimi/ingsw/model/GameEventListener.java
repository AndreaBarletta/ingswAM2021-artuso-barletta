package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public interface GameEventListener {
    void newPersonalBoard(PersonalBoard newPersonalBoard);
    void inkwellGiven(String playerName);
}
