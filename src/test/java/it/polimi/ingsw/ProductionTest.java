package it.polimi.ingsw;

import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionTest {
    @Test
    public void testSetGet(){
        Production production=new Production();
        Map<ResType,Integer> ingredients=new HashMap<>();
        Map<ResType,Integer> product=new HashMap<>();

        ingredients.put(ResType.COIN,1);
        product.put(ResType.COIN,1);
        product.put(ResType.FAITH,1);

        production.setIngredients(ingredients);
        production.setProduct(product);

        assertEquals(production.getIngredients(),ingredients);
        assertEquals(production.getProduct(),product);
        assertEquals(production.getProduct().get(ResType.FAITH),1);
    }

}
