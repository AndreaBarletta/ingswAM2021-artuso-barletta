package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiControllerMarket {

    @FXML
    private ImageView b11, b12, b13, b14, b21, b22, b23, b24, b31, b32, b33, b34;
    @FXML
    private ImageView leftover;
    @FXML
    ImageView[][] market = new ImageView[3][4];

    public void createMarketImageViews(){
        market[0][0] = b11;
        market[0][1] = b12;
        market[0][2] = b13;
        market[0][3] = b14;
        market[1][0] = b21;
        market[1][1] = b22;
        market[1][2] = b23;
        market[1][3] = b24;
        market[2][0] = b31;
        market[2][1] = b32;
        market[2][2] = b33;
        market[2][3] = b34;
    }

    public void update(LightModel lightModel){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                Image murble = new Image(lightModel.getLightMarket().getMarketTray()[i][j].getPath());
                market[i][j].setImage(murble);
                }
            }
        Image leftoverMurble = new Image(lightModel.getLightMarket().getLeftoverMarble().getPath());
        leftover.setImage(leftoverMurble);
    }
}
