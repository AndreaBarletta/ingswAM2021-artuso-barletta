package it.polimi.ingsw.model;

public class DevelopmentCardGrid {
    private DevelopmentCardGridCell[][] cardGrid;

    public DevelopmentCardGrid() {
        cardGrid = new DevelopmentCardGridCell[3][4];
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 4; col++){
                cardGrid[row][col]=new DevelopmentCardGridCell();
            }
        }
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
     * @param level level of the card to remove
     * @param cardType card type of the card to remove
     * @return returns the card removed
     * Remove a card from the selected pile of the grid
     */
    public DevelopmentCard removeCard(int level, CardType cardType){
        return cardGrid[level-1][cardType.ordinal()].removeCard();
    }

    /**
     *
     * @param card
     * Adds a new card to the grid, placing it in the right spot based on level and card type
     */
    public void addCard(DevelopmentCard card){
        cardGrid[card.getLevel()-1][card.getCardType().ordinal()].addCard(card);
    }
}
