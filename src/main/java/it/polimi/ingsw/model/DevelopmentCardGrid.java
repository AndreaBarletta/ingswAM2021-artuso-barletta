package it.polimi.ingsw.model;

import java.util.Stack;

public class DevelopmentCardGrid {

    private DevelopmentCardGridCell[][] cardGrid;

    public DevelopmentCardGrid() {
        cardGrid = new DevelopmentCardGridCell[3][4];
    }

    public DevelopmentCardGridCell[][] getCardGrid() {
        return cardGrid;
    }
}
