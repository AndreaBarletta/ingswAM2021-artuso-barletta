package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.LightModel;
import it.polimi.ingsw.view.LightPersonalBoard;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GuiControllerMyPersonalBoard {

    private int faithCounter = 0;
    @FXML
    private ImageView r1, r2, r3, r4, r5, r6;
    @FXML
    private ImageView rs1, rs2, rs3, rs4;
    @FXML
    private ImageView c1, c2, c3;
    @FXML
    private ImageView f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24;
    @FXML
    private ImageView p1, p2, p3;

    public void updateFaithTrack(String playername, LightModel lightModel){
        for(LightPersonalBoard lpb: lightModel.getLightPersonalBoards()){
            if(lpb.getPlayerName().equals(playername)){
                faithCounter = lpb.getFaithTrack().getPosition();
            }
        }


    }
}
