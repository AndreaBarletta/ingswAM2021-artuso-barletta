package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiControllerMarket {

    @FXML
    private ImageView b11 = new ImageView();
    @FXML
    private ImageView b12 = new ImageView();
    @FXML
    private ImageView b13 = new ImageView();
    @FXML
    private ImageView b14 = new ImageView();
    @FXML
    private ImageView b21 = new ImageView();
    @FXML
    private ImageView b22 = new ImageView();
    @FXML
    private ImageView b23 = new ImageView();
    @FXML
    private ImageView b24 = new ImageView();
    @FXML
    private ImageView b31 = new ImageView();
    @FXML
    private ImageView b32 = new ImageView();
    @FXML
    private ImageView b33 = new ImageView();
    @FXML
    private ImageView b34 = new ImageView();
    @FXML
    private ImageView leftover = new ImageView();


    public void update(LightModel lightModel){
        Image murble11 = new Image(lightModel.getLightMarket().getMarketTray()[0][0].getMurblesPath());
        b11.setImage(murble11);
        Image murble12 = new Image(lightModel.getLightMarket().getMarketTray()[0][1].getMurblesPath());
        b12.setImage(murble12);
        Image murble13 = new Image(lightModel.getLightMarket().getMarketTray()[0][2].getMurblesPath());
        b13.setImage(murble13);
        Image murble14 = new Image(lightModel.getLightMarket().getMarketTray()[0][3].getMurblesPath());
        b14.setImage(murble14);
        Image murble21 = new Image(lightModel.getLightMarket().getMarketTray()[1][0].getMurblesPath());
        b21.setImage(murble21);
        Image murble22 = new Image(lightModel.getLightMarket().getMarketTray()[1][1].getMurblesPath());
        b22.setImage(murble22);
        Image murble23 = new Image(lightModel.getLightMarket().getMarketTray()[1][2].getMurblesPath());
        b23.setImage(murble23);
        Image murble24 = new Image(lightModel.getLightMarket().getMarketTray()[1][3].getMurblesPath());
        b24.setImage(murble24);
        Image murble31 = new Image(lightModel.getLightMarket().getMarketTray()[2][0].getMurblesPath());
        b31.setImage(murble31);
        Image murble32 = new Image(lightModel.getLightMarket().getMarketTray()[2][1].getMurblesPath());
        b32.setImage(murble32);
        Image murble33 = new Image(lightModel.getLightMarket().getMarketTray()[2][2].getMurblesPath());
        b33.setImage(murble33);
        Image murble34 = new Image(lightModel.getLightMarket().getMarketTray()[2][3].getMurblesPath());
        b34.setImage(murble34);
        Image leftoverMurble = new Image(lightModel.getLightMarket().getLeftoverMarble().getMurblesPath());
        leftover.setImage(leftoverMurble);
    }
}
