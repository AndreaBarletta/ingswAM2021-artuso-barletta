package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.ResType;

import java.util.Map;
import java.util.Stack;

public class DevelopmentCardSlot {
    //attributes
    private Stack<DevelopmentCard> devCardsSlot;

    //constructor
    public DevelopmentCardSlot(){
        devCardsSlot = new Stack<>();
    }

    //methods
    public int getCardsInSlot() {
        return devCardsSlot.size();     //devCardsSlot.capacity();
    }

    public void addCard(DevelopmentCard devCard) {
        devCardsSlot.push(devCard);
    }

    public Map<ResType, Integer> getIngredients() {
        return devCardsSlot.peek().getIngredients();
    }
}
