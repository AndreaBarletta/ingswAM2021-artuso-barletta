package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLoadFromFile(){
        Game game=new Game("Test");
        assertTrue(game.loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json"));
        assertTrue(game.loadPopeFavourCardsFromFile("src/main/resources/popeFavourCards.json"));
        assertTrue(game.loadLeaderCardsFromFile("src/main/resources/leaderCards.json"));

        assertEquals(game.getDevelopmentCardGrid().size(),48);
    }

    @Test
    public void testAddPlayer(){
        Game game=new Game("Test");
        Controller controller=new Controller();
        controller.addEventListener(game);
        game.addEventListener(controller);

        assertTrue(game.addPlayer("player 1"));
        assertTrue(game.addPlayer("player 2"));

        assertEquals(game.getNumberOfPlayer(),2);

        assertTrue(game.addPlayer("player 1"));
        assertTrue(game.addPlayer("player 2"));
        assertFalse(game.addPlayer("player 1"));
        assertFalse(game.addPlayer("player 2"));
    }

    @Test
    public void testGiveLeaderCards(){
        Game game=new Game("Test");
        Controller controller=new Controller();
        controller.addEventListener(game);
        game.addEventListener(controller);

        game.addPlayer("player 1");
        game.addPlayer("player 2");

        game.loadLeaderCardsFromFile("src/main/resources/leaderCards.json");

        game.showLeaderCard();


    }
}
