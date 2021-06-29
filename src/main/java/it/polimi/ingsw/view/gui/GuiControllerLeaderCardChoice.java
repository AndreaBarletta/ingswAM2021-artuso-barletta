package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.LightModel;
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

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void showLeaderCards(String[] leaderCardsIds, LightModel lightModel) {
        String img1path=lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[0])].getCardFront();
        String img2path=lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[1])].getCardFront();
        String img3path=lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[2])].getCardFront();
        String img4path=lightModel.getLeaderCardDeck()[Integer.parseInt(leaderCardsIds[3])].getCardFront();
        Image lc1 = new Image(getClass().getClassLoader().getResource(img1path).toString());
        Image lc2 = new Image(getClass().getClassLoader().getResource(img2path).toString());
        Image lc3 = new Image(getClass().getClassLoader().getResource(img3path).toString());
        Image lc4 = new Image(getClass().getClassLoader().getResource(img4path).toString());
        iv1.setImage(lc1);
        iv2.setImage(lc2);
        iv3.setImage(lc3);
        iv4.setImage(lc4);
    }
}
