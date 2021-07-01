package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.LightModel;
import it.polimi.ingsw.view.LightPersonalBoard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiControllerMyPersonalBoard {

    private boolean resourcesSetted = false;
    private ImageView[] resources = new ImageView[10];
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

    /*public void updateFaithTrack(String playername, LightModel lightModel){
        for(LightPersonalBoard lpb: lightModel.getLightPersonalBoards()){
            if(lpb.getPlayerName().equals(playername)){
                faithCounter = lpb.getFaithTrack().getPosition();
            }
        }
    }*/

    public void setPersonalBoardResources(){
        if(!resourcesSetted) {
            resources[0] = r1;
            resources[1] = r2;
            resources[2] = r3;
            resources[3] = r4;
            resources[4] = r5;
            resources[5] = r6;
            resources[6] = rs1;
            resources[7] = rs2;
            resources[8] = rs3;
            resources[9] = rs4;
            resourcesSetted = true;
        }
    }

    public void updateResources(LightModel lightModel){
        for(LightPersonalBoard lpb: lightModel.getLightPersonalBoards()) {
            if(lpb.equals(lightModel.getPlayerName())) {
                Image res1depot1 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[0].getResource().getResourcesPath()).toString());
                resources[0].setImage(res1depot1);
                Image res1depot2 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[1].getResource().getResourcesPath()).toString());
                resources[1].setImage(res1depot2);
                resources[2].setImage(null);
                if(lpb.getDepots()[1].getCounter() == 2){
                    Image res2depot2 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[1].getResource().getResourcesPath()).toString());
                    resources[2].setImage(res2depot2);
                }
                Image res1depot3 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[2].getResource().getResourcesPath()).toString());
                resources[3].setImage(res1depot3);
                resources[4].setImage(null);
                resources[5].setImage(null);
                if(lpb.getDepots()[2].getCounter() == 2){
                    Image res2depot3 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[2].getResource().getResourcesPath()).toString());
                    resources[4].setImage(res2depot3);
                }
                if(lpb.getDepots()[2].getCounter() == 3){
                    Image res2depot3 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[2].getResource().getResourcesPath()).toString());
                    Image res3depot3 = new Image(getClass().getClassLoader().getResource(lpb.getDepots()[2].getResource().getResourcesPath()).toString());
                    resources[4].setImage(res2depot3);
                    resources[5].setImage(res3depot3);
                }
                Image strongboxCoin = new Image("gui_images/resources/coin.png");
                Image strongboxServant = new Image("gui_images/resources/servant.png");
                Image strongboxShield = new Image("gui_images/resources/shield.png");
                Image strongboxStone = new Image("gui_images/resources/stone.png");
                resources[6].setImage(strongboxCoin);
                resources[7].setImage(strongboxServant);
                resources[8].setImage(strongboxShield);
                resources[9].setImage(strongboxStone);
            }
        }
    }
}
