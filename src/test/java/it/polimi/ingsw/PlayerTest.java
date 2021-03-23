package it.polimi.ingsw;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    @Test
    public void testNickname(){
        Player newPlayer=new Player("Test");
        assertEquals(newPlayer.getNickname(),"Test");
    }
}
