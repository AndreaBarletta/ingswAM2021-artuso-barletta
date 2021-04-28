package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.DevelopmentCardSlot;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.exceptions.LevelException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardSlotTest {
    @Test
    public void testAddCardGetIngredients(){
        DevelopmentCardSlot devCardSlot = new DevelopmentCardSlot();

        // Create cost map
        HashMap<ResType, Integer> cost = new HashMap<>();
        cost.put(ResType.COIN, 1);

        Production production;

        //Create ingredients map
        HashMap<ResType, Integer> ingredients = new HashMap<>();
        ingredients.put(ResType.SERVANT, 1);

        //Create products map
        HashMap<ResType, Integer> products = new HashMap<>();
        products.put(ResType.SHIELD, 2);

        production = new Production(ingredients, products);

        //Create dev cards
        DevelopmentCard devCard1 = new DevelopmentCard(1, cost, 3, CardType.BLUE, production);
        DevelopmentCard devCard2 = new DevelopmentCard(2, cost, 3, CardType.BLUE, production);
        DevelopmentCard devCard3 = new DevelopmentCard(3, cost, 3, CardType.BLUE, production);

        //Error adding wrong level card to an empty slot
        assertThrows(LevelException.class,()->devCardSlot.addCard(devCard3));

        //Correct card's level add
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard1));

        //Error adding wrong level card (3) to a slot with 1 card
        assertThrows(LevelException.class,()->devCardSlot.addCard(devCard3));

        //Now adding the correct level 2 card
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard2));

        //Adding last card (level 3)
        assertDoesNotThrow(() -> devCardSlot.addCard(devCard3));


        int expectedNum = 3;

        //Checking the num of the card conteined in the slot
        assertEquals(devCardSlot.getCardsInSlot(), expectedNum);

        //Checking getIngredients function
        assertEquals(devCardSlot.getIngredients(), ingredients);

    }

    @Test
    public void testGetCards(){
        DevelopmentCardSlot devCardSlot = new DevelopmentCardSlot();

        //Create cost map
        HashMap<ResType, Integer> cost = new HashMap<>();
        cost.put(ResType.COIN, 1);

        Production production;

        //Create ingredients map
        HashMap<ResType, Integer> ingredients = new HashMap<>();
        ingredients.put(ResType.SERVANT, 1);

        //Crate products map
        HashMap<ResType, Integer> products = new HashMap<>();
        products.put(ResType.SHIELD, 2);

        production = new Production(products, ingredients);

        ArrayList<DevelopmentCard> devCards=new ArrayList<>();

        devCards.add(new DevelopmentCard(1, cost, 3, CardType.BLUE, production));
        devCards.add(new DevelopmentCard(2, cost, 3, CardType.BLUE, production));

        //Checking addCard function
        for(DevelopmentCard d:devCards) {
            assertDoesNotThrow(() -> devCardSlot.addCard(d));
        }

        //Checking getCard function
        assertEquals(devCardSlot.getCards(), devCards);
    }
}
