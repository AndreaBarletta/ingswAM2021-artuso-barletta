package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.PrintWriter;

public class GuiControllerDevelopmentCardsGrid {

    private PrintWriter out;
    @FXML
    ImageView dc11 = new ImageView();
    @FXML
    ImageView dc12 = new ImageView();
    @FXML
    ImageView dc13 = new ImageView();
    @FXML
    ImageView dc14 = new ImageView();
    @FXML
    ImageView dc21 = new ImageView();
    @FXML
    ImageView dc22 = new ImageView();
    @FXML
    ImageView dc23 = new ImageView();
    @FXML
    ImageView dc24 = new ImageView();
    @FXML
    ImageView dc31 = new ImageView();
    @FXML
    ImageView dc32 = new ImageView();
    @FXML
    ImageView dc33 = new ImageView();
    @FXML
    ImageView dc34 = new ImageView();

    public void setOutPrintWriter(PrintWriter out) {
        this.out = out;
    }

    public void updateGrid(LightModel lightModel){
        String card11path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[0][0]].getCardFront();
        String card12path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[0][1]].getCardFront();
        String card13path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[0][2]].getCardFront();
        String card14path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[0][3]].getCardFront();
        String card21path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[1][0]].getCardFront();
        String card22path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[1][1]].getCardFront();
        String card23path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[1][2]].getCardFront();
        String card24path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[1][3]].getCardFront();
        String card31path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[2][0]].getCardFront();
        String card32path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[2][1]].getCardFront();
        String card33path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[2][2]].getCardFront();
        String card34path = lightModel.getDevelopmentCardDeck()[lightModel.getDevelopmentCardGrid()[2][3]].getCardFront();
        Image card11 = new Image(getClass().getClassLoader().getResource(card11path).toString());
        Image card12 = new Image(getClass().getClassLoader().getResource(card12path).toString());
        Image card13 = new Image(getClass().getClassLoader().getResource(card13path).toString());
        Image card14 = new Image(getClass().getClassLoader().getResource(card14path).toString());
        Image card21 = new Image(getClass().getClassLoader().getResource(card21path).toString());
        Image card22 = new Image(getClass().getClassLoader().getResource(card22path).toString());
        Image card23 = new Image(getClass().getClassLoader().getResource(card23path).toString());
        Image card24 = new Image(getClass().getClassLoader().getResource(card24path).toString());
        Image card31 = new Image(getClass().getClassLoader().getResource(card31path).toString());
        Image card32 = new Image(getClass().getClassLoader().getResource(card32path).toString());
        Image card33 = new Image(getClass().getClassLoader().getResource(card33path).toString());
        Image card34 = new Image(getClass().getClassLoader().getResource(card34path).toString());
        dc11.setImage(card11);
        dc12.setImage(card12);
        dc13.setImage(card13);
        dc14.setImage(card14);
        dc21.setImage(card21);
        dc22.setImage(card22);
        dc23.setImage(card23);
        dc24.setImage(card24);
        dc31.setImage(card31);
        dc32.setImage(card32);
        dc33.setImage(card33);
        dc34.setImage(card34);
    }
}
