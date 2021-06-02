package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.ResType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.net.URL;
public class GuiView extends Application implements View {
    @Override
    public synchronized void run(){
        launch();
    }

    @Override
    public synchronized void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("test.fxml"));
        Scene newScene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Test");
        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    @Override
    public synchronized void setPlayerName(String playerName) {

    }

    @Override
    public synchronized void newPlayerConnected(String newPlayerName) {

    }

    @Override
    public synchronized void error(String errorMessage) {

    }

    @Override
    public synchronized void waitForOtherPlayers() {

    }

    @Override
    public synchronized void askForNumberOfPlayers() {

    }

    @Override
    public synchronized void gameCreated() {

    }

    @Override
    public synchronized void gameJoined(String[] playerNames) {

    }

    @Override
    public synchronized void gameStarted() {

    }

    @Override
    public synchronized void setDevCardGrid(int[][] devCardGridIds) {

    }

    @Override
    public synchronized void setMarket(LightMarket lightMarket) {

    }

    @Override
    public synchronized void showInitialLeaderCards(String[] leaderCardsIds) {

    }

    @Override
    public synchronized void leaderCardsChosen(String playerName, int[] ids) {

    }

    @Override
    public synchronized void inkwellGiven(String[] playerNamesOrdered) {

    }

    @Override
    public synchronized void askInitialResource() {

    }

    @Override
    public synchronized void initialResourcesChosen(String playerName, ResType resource) {

    }

    @Override
    public synchronized void incrementFaithTrack(String playername, int increment) {

    }

    @Override
    public synchronized void waitYourTurn() {

    }

    @Override
    public synchronized void turnStart(String playerName) {

    }

    @Override
    public synchronized void askLeaderAction() {

    }

    @Override
    public synchronized void leaderActivate(String playerName, int leaderCardId) {

    }

    @Override
    public synchronized void leaderDiscard(String playerName, int leaderCardId) {

    }

    @Override
    public synchronized void leaderSkip(String playerName) {

    }

    @Override
    public synchronized void askTurnAction() {

    }

    @Override
    public synchronized void turnChoice(String playerName, String choice) {

    }

    @Override
    public synchronized void showProductions() {

    }

    @Override
    public synchronized void showChosenProductions(String playerName, int[] activatedProductions) {

    }

    @Override
    public synchronized void updateResources() {

    }

    @Override
    public synchronized void showDevCardGrid() {

    }

    @Override
    public synchronized void updateDevCardGrid(int level, CardType cardType, int newCardId) {

    }

    @Override
    public synchronized void askDevCardSlot() {

    }

    @Override
    public synchronized void updateDevCardSlot(String playerName, int id, int slot) {

    }

    @Override
    public synchronized void showMarket() {

    }

    @Override
    public synchronized void chooseRowOrColumn() {

    }

    @Override
    public synchronized void updateMarket(boolean row, int index) {

    }

    @Override
    public synchronized void disconnected() {

    }
}