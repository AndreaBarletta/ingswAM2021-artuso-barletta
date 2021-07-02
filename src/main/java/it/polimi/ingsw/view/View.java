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

    /**
     * Set the printerwriter (given by the socket to send messages to the server)
     * @param out
     */
    void setOutPrintWriter(PrintWriter out);

    /**
     * Set the player name
     * @param playerName Name of the player
     */
    void setPlayerName(String playerName);

    /**
     * A new player has joined the game
     * @param newplayerName Name of the new player
     */
    void newPlayerConnected(String newplayerName);

    /**
     * An error occurred
     * @param errorMessage Error info
     */
    void error(String errorMessage);

    /**
     * Wait for other players to join the game
     */
    void waitForOtherPlayers();

    /**
     * Ask for the number of players of the new game
     */
    void askForNumberOfPlayers();

    /**
     * Game has been created
     */
    void gameCreated();

    /**
     * Game has been joined
     * @param playerNames Name of the players already present in the game
     */
    void gameJoined(String[] playerNames);

    /**
     * Game has started
     * @param gameType Type of the game "single" for singleplayer, "multi" for multiplayer
     */
    void gameStarted(String gameType);

    /**
     * Set the development card grid ids
     * @param devCardGridIds Ids of the cards on top of the grid
     */
    void setDevCardGrid(int[][] devCardGridIds);

    /**
     * Create the market
     * @param lightMarket Market
     */
    void setMarket(LightMarket lightMarket);

    /**
     * Show the initial leader cards from which 2 must be chosen
     * @param leaderCardsIds Ids of the 4 leader cards
     */
    void showInitialLeaderCards(String[] leaderCardsIds);

    /**
     * A player has choosen their leader cards
     * @param playerName Name of the player
     * @param ids Ids of the leader cards chosen
     */
    void leaderCardsChosen(String playerName,int[] ids);

    /**
     * Inkwell was given
     * @param playerNamesOrdered Order of the players (first one is the one with the inkwell)
     */
    void inkwellGiven(String[] playerNamesOrdered);

    /**
     * Ask which initial resource the player wants to acquire
     */
    void askInitialResource();

    /**
     * A player has choosen their initial resources
     * @param playerName Name of the player
     * @param resource Resource acquired
     */
    void initialResourcesChosen(String playerName,ResType resource);

    /**
     * A player has advanced on their faith track
     * @param playerName Name of the player
     * @param increment Number of places the players has advanced
     */
    void incrementFaithTrack(String playerName, int increment);

    /**
     * Wait while the other player plays their turn
     */
    void waitYourTurn();

    /**
     * A player's turn has started
     * @param playerName Name of the player
     */
    void turnStart(String playerName);

    /**
     * Ask which leader action to play
     */
    void askLeaderAction();

    /**
     * A player has chosen to activate a leader card
     * @param playerName Name of the player
     * @param leaderCardId Id of the leader card activated
     */
    void leaderActivate(String playerName, int leaderCardId);

    /**
     * A player has chosen to discard a leader card
     * @param playerName Name of the player
     * @param leaderCardId Id of the leader card discarded
     */
    void leaderDiscard(String playerName,int leaderCardId);

    /**
     * A player has chosen to skip their leader action
     * @param playerName Name of the player
     */
    void leaderSkip(String playerName);

    /**
     * Ask which turn action to play
     */
    void askTurnAction();

    /**
     * A player has chosen their turn action
     * @param playerName Name of the player
     * @param choice Turn action chosen
     */
    void turnChoice(String playerName,String choice);

    /**
     * Show the productions available
     */
    void showProductions();

    /**
     * Show the chosen productions
     * @param playerName Name of the player
     * @param activatedProductions Chosen productions
     */
    void showChosenProductions(String playerName,int[] activatedProductions);

    /**
     * Ask a chooseable resource
     */
    void askAnyResource();

    /**
     * Update the resources
     * @param playerName Name of the player
     * @param depots New depots
     * @param leaderDepots New leader depots
     * @param strongbox New strongbox
     */
    void updateResources(String playerName, LightDepot[] depots, List<LightDepot> leaderDepots, LightStrongbox strongbox);

    /**
     * Show the development card grid
     */
    void showDevCardGrid();

    /**
     * Update the development card grid
     * @param level Level to update
     * @param cardType Card type to update
     * @param newCardId New card on top
     */
    void updateDevCardGrid(int level, CardType cardType, int newCardId);

    /**
     * Ask to choose a development card slot where to place the newly-bought development card
     */
    void askDevCardSlot();

    /**
     * A player has placed a new development card in their personal board
     * @param playerName Name of the player
     * @param id Id of the carc
     * @param slot Slot index
     */
    void updateDevCardSlot(String playerName,int id,int slot);

    /**
     * Show the market content
     */
    void showMarket();

    /**
     * Update the market
     * @param row A row has been updated if true, a column has been updated otherwise
     * @param index Index of the row / column
     */
    void updateMarket(boolean row,int index);

    /**
     * Ask the player to discard a resource
     * @param resourcesAcquired Resources acquired
     */
    void askResourceDiscard(ResType[] resourcesAcquired);

    /**
     * Ask a player to convert a resource
     * @param leftToConvert How many resources are left to convert
     */
    void askResourceConvert(int leftToConvert);

    /**
     * A player has discarded a resource
     * @param playerName Name of the player
     * @param resource Resource discarded
     */
    void resourceDiscarded(String playerName, ResType resource);

    /**
     * Get the running view, used since the GUI creats a different view object on start
     * @return The actual running view object
     */
    View getRunningView();

    /**
     * The view has loaded
     * @return Whether or not the view has loaded
     */
    boolean isReady();

    /**
     * The last turn is being played
     */
    void lastTurn();

    /**
     * Annoince the winner
     * @param winnerPlayerName Name of the winning player
     */
    void announceWinner(String winnerPlayerName);

    /**
     * Lorenzo has discarded 2 developement cards
     * @param cardType Card type discarded
     */
    void lorenzoDiscard(String cardType);

    /**
     * Lorenzo has advanced on his faith track
     * @param increment Number of places advanced
     */
    void lorenzoIncrementFaithTrack(int increment);

    /**
     * Lorenzo has shuffled it's token action deck
     */
    void lorenzoShuffle();

    /**
     * Lorenzo has won
     */
    void lorenzoWon();

    /**
     * Vatican reports were activated
     * @param results Key is the player name, value is whether or not the pope favour card was activated
     */
    void vaticanReportResults(Map<String,Boolean> results);
}
