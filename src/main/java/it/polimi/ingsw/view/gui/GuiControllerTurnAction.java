package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.PrintWriter;

public class GuiControllerTurnAction {

    private PrintWriter out;
    @FXML
    private Button toBuyDevCard;
    @FXML
    private Button toAcquireResources;
    @FXML
    private Button toActivateProductions;

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void buyDevCard(){
        System.out.println("Player chose to buy a development card");
        toBuyDevCard.setText("Opening Card Grid..");
        out.println(new Message(MessageType.BUY_DEV_CARD, null));
    }

    public void acquireResources(){
        System.out.println("Player chose to visit market");
        toAcquireResources.setText("Opening Market..");
        out.println(new Message(MessageType.SHOW_MARKET, null));
    }

    public void activateProductions(){
        System.out.println("Player chose to activate productions");
        toActivateProductions.setText("Showing Cards..");
        out.println(new Message(MessageType.ACTIVATE_PRODUCTIONS, null));
    }
}
