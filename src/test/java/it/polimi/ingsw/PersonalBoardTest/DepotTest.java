package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.exceptions.DepotResourceTypeException;
import it.polimi.ingsw.exceptions.DepotSpaceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DepotTest {
    @Test
    public void testAdd() {
        Depot depot = new Depot(2);
        depot.setDepotResource(ResType.COIN);

        assertDoesNotThrow(() -> depot.add(ResType.COIN, 1));
        assertThrows(DepotResourceTypeException.class,()->depot.add(ResType.SHIELD,1));
        assertThrows(DepotSpaceException.class,()->depot.add(ResType.COIN,20));
    }
}
