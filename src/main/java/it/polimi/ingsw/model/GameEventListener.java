package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public interface GameEventListener {
    void inkwellGiven(String playerName);
    void chooseLeaderCards(LeaderCard[] leaderCards, String playerName);
    void announceWinner(String playerName);
    void addInitialResource(String playerName, int playerNumber);
}
