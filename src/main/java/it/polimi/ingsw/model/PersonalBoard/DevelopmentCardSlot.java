package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.LevelException;

import java.util.*;

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
        if(devCardsSlot.size()==0){
            if(devCard.getLevel()==1){
                devCardsSlot.push(devCard);
            }else{
                throw new LevelException();
            }
        }else if(devCard.getLevel() == devCardsSlot.peek().getLevel() + 1){
            devCardsSlot.push(devCard);
        }else{
            throw new LevelException();
        }
    }

    public Map<ResType, Integer> getIngredients() {
        return devCardsSlot.peek().getProduction().getIngredients();
    }

    public List<DevelopmentCard> getCards(){
        return new ArrayList<>(devCardsSlot);
    }

    public DevelopmentCard getTopCard() {
        try {
            return devCardsSlot.peek();
        }catch(EmptyStackException e){
            return null;
        }
    }
}
