package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.PrintWriter;

public class GuiControllerAcquireResourcesChoice {

    private PrintWriter out;
    @FXML
    private Button r1, r2, r3, c1, c2, c3, c4;

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void row1(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"row 1"}));
    }

    public void row2(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"row 2"}));
    }

    public void row3(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"row 3"}));
    }

    public void column1(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"column 1"}));
    }

    public void column2(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"column 2"}));
    }

    public void column3(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"column 3"}));
    }

    public void column4(){
        out.println(new Message(MessageType.CHOOSE_ROW_OR_COLUMN, new String[]{"column 4"}));
    }
}
