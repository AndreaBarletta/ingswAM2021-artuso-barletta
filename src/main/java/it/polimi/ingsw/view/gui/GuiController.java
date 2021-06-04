package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GuiController {
    public Button b1;

    @FXML
    void connectPlayer(){
        System.out.println("Player Connected");
        b1.setText("Connecting");
    }
}
