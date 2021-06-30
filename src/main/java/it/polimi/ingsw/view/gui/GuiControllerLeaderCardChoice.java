package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GuiControllerLeaderCardChoice<event> {

    private int counter = 0;
    private String[] cardsIds = new String[4];
    private boolean[] selected = new boolean[4];
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
    private Text t1;
    @FXML
    private Text t2;
    @FXML
    private Text t3;
    @FXML
    private Text t4;

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void showLeaderCards(String[] leaderCardsIds, LightModel lightModel) {
        cardsIds[0] = leaderCardsIds[0];
        cardsIds[1] = leaderCardsIds[1];
        cardsIds[2] = leaderCardsIds[2];
        cardsIds[3] = leaderCardsIds[3];
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

    public void pickLeaderCards(){
        ArrayList leaderChosen = new ArrayList();
        for(int i=0; i<4; i++){
            if(selected[i])
                leaderChosen.add(cardsIds[i]);
        }
        out.println(new Message(MessageType.CHOOSE_LEADER_CARDS, new String[]{leaderChosen.get(0).toString(), leaderChosen.get(1).toString()}));
    }

    public void selectCard1(){
        if(selected[0] == true) {
            selected[0] = false;
            counter--;
            t1.setText("");
        }
        else if(!selected[0] && counter < 2){
            selected[0] = true;
            counter++;
            t1.setText("Selected");
        }
    }

    public void selectCard2(){
        if(selected[1] == true) {
            selected[1] = false;
            counter--;
            t2.setText("");
        }
        else if(!selected[1] && counter < 2){
            selected[1] = true;
            counter++;
            t2.setText("Selected");
        }
    }

    public void selectCard3(){
        if(selected[2] == true) {
            selected[2] = false;
            counter--;
            t3.setText("");
        }
        else if(!selected[2] && counter < 2){
            selected[2] = true;
            counter++;
            t3.setText("Selected");
        }
    }

    public void selectCard4(){
        if(selected[3] == true) {
            selected[3] = false;
            counter--;
            t4.setText("");
        }
        else if(!selected[3] && counter < 2){
            selected[3] = true;
            counter++;
            t4.setText("Selected");
        }
    }

}
