package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public interface PersonalBoardEventListener {
    DevelopmentCard chooseDevelopmentCard(DevelopmentCardGrid cardGrid, String playerName);
    int chooseDevelopmentCardSlot(DevelopmentCardSlot[] developmentCardSlots,DevelopmentCard card, String playerName);
    void error(String error);
    boolean askForLeaderAction(String playerName);
    GameState askForTurnAction(String playerName);
    void showMarket(Market market,String playerName);
    AbstractMap.SimpleEntry<Boolean,Integer> askMarketRowColumn(String playerName);
    void notEnoughDepotSpace(ResType[] resources,String playerName);
    List<Production> chooseProductions(List<Production> productions, String playerName);
}
