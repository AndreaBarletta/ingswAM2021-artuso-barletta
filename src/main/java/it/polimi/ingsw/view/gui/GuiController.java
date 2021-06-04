package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.PrintWriter;

public class GuiController {
    public Button b1;
    public Button b2;
    private PrintWriter out;
    @FXML
    private TextField playername=new TextField();
    private TextField numberofplayers=new TextField();

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
        out.println(new Message(MessageType.NUMBER_OF_PLAYERS,new String[]{numberofplayers.getText()}));
    }
}
