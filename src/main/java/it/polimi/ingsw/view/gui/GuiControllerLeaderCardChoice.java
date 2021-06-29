package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

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

    @FXML
    private void initialize(String[] leaderCardsIds) throws IOException {
        Image lc1 = new Image(getClass().getResource(gui.lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[0])].getCardFront()).toString());
        Image lc2 = new Image(getClass().getResource(gui.lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[1])].getCardFront()).toString());
        Image lc3 = new Image(getClass().getResource(gui.lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[2])].getCardFront()).toString());
        Image lc4 = new Image(getClass().getResource(gui.lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[3])].getCardFront()).toString());
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();
        ImageView iv3 = new ImageView();
        ImageView iv4 = new ImageView();
        iv1.setImage(lc1);
        iv2.setImage(lc2);
        iv3.setImage(lc3);
        iv4.setImage(lc4);
    }
}
