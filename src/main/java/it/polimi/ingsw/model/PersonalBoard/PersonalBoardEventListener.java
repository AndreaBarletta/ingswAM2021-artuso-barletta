package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Production;

public interface PersonalBoardEventListener {
    public void incrementFaithTrack(String playername, int increment);
}
