package it.polimi.ingsw.model;

public class DevelopmentCardGrid {

    private DevelopmentCardGridCell[][] cardGrid;

    public DevelopmentCardGrid() {
        cardGrid = new DevelopmentCardGridCell[4][3];
    }

    /**
     *
     * @return Actual card grid
     */
    public DevelopmentCardGridCell[][] getCardGrid() {
        return cardGrid;
    }

    /**
     *
     * @param row
     * @param column
     * Remove a card from the selected pile of the grid
     */
    public void removeCard(int row, int column){
        cardGrid[row][column].removeCard();
    }
}
