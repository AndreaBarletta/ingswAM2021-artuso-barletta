package it.polimi.ingsw.PersonalBoardTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.CardTypeException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
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

        LeaderCard[] leaderCards=loadLeaderCardsFromFile(getClass().getClassLoader().getResource("leaderCards.json").getPath());
        DevelopmentCard[] developmentCards=loadDevelopmentCardsFromFile(getClass().getClassLoader().getResource("developmentCards.json").getPath());
        /*"resourceRequirements":{
            "SHIELD":5
        }*/
        resources.put(ResType.SHIELD,4);
        resources.put(ResType.STONE,10);
        assertThrows(ResourcesException.class,()->leaderCards[5].canActivate(resources,devCards));
        resources.put(ResType.SHIELD,5);
        assertDoesNotThrow(()->leaderCards[5].canActivate(resources,devCards));
        /*"cardRequirements":{
            "PURPLE":2,
            "GREEN":1
        }*/
        assertThrows(CardTypeException.class,()->leaderCards[3].canActivate(resources,devCards));
        devCards.add(developmentCards[13]);
        devCards.add(developmentCards[12]);
        devCards.add(developmentCards[3]);
        assertDoesNotThrow(()->leaderCards[3].canActivate(resources,devCards));
        /*"levelRequirements":{
            "PURPLE":2
        }*/
        assertThrows(LevelException.class,()->leaderCards[10].canActivate(resources,devCards));
        devCards.add(developmentCards[29]);
        assertDoesNotThrow(()->leaderCards[10].canActivate(resources,devCards));
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
