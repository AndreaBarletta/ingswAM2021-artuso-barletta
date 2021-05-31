package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.DuplicatedIdException;
import it.polimi.ingsw.exceptions.GameSizeExceeded;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLoadFromFile(){
        Game game=new Game(4);
        assertTrue(game.loadDevelopmentCardGridFromFile(getClass().getClassLoader().getResource("developmentCards/developmentCards.json").getPath()));
        assertTrue(game.loadPopeFavourCardsFromFile(getClass().getClassLoader().getResource("popeFavourCards.json").getPath()));
        assertTrue(game.loadLeaderCardsFromFile(getClass().getClassLoader().getResource("leaderCards/leaderCards.json").getPath()));

        assertEquals(game.getDevelopmentCardGrid().size(),48);
    }

    @Test
    public void testAddPlayer(){
        Game game=new Game(3);
        Controller controller=new Controller();
        controller.addEventListener(game);
        game.addEventListener(controller);

        //Test if players are added without errors (parsing of the faith track)
        assertDoesNotThrow(()->game.addPlayer("player 1"));
        assertDoesNotThrow(()->game.addPlayer("player 2"));
        assertEquals(game.getNumberOfPlayer(),2);

        //Test if players with the same name cannot be added
        assertThrows(DuplicatedIdException.class,()->game.addPlayer("player 1"));
        assertThrows(DuplicatedIdException.class,()->game.addPlayer("player 2"));

        //Test for the maximum number of players
        assertDoesNotThrow(()->game.addPlayer("player 3"));
        assertThrows(GameSizeExceeded.class,()->game.addPlayer("player4"));
    }

    @Test
    public void testGiveLeaderCards(){
        Game game=new Game(4);
        Controller controller=new Controller();
        controller.addEventListener(game);
        game.addEventListener(controller);

        try{
            game.addPlayer("player 1");
            game.addPlayer("player 2");
        }catch(Exception e){}


        game.loadLeaderCardsFromFile(getClass().getClassLoader().getResource("leaderCards/leaderCards.json").getPath());
    }
}
