package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.PrintWriter;

public class GuiControllerConnectPlayer {

    private PrintWriter out;
    @FXML
    private Button b;
    @FXML
    private TextField playername=new TextField();

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    @FXML
    void connectPlayer() throws IOException {
        System.out.println("Player Connected");
        b.setText("Connecting..");
        out.println(new Message(MessageType.PICK_PLAYERNAME,new String[]{playername.getText()}));
    }
}
