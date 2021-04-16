package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

public interface PersonalBoardEventListener {
    int[] chooseLeaderCards(LeaderCard[] leaderCards,String playerName);
    DevelopmentCard chooseDevelopmentCard(DevelopmentCardGrid cardGrid, String playerName);
    int chooseDevelopmentCardSlot(DevelopmentCardSlot[] developmentCardSlots,DevelopmentCard card, String playerName);
    void error(String error);
}
