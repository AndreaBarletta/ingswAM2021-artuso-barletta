package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.PrintWriter;

public class GuiControllerNumberOfPlayers {
    ObservableList<Integer> numberofplayersChoices = FXCollections.observableArrayList(2,3,4);

    private PrintWriter out;
    @FXML
    private Button b;
    @FXML
    private ChoiceBox<Integer> numberofplayers=new ChoiceBox<>();
    @FXML
    private void initialize() {
        numberofplayers.setItems(numberofplayersChoices);
    }

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void numberOfPlayers() {
        System.out.println("Number of players: " + numberofplayers.getValue().toString());
        b.setText("Creating Game..");
        out.println(new Message(MessageType.NUMBER_OF_PLAYERS,new String[]{numberofplayers.getValue().toString()}));
    }
}
