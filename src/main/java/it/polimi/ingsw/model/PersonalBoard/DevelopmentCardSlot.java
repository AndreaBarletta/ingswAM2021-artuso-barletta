package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.LevelException;

import java.util.*;

public class DevelopmentCardSlot {
    private final Stack<DevelopmentCard> devCardsSlot;

    public DevelopmentCardSlot(){
        devCardsSlot = new Stack<>();
    }

    public int getCardsInSlot() {
        return devCardsSlot.size();
    }

    /**
     * Add a card to the card slot
     * @param devCard The card to be added
     * @throws LevelException The slot contains a card that is not a level lower
     */
    public void canAddCard(DevelopmentCard devCard) throws LevelException {
        if (devCardsSlot.size() == 0) {
            if (devCard.getLevel() != 1)
                throw new LevelException();
        } else if (devCard.getLevel() != devCardsSlot.peek().getLevel() + 1) {
            throw new LevelException();
        }
    }

    /**
     * Add a card to the card slot
     * @param devCard Card to be added
     */
    public void addCard(DevelopmentCard devCard) {
        devCardsSlot.push(devCard);
    }

    /**
     * Gets the ingredients of the production of the top card
     * @return Ingredients of the production of the top card
     */
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
