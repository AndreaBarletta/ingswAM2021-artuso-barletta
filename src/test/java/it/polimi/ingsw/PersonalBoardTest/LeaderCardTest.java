package it.polimi.ingsw.PersonalBoardTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardTest {
    @Test
    public void testCanActivate(){
        Map<ResType,Integer> resources=new HashMap<>();
        List<DevelopmentCard> devCards=new ArrayList<>();

        resources.put(ResType.SHIELD,4);

        LeaderCard[] leaderCards=loadLeaderCardsFromFile("src/main/resources/leaderCards.json");
        DevelopmentCard[] developmentCards=loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json");
        /*"resourceRequirements":{
            "SHIELD":5
        }*/
        resources.put(ResType.SHIELD,4);
        resources.put(ResType.STONE,10);
        assertThrows(leaderCards[5].canActivate(resources,devCards));
        resources.put(ResType.SHIELD,5);
        assertTrue(leaderCards[5].canActivate(resources,devCards));
        /*"cardRequirements":{
            "PURPLE":2,
            "GREEN":1
        }*/
        assertFalse(leaderCards[3].canActivate(resources,devCards));
        devCards.add(developmentCards[13]);
        devCards.add(developmentCards[12]);
        devCards.add(developmentCards[3]);
        assertTrue(leaderCards[3].canActivate(resources,devCards));
        /*"levelRequirements":{
            "PURPLE":2
        }*/
        assertFalse(leaderCards[10].canActivate(resources,devCards));
        devCards.add(developmentCards[29]);
        assertTrue(leaderCards[10].canActivate(resources,devCards));
    }

    private LeaderCard[] loadLeaderCardsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            return null;
        }

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            LeaderCard[] leaderCardsArray=gson.fromJson(content, LeaderCard[].class);
            return leaderCardsArray;
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            return null;
        }
    }
    private DevelopmentCard[] loadDevelopmentCardsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            return null;
        }

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            DevelopmentCard[] developmentCardArray=gson.fromJson(content, DevelopmentCard[].class);
            return developmentCardArray;
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for develompent cards");
            return null;
        }
    }
}
