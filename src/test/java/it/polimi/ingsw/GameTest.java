package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLoadFromFile(){
        Game game=new Game();
        try {
            game.loadDevelopmentCardsFromFile("src/main/java/it/polimi/ingsw/resources/developmentCards.json");
        }catch(IOException e){
            System.out.println("Error while loading development cards from file");
        }

        assertEquals(game.getDevelopmentCardGrid().size(),48);
    }
}
