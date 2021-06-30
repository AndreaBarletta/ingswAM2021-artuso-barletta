package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.view.LightModel;
import it.polimi.ingsw.view.LightPersonalBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.PrintWriter;

public class GuiControllerLeaderActionChoice {

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

    }

    public void selectCard1(){
        if(selected[0] == true) {
            selected[0] = false;
            t1.setText("");
        }
        else{
            selected[0] = true;
            t1.setText("Selected");
        }
    }

    public void selectCard2(){
        if(selected[1] == true) {
            selected[1] = false;
            t2.setText("");
        }
        else{
            selected[1] = true;
            t2.setText("Selected");
        }
    }

    public void activateCard(){
        //out.println(new Message(MessageType.LEADER_ACTION_ACTIVATE, );
    }

    public void discardCard(){

    }

    public void skip(){

    }
}
