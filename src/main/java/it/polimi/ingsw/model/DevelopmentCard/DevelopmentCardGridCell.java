package it.polimi.ingsw.model.DevelopmentCard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;

import java.util.Collections;
import java.util.Stack;

public class DevelopmentCardGridCell {
    private final Stack<DevelopmentCard> cardGridCell;

    public DevelopmentCardGridCell(){
        cardGridCell=new Stack<>();
    }

    /**
     * Adds a new card to the cell
     * @param newCard The new card to be added
     */
    public void addCard(DevelopmentCard newCard){
        cardGridCell.push(newCard);
    }

    /**
     * Removes a card from the stack
     * @return The card removed from the stack
     */
    public void removeCard(){
        cardGridCell.pop();
    }

    /**
     *
     * @return Whether or not the cell is empty
     */
    public boolean isEmpty(){
        return cardGridCell.empty();
    }

    public int size(){
        return cardGridCell.size();
    }

    /**
     * Gets the first card on top of the stack
     * @return The first card on top of the stack
     */
    public DevelopmentCard getTopCard(){
        return cardGridCell.peek();
    }

    /**
     * Shuffle the stack of cards
     */
    public void shuffle(){
        Collections.shuffle(cardGridCell);
    }
}
