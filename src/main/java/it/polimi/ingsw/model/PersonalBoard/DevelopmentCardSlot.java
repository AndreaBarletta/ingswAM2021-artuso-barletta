package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.exceptions.LevelException;

import java.util.List;
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

    public void addCard(DevelopmentCard devCard) throws LevelException {
        if(devCard.getLevel() == devCardsSlot.peek().getLevel() + 1){
            devCardsSlot.push(devCard);
        }else{
            throw new LevelException();
        }
    }

    public Map<ResType, Integer> getIngredients() {
        return devCardsSlot.peek().getIngredients();
    }

    public DevelopmentCard[] getCards(){
        DevelopmentCard[] cards=new DevelopmentCard[devCardsSlot.size()];
        devCardsSlot.toArray(cards);
        return cards;
    }
}
