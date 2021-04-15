package it.polimi.ingsw.PersonalBoardTest.FaithTrackTest;

import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackTest {
    @Test
    public void testLoadVaticanReportsFromFile(){
        FaithTrack faithTrack = new FaithTrack();
        String path = new String();

        assertTrue(faithTrack.loadVaticanReportsFromFile(path));
    }


}
