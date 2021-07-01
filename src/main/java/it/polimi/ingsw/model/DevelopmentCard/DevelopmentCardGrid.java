package it.polimi.ingsw.model.DevelopmentCard;

import it.polimi.ingsw.model.CardType;

import java.util.*;

public class DevelopmentCardGrid {
    private final DevelopmentCardGridCell[][] cardGrid;

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
     * @param level level of the card to remove
     * @param cardType card type of the card to remove
     * @return returns the card removed
     * Remove a card from the selected pile of the grid
     */
    public void removeCard(int level, CardType cardType){
        cardGrid[level-1][cardType.ordinal()].removeCard();
    }

    /**
     * Gets the first card on top of the stack of a given level and type
     * @param level The level of the card
     * @param cardType The type of the card
     * @return The card on top of the stack
     */
    public DevelopmentCard getTopCard(int level, CardType cardType){
        return cardGrid[level-1][cardType.ordinal()].getTopCard();
    }

    /**
     * Adds a card to the right stack based on level and card type
     * @param card Card to be added
     */
    public void addCard(DevelopmentCard card){
        cardGrid[card.getLevel()-1][card.getCardType().ordinal()].addCard(card);
    }

    /**
     * Shuffle the cards, keeping them separated by level and card type
     */
    public void shuffle(){
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 4; col++){
                cardGrid[row][col].shuffle();
            }
        }
    }

    public int size(){
        int size=0;
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 4; col++){
                size+=cardGrid[row][col].size();
            }
        }
        return size;
    }

    public String[][] getTopCardsIds(){
        String[][] topCardsIds=new String[3][4];
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 4; j++) {
                DevelopmentCard topCard=cardGrid[i][j].getTopCard();
                topCardsIds[i][j] = topCard==null?String.valueOf(-1):String.valueOf(topCard.getId());
            }
        }

        return topCardsIds;
    }

    public boolean removeBottomCard(CardType cardType) {
        try {
            cardGrid[2][cardType.ordinal()].removeBottomCard();
        } catch (EmptyStackException e0){
            try {
                cardGrid[1][cardType.ordinal()].removeBottomCard();
            } catch (EmptyStackException e1){
                try {
                    cardGrid[0][cardType.ordinal()].removeBottomCard();
                } catch (EmptyStackException e2){
                    return true;
                }
            }
        }
        return false;
    }
}
