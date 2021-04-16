package it.polimi.ingsw;

import it.polimi.ingsw.model.Market;
import org.junit.jupiter.api.Test;

public class MarketTest {
    @Test
    public void testConstructor(){
        Market market=new Market();

        System.out.println(market);
    }
}
