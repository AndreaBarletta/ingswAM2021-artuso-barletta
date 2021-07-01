package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.PrintWriter;

public class GuiControllerDiscardResourceChoice {

    private PrintWriter out;
    @FXML
    ImageView coin = new ImageView();
    @FXML
    ImageView servant = new ImageView();
    @FXML
    ImageView shield = new ImageView();
    @FXML
    ImageView stone = new ImageView();

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void printResources(){
        Image printCoin = new Image("gui_images/market/marbles/coin_marble.png");
        Image printServant = new Image("gui_images/market/marbles/servant_marble.png");
        Image printShield = new Image("gui_images/market/marbles/shield_marble.png");
        Image printStone = new Image("gui_images/market/marbles/stone_marble.png");
    }

    public void discardCoin(){
        out.println(new Message(MessageType.DISCARD_RESOURCE, new String[]{"COIN"}));
    }

    public void discardServant(){
        out.println(new Message(MessageType.DISCARD_RESOURCE, new String[]{"SERVANT"}));
    }

    public void discardShield(){
        out.println(new Message(MessageType.DISCARD_RESOURCE, new String[]{"SHIELD"}));
    }

    public void discardStone(){
        out.println(new Message(MessageType.DISCARD_RESOURCE, new String[]{"STONE"}));
    }
}
