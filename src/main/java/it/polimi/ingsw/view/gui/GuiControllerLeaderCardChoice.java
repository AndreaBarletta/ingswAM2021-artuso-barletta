package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.PrintWriter;

public class GuiControllerLeaderCardChoice {

    private PrintWriter out;
    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private ImageView iv3;
    @FXML
    private ImageView iv4;
    @FXML
    private GuiView gui = new GuiView();

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

   /* @FXML
    private void initialize(String[] leaderCardsIds) {
        Image lc1 = new Image(leaderCardsIds[0]+"_front.png");
        Image lc2 = new Image(leaderCardsIds[1]+"_front.png");
        Image lc3 = new Image(leaderCardsIds[2]+"_front.png");
        Image lc4 = new Image(leaderCardsIds[3]+"_front.png");
        iv1.setImage(lc1);
        iv2.setImage(lc2);
        iv3.setImage(lc3);
        iv4.setImage(lc4);
    }*/
}
