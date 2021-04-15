package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.PersonalBoard.Production;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.exceptions.LevelException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

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

        DevelopmentCard devCard1 = new DevelopmentCard(1, cost, 3, CardType.BLUE, production);
        DevelopmentCard devCard2 = new DevelopmentCard(2, cost, 3, CardType.BLUE, production);
        DevelopmentCard devCard3 = new DevelopmentCard(3, cost, 3, CardType.BLUE, production);

        assertThrows(LevelException.class,()->devCardSlot.addCard(devCard3));
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard1));
        assertThrows(LevelException.class,()->devCardSlot.addCard(devCard3));
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard2));
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard3));


        int expectedNum = 3;
        assertEquals(devCardSlot.getCardsInSlot(), expectedNum);

        assertEquals(devCardSlot.getIngredients(), ingredients);

    }

    public void testGetCards(){
        DevelopmentCardSlot devCardSlot = new DevelopmentCardSlot();
        DevelopmentCard[] devCards = new DevelopmentCard[2];
        for(DevelopmentCard d: devCards){
            assertDoesNotThrow(() -> devCardSlot.addCard(d));
        }

        assertEquals(devCardSlot.getCards(), devCards);
    }
}
