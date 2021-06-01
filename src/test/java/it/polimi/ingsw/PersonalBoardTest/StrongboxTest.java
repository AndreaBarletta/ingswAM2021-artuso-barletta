package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.PersonalBoard.Strongbox;
import it.polimi.ingsw.exceptions.NegQuantityException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class StrongboxTest {
    @Test
    public void testAddRemove() {
        Strongbox strongbox=new Strongbox();

        HashMap<ResType,Integer> expected=new HashMap<>();
        expected.put(ResType.COIN,10);
        expected.put(ResType.SHIELD,20);

        strongbox.add(ResType.COIN,10);
        strongbox.add(ResType.SHIELD,20);

        assertEquals(strongbox.getContent(),expected);

        //Correct usage
        expected.put(ResType.SHIELD,0);
        assertDoesNotThrow(()->strongbox.remove(ResType.SHIELD,20));
        assertEquals(strongbox.getContent(),expected);

        //Removal of too much of a given resource
        assertThrows(NegQuantityException.class,()->strongbox.remove(ResType.COIN,20));

        //Check if changes reverted after failed remove
        assertEquals(strongbox.getContent(),expected);

        //Removal of a resource not present in the storage
        assertThrows(NegQuantityException.class,()->strongbox.remove(ResType.STONE,10));

        //Check if changes reverted after failed remove
        assertEquals(strongbox.getContent(),expected);
    }
}
