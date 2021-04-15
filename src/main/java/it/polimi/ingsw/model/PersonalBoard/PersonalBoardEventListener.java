package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

public interface PersonalBoardEventListener {
    int[] chooseLeaderCards(LeaderCard[] leaderCards);
}
