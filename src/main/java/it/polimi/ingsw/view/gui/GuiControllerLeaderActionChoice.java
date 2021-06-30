package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.view.LightModel;
import it.polimi.ingsw.view.LightPersonalBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.PrintWriter;

public class GuiControllerLeaderActionChoice {

    private int counter = 0;
    private String[] cardsIds = new String[2];
    private boolean[] selected = new boolean[2];
    private PrintWriter out;
    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private Button bActivate;
    @FXML
    private Button bDiscard;
    @FXML
    private Button bSkip;
    @FXML
    private Text t1;
    @FXML
    private Text t2;

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void printLeaderCards(LightModel lightModel){
        for(LightPersonalBoard lpb:lightModel.getLightPersonalBoards()){
            if(lpb.getPlayerName().equals(lightModel.getPlayerName())){
                if(lpb.getLeaderCards().size() == 2) {
                    cardsIds[0] = lpb.getLeaderCards().get(0).toString();
                    cardsIds[1] = lpb.getLeaderCards().get(1).toString();
                    String img1path = lightModel.getLeaderCardDeck()[lpb.getLeaderCards().get(0)].getCardFront();
                    String img2path = lightModel.getLeaderCardDeck()[lpb.getLeaderCards().get(1)].getCardFront();
                    Image lc1 = new Image(getClass().getClassLoader().getResource(img1path).toString());
                    Image lc2 = new Image(getClass().getClassLoader().getResource(img2path).toString());
                    iv1.setImage(lc1);
                    iv2.setImage(lc2);
                }
                if(lpb.getLeaderCards().size() == 1){
                    cardsIds[0] = lpb.getLeaderCards().get(0).toString();
                    String img1path = lightModel.getLeaderCardDeck()[lpb.getLeaderCards().get(0)].getCardFront();
                    Image lc1 = new Image(getClass().getClassLoader().getResource(img1path).toString());
                    iv1.setImage(lc1);
                }
            }
        }
    }

    public void selectCard1(){
        if(selected[0] == true) {
            selected[0] = false;
            counter--;
            t1.setText("");
        }
        else if(!selected[0] && counter == 0){
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
        else if(!selected[1] && counter == 0){
            selected[1] = true;
            counter++;
            t2.setText("Selected");
        }
    }

    public void activateCard(){
        for(int i=0; i<2; i++){
            if(selected[i])
                out.println(new Message(MessageType.LEADER_ACTION_ACTIVATE, new String[]{cardsIds[i]}));
        }
    }

    public void discardCard(){
        for(int i=0; i<2; i++){
            if(selected[i])
                out.println(new Message(MessageType.LEADER_ACTION_DISCARD, new String[]{cardsIds[i]}));
        }
    }

    public void skip(){
                out.println(new Message(MessageType.LEADER_ACTION_SKIP, null));
    }
}
