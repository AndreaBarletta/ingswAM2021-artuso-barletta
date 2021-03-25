package it.polimi.ingsw.model;

import java.util.Stack;

public class DevelopmentCardGrid {

    private Stack<DevelopmentCard>[][] cardGrid;

    public DevelopmentCardGrid(Stack<DevelopmentCard>[][] cardGrid) {
        this.cardGrid = new Stack<DevelopmentCard>[4][3];
    }

    public Stack<DevelopmentCard>[][] getCardGrid() {
        return cardGrid;
    }
}
