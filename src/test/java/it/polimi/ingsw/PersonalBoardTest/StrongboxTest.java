package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.PersonalBoard.Strongbox;
import it.polimi.ingsw.model.exceptions.StrongboxNegQuantityException;
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

        strongbox.add(expected);

        assertEquals(strongbox.getContent(),expected);

        HashMap<ResType,Integer> toRemove=new HashMap<>();

        //Correct usage
        toRemove.put(ResType.SHIELD,20);
        expected.put(ResType.SHIELD,0);
        assertDoesNotThrow(()->strongbox.remove(toRemove));
        assertEquals(strongbox.getContent(),expected);

        //Removal of too much of a given resource
        toRemove.put(ResType.COIN,20);
        assertThrows(StrongboxNegQuantityException.class,()->strongbox.remove(toRemove));

        //Check if changes reverted after failed remove
        assertEquals(strongbox.getContent(),expected);

        //Removal of a resource not present in the storage
        toRemove.put(ResType.STONE,20);
        assertThrows(StrongboxNegQuantityException.class,()->strongbox.remove(toRemove));

        //Check if changes reverted after failed remove
        assertEquals(strongbox.getContent(),expected);
    }
}
