package it.polimi.ingsw.PersonalBoardTest.FaithTrackTest;

import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackTest {

    @Test
    public void testIncrementFaithTrack(){
        FaithTrack faithTrack = new FaithTrack();

        int faithPoint = 5;
        int faithTest = 5;

        //Increment faithMarker of 5
        faithTrack.incrementFaithTrack(faithPoint);

        //Check the increment starting with faithMarker = 0
        assertEquals(faithTrack.getPosition(), faithTest);

        faithPoint = 3;
        faithTest = 8;

        //Increment faithMarker of 3
        faithTrack.incrementFaithTrack(faithPoint);

        //Check the increment starting with faithMarker != 0
        assertEquals(faithTrack.getPosition(), faithTest);

        faithPoint = 30;
        faithTest = 24;

        //Try to increment faithMarker more then the max (max = 24)
        faithTrack.incrementFaithTrack(faithPoint);

        //Check if increment is blocked at 24 (max of faithTrack)
        assertEquals(faithTrack.getPosition(), faithTest);

        //Check isAtEnd boolean
        assertTrue(faithTrack.isAtEnd());

    }

}
