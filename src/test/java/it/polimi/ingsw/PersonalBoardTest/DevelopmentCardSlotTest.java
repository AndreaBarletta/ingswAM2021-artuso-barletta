package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.Production;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DevelopmentCardSlotTest {
    @Test
    public void testAddCardGetIngredients(){
        DevelopmentCardSlot devCardSlot = new DevelopmentCardSlot();

        HashMap<ResType, Integer> cost = new HashMap<>();
        cost.put(ResType.COIN, 1);

        Production production;
        HashMap<ResType, Integer> ingredients = new HashMap<>();
        ingredients.put(ResType.SERVANT, 1);
        HashMap<ResType, Integer> products = new HashMap<>();
        products.put(ResType.SHIELD, 2);
        production = new Production(products, ingredients);

        DevelopmentCard devCard = new DevelopmentCard(1, cost, 3, CardType.BLUE, production);

        devCardSlot.addCard(devCard);

        int expectedNum = 1;
        assertEquals(devCardSlot.getCardsInSlot(), expectedNum);

        HashMap<ResType, Integer> expectedProduction = new HashMap<>();
        expectedProduction.put(ResType.SERVANT, 1);
        expectedProduction.put(ResType.SHIELD, 2);
        assertEquals(devCardSlot.getIngredients(), expectedProduction);

    }
}
