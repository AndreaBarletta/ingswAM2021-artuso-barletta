package it.polimi.ingsw;

import it.polimi.ingsw.model.Market;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    @Test
    public void testConstructor(){
        Market market=new Market();
        assertNotNull(market.getMarketTray());
    }

    @Test
    public void testAcquires(){
        Market market=new Market();

        assertNotNull(market.acquireColumn(1));
        assertNotNull(market.acquireRow(1));
    }
}
