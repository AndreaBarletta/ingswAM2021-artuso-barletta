package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderDepot;
import it.polimi.ingsw.model.ResType;

import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args){
        Game game=new Game();

        if (game.loadLeaderCardsFromFile("src/main/resources/leaderCards.json")) {
            System.out.println("OK");
        } else {
            System.out.println("NO");
        }
    }
}
