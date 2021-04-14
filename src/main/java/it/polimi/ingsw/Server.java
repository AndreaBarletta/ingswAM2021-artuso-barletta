package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;

public class Server {
    public static void main(String[] args){
        Game game=new Game();

        if (game.loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json")) {
            System.out.println("OK");
        } else {
            System.out.println("NO");
        }
    }
}
