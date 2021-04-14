package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLoadFromFile(){
        Game game=new Game();
        assertTrue(game.loadDevelopmentCardsFromFile("src/main/java/it/polimi/ingsw/resources/developmentCards.json"));
        assertTrue(game.loadPopeFavorCardsFromFile("src/main/java/it/polimi/ingsw/resources/developmentCards.json"));

        assertEquals(game.getDevelopmentCardGrid().size(),48);
    }
}
