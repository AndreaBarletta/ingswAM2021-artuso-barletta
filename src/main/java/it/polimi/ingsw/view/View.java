package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public interface View extends Runnable {
    LightModel lightModel=new LightModel();

    void setOutPrintWriter(PrintWriter out);
    void setPlayerName(String playerName);
    void newPlayerConnected(String newplayerName);
    void error(String errorMessage);
    void waitForOtherPlayers();
    void askForNumberOfPlayers();
    void gameCreated();
    void gameJoined(String[] playerNames);
    void disconnected();
    void gameStarted();
    void setDevCardGrid(int[][] devCardGridIds);
    void setMarket(LightMarket lightMarket);
    void showInitialLeaderCards(String[] leaderCardsIds);
    void leaderCardsChosen(String playerName,int[] ids);
    void inkwellGiven(String[] playerNamesOrdered);
    void askInitialResource();
    void initialResourcesChosen(String playerName,ResType resource);
    void incrementFaithTrack(String playerName, int increment);
    void waitYourTurn();
    void turnStart(String playerName);
    void askLeaderAction();
    void leaderActivate(String playerName, int leaderCardId);
    void leaderDiscard(String playerName,int leaderCardId);
    void leaderSkip(String playerName);
    void askTurnAction();
    void turnChoice(String playerName,String choice);
    //Productions
    void showProductions();
    void showChosenProductions(String playerName,int[] activatedProductions);
    void updateResources(String playerName, LightDepot[] depots, List<LightDepot> leaderDepots, LightStrongbox strongbox);
    //Development cards
    void showDevCardGrid();
    void updateDevCardGrid(int level, CardType cardType, int newCardId);
    void askDevCardSlot();
    void updateDevCardSlot(String playerName,int id,int slot);
    //Market
    void showMarket();
    void chooseRowOrColumn();
    void updateMarket(boolean row,int index);
    void askResourceDiscard(ResType[] resourcesAcquired);
    void askResourceConvert(int leftToConvert);
}
