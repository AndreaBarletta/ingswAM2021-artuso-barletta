package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.PrintWriter;

public class GuiControllerInitialResourceChoice {

    private PrintWriter out;
    @FXML
    private Button bCoin;
    @FXML
    private Button bStone;
    @FXML
    private Button bServant;
    @FXML
    private Button bShield;

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void chooseCoin(){
        System.out.println("Coin chosen");
        bCoin.setText("Acquiring..");
        out.println(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{"COIN"}));
    }

    public void chooseStone(){
        System.out.println("Stone chosen");
        bStone.setText("Acquiring..");
        out.println(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{"STONE"}));
    }

    public void chooseServant(){
        System.out.println("Servant chosen");
        bServant.setText("Acquiring..");
        out.println(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{"SERVANT"}));
    }

    public void chooseShield(){
        System.out.println("Shield chosen");
        bShield.setText("Acquiring..");
        out.println(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{"SHIELD"}));
    }
}
