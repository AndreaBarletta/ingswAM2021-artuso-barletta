package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GuiView extends Application implements View {
    private static PrintWriter out;
    private static Stage primaryStage;
    private static GuiView runningView;
    private static boolean isReady;
    private Scene mainScene;

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
        } catch (Exception e) {}
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
        } catch (Exception e) {}
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
    public void gameStarted() {
        System.out.println("Game has started");
    }

    @Override
    public void setDevCardGrid(int[][] devCardGridIds) {

    }

    @Override
    public void setMarket(LightMarket lightMarket) {

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
        } catch (Exception e) {}
    }

    @Override
    public void leaderCardsChosen(String playerName, int[] ids) {

    }

    @Override
    public void inkwellGiven(String[] playerNamesOrdered) {

    }

    @Override
    public void askInitialResource() {

    }

    @Override
    public void initialResourcesChosen(String playerName, ResType resource) {

    }

    @Override
    public void incrementFaithTrack(String playername, int increment) {

    }

    @Override
    public void waitYourTurn() {

    }

    @Override
    public void turnStart(String playerName) {

    }

    @Override
    public void askLeaderAction() {

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
    public void updateResources(String playerName, LightDepot[] depots, List<LightDepot> leaderDepots, LightStrongbox strongbox) {

    }

    @Override
    public void showDevCardGrid() {

    }

    @Override
    public void updateDevCardGrid(int level, CardType cardType, int newCardId) {

    }

    @Override
    public void askDevCardSlot() {

    }

    @Override
    public void updateDevCardSlot(String playerName, int id, int slot) {

    }

    @Override
    public void showMarket() {

    }

    @Override
    public void chooseRowOrColumn() {

    }

    @Override
    public void updateMarket(boolean row, int index) {

    }

    @Override
    public void askResourceDiscard(ResType[] resourcesAcquired) {

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
}