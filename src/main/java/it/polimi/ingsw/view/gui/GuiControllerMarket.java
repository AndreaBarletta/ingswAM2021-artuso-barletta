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
    ImageView[][] marketTray = new ImageView[3][4];

    public void createMarketImageViews(){
        marketTray[0][0] = b11;
        marketTray[0][1] = b12;
        marketTray[0][2] = b13;
        marketTray[0][3] = b14;
        marketTray[1][0] = b21;
        marketTray[1][1] = b22;
        marketTray[1][2] = b23;
        marketTray[1][3] = b24;
        marketTray[2][0] = b31;
        marketTray[2][1] = b32;
        marketTray[2][2] = b33;
        marketTray[2][3] = b34;
    }

    public void update(LightModel lightModel){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                Image murble = new Image(lightModel.getLightMarket().getMarketTray()[i][j].getPath());
                marketTray[i][j].setImage(murble);
                }
            }
        Image leftoverMurble = new Image(lightModel.getLightMarket().getLeftoverMarble().getPath());
        leftover.setImage(leftoverMurble);
    }
}