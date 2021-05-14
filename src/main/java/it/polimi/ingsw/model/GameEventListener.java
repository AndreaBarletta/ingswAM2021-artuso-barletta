package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public interface GameEventListener {
    void inkwellGiven(String playerName);
    void announceWinner(String playerName);
    void chooseOneInitialResource(String playerName);
    void chooseTwoInitialResource(String playerName);
}
