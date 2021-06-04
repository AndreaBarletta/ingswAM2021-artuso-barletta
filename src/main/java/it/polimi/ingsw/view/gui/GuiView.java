package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GuiView extends Application implements View {
    private static PrintWriter out;
    private static Stage primaryStage;

    public void setOutPrintWriter(PrintWriter out){
        GuiView.out =out;
    }

    @Override
    public void run(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Master of Renaissance - AM58");
        primaryStage.setResizable(false);
        GuiView.primaryStage=primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/Nickname.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root, 1024, 576);
        primaryStage.setScene(newScene);
        primaryStage.show();
        GuiController guiController= loader.getController();
        guiController.setOutPrintWriter(out);
    }

    @Override
    public void setPlayerName(String playerName) {
        lightModel.setPlayerName(playerName);
        lightModel.getLightPersonalBoards().add(new LightPersonalBoard(playerName));
    }

    @Override
    public void newPlayerConnected(String newPlayerName) {

    }

    @Override
    public void error(String errorMessage) {

    }

    @Override
    public void waitForOtherPlayers() {

    }

    @Override
    public void askForNumberOfPlayers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pages/NumberOfPlayers.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root, 1024, 576);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (Exception e) {}
    }

    @Override
    public void gameCreated() {

    }

    @Override
    public void gameJoined(String[] playerNames) {

    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void setDevCardGrid(int[][] devCardGridIds) {

    }

    @Override
    public void setMarket(LightMarket lightMarket) {

    }

    @Override
    public void showInitialLeaderCards(String[] leaderCardsIds) {

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
    public void disconnected() {

    }
}