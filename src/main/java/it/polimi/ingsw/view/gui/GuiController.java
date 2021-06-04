package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.PrintWriter;

public class GuiController {
    ObservableList<Integer> numberofplayersChoices = FXCollections.observableArrayList(2,3,4);

    private PrintWriter out;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private TextField playername=new TextField();
    @FXML
    private ChoiceBox<Integer> numberofplayers=new ChoiceBox<>();

    @FXML
    private void initialize() {
        numberofplayers.setItems(numberofplayersChoices);
    }

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    @FXML
    void connectPlayer(){
        System.out.println("Player Connected");
        b1.setText("Connecting");
        out.println(new Message(MessageType.PICK_PLAYERNAME,new String[]{playername.getText()}));
    }

    public void numberOfPlayers() {
        System.out.println("Number of players");
        b2.setText("Creating Game");
        out.println(new Message(MessageType.NUMBER_OF_PLAYERS,new String[]{numberofplayers.getValue().toString()}));
    }
}
