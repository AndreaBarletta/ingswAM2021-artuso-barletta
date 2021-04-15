package it.polimi.ingsw.PersonalBoardTest;

import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonalBoardTest {
    @Test
    public void testLoadFaithTrackFromFile(){
        PersonalBoard personalBoard=new PersonalBoard("test");
        assertTrue(personalBoard.loadFaithTrackFromFile("src/main/resources/faithTrack.json"));
    }
}
