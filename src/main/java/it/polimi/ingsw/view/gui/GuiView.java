package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiView extends Application implements View {
    private static PrintWriter out;
    private static Stage primaryStage;
    private static GuiView runningView;
    private static boolean isReady;
    private Scene mainScene;
    GuiControllerMyPersonalBoard guiControllerMyPersonalBoard = new GuiControllerMyPersonalBoard();
    GuiControllerMarket guiControllerMarket = new GuiControllerMarket();
    //GuiControllerDevelopmentCardsGrid guiControllerDevelopmentCardsGrid= new GuiControllerDevelopmentCardsGrid();

    public View getRunningView() {
        return runningView;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }



    public void setOutPrintWriter(PrintWriter out){
        GuiView.out =out;
    }

    @Override
    public void run(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        runningView=this;
        primaryStage.setTitle("Master of Renaissance - AM58");
        primaryStage.setResizable(false);
        GuiView.primaryStage=primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/Nickname.fxml"));
        Parent root = loader.load();
        mainScene = new Scene(root, 1024, 576);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        GuiControllerConnectPlayer guiControllerConnectPlayer = loader.getController();
        guiControllerConnectPlayer.setOutPrintWriter(out);
        isReady=true;
    }

    private LightPersonalBoard LBPByName(String playerName){
        for(LightPersonalBoard lpb:lightModel.getLightPersonalBoards()){
            if(lpb.getPlayerName().equals(playerName)){
                return lpb;
            }
        }
        return null;
    }

    @Override
    public void setPlayerName(String playerName) {
        lightModel.setPlayerName(playerName);
        lightModel.getLightPersonalBoards().add(new LightPersonalBoard(playerName));
    }

    @Override
    public void newPlayerConnected(String newPlayerName) {
        lightModel.getLightPersonalBoards().add(new LightPersonalBoard(newPlayerName));
    }

    @Override
    public void error(String errorMessage) {

    }

    @Override
    public void waitForOtherPlayers() {
        System.out.println("WAIT FOR OTHER PLAYERS");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/WaitingForOtherPlayers.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
        } catch (Exception e) {
            System.out.println("Exception while waiting for other players");
        }
    }

    @Override
    public void askForNumberOfPlayers() {
        System.out.println("ASK NUMBER OF PLAYERS");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/NumberOfPlayers.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerNumberOfPlayers guiControllerNumberOfPlayers = loader.getController();
            guiControllerNumberOfPlayers.setOutPrintWriter(out);
        } catch (Exception e) {
            System.out.println("Exception while asking number of players");
        }
    }

    @Override
    public void gameCreated() {

    }

    @Override
    public void gameJoined(String[] playerNames) {
        for(String s: playerNames){
            lightModel.getLightPersonalBoards().add(new LightPersonalBoard(s));
        }
    }

    @Override
    public void gameStarted(String gameType) {
        System.out.println("Game has started");
    }

    @Override
    public void setDevCardGrid(int[][] devCardGridIds) {
        lightModel.setDevelopmentCardGrid(devCardGridIds);
        //guiControllerDevelopmentCardsGrid.updateGrid(lightModel);
    }

    @Override
    public void setMarket(LightMarket lightMarket) {
        lightModel.setLightMarket(lightMarket);
        guiControllerMarket.update(lightModel);
    }

    @Override
    public void showInitialLeaderCards(String[] leaderCardsIds) {
        System.out.println("CHOOSE INITIAL LEADER CARDS");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/ChooseLeaderCards.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerLeaderCardChoice guiControllerLeaderCardChoice = loader.getController();
            guiControllerLeaderCardChoice.setOutPrintWriter(out);
            guiControllerLeaderCardChoice.showLeaderCards(leaderCardsIds,lightModel);
        } catch (Exception e) {
            System.out.println("Exception while showing initial leader cards");
        }
    }

    @Override
    public void leaderCardsChosen(String playerName, int[] ids) {
        LBPByName(playerName).setLeaderCards(Arrays.stream(ids).boxed().collect(Collectors.toList()));
    }

    @Override
    public void inkwellGiven(String[] playerNamesOrdered) {
        LBPByName(playerNamesOrdered[0]).setInkwell(true);
    }

    @Override
    public void askInitialResource() {
        System.out.println("CHOOSE INITIAL RESOURCE");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/ChooseInitialResource.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerInitialResourceChoice guiControllerInitialResourceChoice = loader.getController();
            guiControllerInitialResourceChoice.setOutPrintWriter(out);
        } catch (Exception e) {
            System.out.println("Exception while asking initial resource to acquire");
        }
    }

    @Override
    public void initialResourcesChosen(String playerName, ResType resource) {
        guiControllerMyPersonalBoard.setPersonalBoardResources();
        guiControllerMyPersonalBoard.updateResources(lightModel);
    }

    @Override
    public void incrementFaithTrack(String playername, int increment) {
        LBPByName(playername).getFaithTrack().incrementFaithTrack(increment);
        //guiControllerMyPersonalBoard.updateFaithTrack(playername, lightModel);
    }

    @Override
    public void waitYourTurn() {
        System.out.println("WAIT YOUR TURN");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/Board.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerBoard guiControllerBoard = loader.getController();
            guiControllerBoard.setOutPrintWriter(out);
        } catch (Exception e) {
            System.out.println("Exception while waiting for your turn to start");
        }
    }

    @Override
    public void turnStart(String playerName) {

    }

    @Override
    public void askLeaderAction() {
        System.out.println("CHOOSE A LEADER ACTION");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/ChooseLeaderAction.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerLeaderActionChoice guiControllerLeaderActionChoice = loader.getController();
            guiControllerLeaderActionChoice.setOutPrintWriter(out);
            guiControllerLeaderActionChoice.printLeaderCards(lightModel);
        } catch (Exception e) {
            System.out.println("Exception while asking leader action");
        }
    }

    @Override
    public void leaderActivate(String playerName, int leaderCardId) {

    }

    @Override
    public void leaderDiscard(String playerName, int leaderCardId) {

    }

    @Override
    public void leaderSkip(String playerName) {

    }

    @Override
    public void askTurnAction() {
        System.out.println("CHOOSE A TURN ACTION");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/TurnAction.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerTurnAction guiControllerTurnAction = loader.getController();
            guiControllerTurnAction.setOutPrintWriter(out);
        } catch (Exception e) {
            System.out.println("Exception while asking turn action");
        }
    }

    @Override
    public void turnChoice(String playerName, String choice) {

    }

    @Override
    public void showProductions() {

    }

    @Override
    public void showChosenProductions(String playerName, int[] activatedProductions) {

    }

    @Override
    public void askAnyResource() {

    }

    @Override
    public void updateResources(String playerName, LightDepot[] depots, List<LightDepot> leaderDepots, LightStrongbox strongbox) {
        LightPersonalBoard lpb=LBPByName(playerName);
        assert lpb != null;
        lpb.setDepots(depots);
        lpb.setLeaderDepots(leaderDepots);
        lpb.setStrongbox(strongbox);
        guiControllerMyPersonalBoard.setPersonalBoardResources();
        guiControllerMyPersonalBoard.updateResources(lightModel);
    }

    @Override
    public void showDevCardGrid() {

    }

    @Override
    public void updateDevCardGrid(int level, CardType cardType, int newCardId) {
        //guiControllerDevelopmentCardsGrid.updateGrid(lightModel);
    }

    @Override
    public void askDevCardSlot() {

    }

    @Override
    public void updateDevCardSlot(String playerName, int id, int slot) {

    }

    @Override
    public void showMarket() {
        System.out.println("CHOOSE RESOURCES");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/ChooseResourcesToAcquire.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerAcquireResourcesChoice guiControllerAcquireResourcesChoice = loader.getController();
            guiControllerAcquireResourcesChoice.setOutPrintWriter(out);
        } catch (Exception e) {
            System.out.println("Exception while visiting market");
        }
    }

    @Override
    public void updateMarket(boolean row, int index) {
        if(row){
            lightModel.getLightMarket().updateRow(index);
        }else{
            lightModel.getLightMarket().updateColumn(index);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/Market.fxml"));
        GuiControllerMarket guiControllerMarket = loader.getController();
        guiControllerMarket.update(lightModel);
    }

    @Override
    public void askResourceDiscard(ResType[] resourcesAcquired) {
        System.out.println("CHOOSE THE RESOURCE YOU WANT TO DISCARD");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/ChooseResourcesToDiscard.fxml"));
            Parent root = loader.load();
            mainScene.setRoot(root);
            GuiControllerDiscardResourceChoice guiControllerDiscardResourceChoice = loader.getController();
            guiControllerDiscardResourceChoice.setOutPrintWriter(out);
            guiControllerDiscardResourceChoice.printResources();
        } catch (Exception e) {
            System.out.println("Exception while discarding resource");
        }
    }

    @Override
    public void askResourceConvert(int leftToConvert) {

    }

    @Override
    public void resourceDiscarded(String playerName, ResType resource) {

    }

    @Override
    public void disconnected() {

    }

    @Override
    public void lastTurn() {

    }

    @Override
    public void announceWinner(String winnerPlayerName) {

    }

    @Override
    public void vaticanReportResults(Map<String, Boolean> results) {

    }

    @Override
    public void lorenzoDiscard(String cardType) {

    }

    @Override
    public void lorenzoIncrementFaithTrack(int increment) {

    }

    @Override
    public void lorenzoShuffle() {

    }

    @Override
    public void lorenzoWon() {

    }
}