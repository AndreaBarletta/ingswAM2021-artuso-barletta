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
        assertEquals(ingredients,developmentCard.getIngredients());
    }
}
