package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLoadFromFile(){
        Game game=new Game();
        assertTrue(game.loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json"));
        assertTrue(game.loadPopeFavourCardsFromFile("src/main/resources/popeFavourCards.json"));

        assertEquals(game.getDevelopmentCardGrid().size(),48);
    }
}
