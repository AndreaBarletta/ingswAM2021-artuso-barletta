package it.polimi.ingsw.DevelopmentCardTest;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCardTest {
    @Test
    public void testGetter(){
        Map<ResType,Integer> cost=new HashMap<>();
        cost.put(ResType.COIN,1);
        cost.put(ResType.SERVANT,2);

        Map<ResType,Integer> ingredients=new HashMap<>();
        ingredients.put(ResType.STONE,1);
        Map<ResType,Integer> products=new HashMap<>();
        products.put(ResType.SHIELD,3);
        products.put(ResType.FAITH,1);
        Production production=new Production(ingredients,products);

        DevelopmentCard developmentCard=new DevelopmentCard(1,cost,10, CardType.GREEN,production);

        assertEquals(cost,developmentCard.getCost());
        assertEquals(ingredients,developmentCard.getProduction().getIngredients());
    }

    @Test
    public void canBuy(){
        Map<ResType,Integer> cost=new HashMap<>();
        cost.put(ResType.COIN,1);
        cost.put(ResType.SERVANT,2);
        DevelopmentCard developmentCard=new DevelopmentCard(1,cost,10, CardType.GREEN,null);

        Map<ResType,Integer> resources=new HashMap<>();
        resources.put(ResType.COIN,1);
        resources.put(ResType.SERVANT,1);

        assertFalse(developmentCard.canBeBought(resources,null));
        resources.put(ResType.COIN,2);
        assertFalse(developmentCard.canBeBought(resources,null));
        resources.put(ResType.SERVANT,2);
        assertTrue(developmentCard.canBeBought(resources,null));

        //With discounts
        Map<ResType,Integer> discounts=new HashMap<>();
        resources.put(ResType.SERVANT,1);
        discounts.put(ResType.SERVANT,1);
        assertTrue(developmentCard.canBeBought(resources,discounts));
    }
}
