package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.View;
import javafx.application.Application;
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
