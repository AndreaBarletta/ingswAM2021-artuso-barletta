package it.polimi.ingsw.PersonalBoardTest.FaithTrackTest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackTest {

    @Test
    public void testIncrementFaithTrack(){
        String content="";
        File file=new File(getClass().getClassLoader().getResource("faithTrack.json").getPath());
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            fail();
        }

        Gson gson=new Gson();
        FaithTrack faithTrack;
        try{
            faithTrack=gson.fromJson(content, FaithTrack.class);

            //Increment faithMarker of 5
            faithTrack.incrementFaithTrack(5);

            //Check the increment starting with faithMarker = 0
            assertEquals(faithTrack.getPosition(), 5);

            //Increment faithMarker of 3
            faithTrack.incrementFaithTrack(3);

            //Check the increment starting with faithMarker != 0
            assertEquals(faithTrack.getPosition(), 8);

            //Try to increment faithMarker more then the max (max = 25)
            faithTrack.incrementFaithTrack(30);

            //Check if increment is blocked at 25 (max of faithTrack)
            assertEquals(faithTrack.getPosition(), 25);

            //Check isAtEnd boolean
            assertTrue(faithTrack.isAtEnd());
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for faith track");
            fail();
        }
    }
}
