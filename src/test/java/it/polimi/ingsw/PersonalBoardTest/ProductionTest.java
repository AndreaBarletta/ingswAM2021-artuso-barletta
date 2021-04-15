package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.PersonalBoard.Production;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionTest {
    @Test
    public void testSetGet(){
        Map<ResType,Integer> ingredients=new HashMap<>();
        Map<ResType,Integer> products=new HashMap<>();

        ingredients.put(ResType.COIN,1);
        products.put(ResType.COIN,1);
        products.put(ResType.FAITH,1);

        Production production=new Production(ingredients,products);

        assertEquals(production.getIngredients(),ingredients);
        assertEquals(production.getProducts(),products);
        assertEquals(production.getProducts().get(ResType.FAITH),1);
    }

}
