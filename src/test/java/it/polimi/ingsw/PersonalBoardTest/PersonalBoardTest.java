package it.polimi.ingsw.PersonalBoardTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.DepotSpaceException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalBoardTest {
    @Test
    public void testLoadFaithTrackFromFile(){
        PersonalBoard personalBoard=new PersonalBoard("test",new DevelopmentCardGrid(),new Market());
        assertTrue(personalBoard.loadFaithTrackFromFile("src/main/resources/faithTrack.json"));
    }

    @Test
    public void testCanBuyDevCard(){
        PersonalBoard personalBoard=new PersonalBoard("test",new DevelopmentCardGrid(),new Market());
        DevelopmentCard[] developmentCards=loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json");

        Map<ResType,Integer> resources=new HashMap<>();
        /*
        "level":2,
        "cost":{
          "SERVANT":3,
          "COIN":2
        },*/
        resources.put(ResType.SERVANT,2);
        resources.put(ResType.COIN,2);
        personalBoard.addResourcesToStrongbox(resources);
        assertThrows(ResourcesException.class,()->personalBoard.canBuyDevCard(developmentCards[29]));
        personalBoard.addResourcesToStrongbox(resources);
        assertThrows(LevelException.class,()->personalBoard.canBuyDevCard(developmentCards[29]));
        /*"id": 3,
                "level":1,
                "cost":{
            "SHIELD":3
        },*/
        resources.put(ResType.SHIELD,2);
        personalBoard.addResourcesToStrongbox(resources);
        assertThrows(ResourcesException.class,()->personalBoard.canBuyDevCard(developmentCards[3]));
        personalBoard.addResourcesToStrongbox(resources);
        assertDoesNotThrow(()->personalBoard.canBuyDevCard(developmentCards[3]));
    }

    @Test
    public void testAddResourcesToDepot(){
        PersonalBoard pe=new PersonalBoard("test",new DevelopmentCardGrid(),new Market());
        //Without leader depots
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.COIN));
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.COIN));
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.COIN));
        assertThrows(DepotSpaceException.class,()->pe.addResourceToDepot(ResType.COIN));
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.STONE));
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.STONE));
        assertThrows(DepotSpaceException.class,()->pe.addResourceToDepot(ResType.STONE));
        //With leader depots
        Depot newLeaderDepot=new Depot(2);
        newLeaderDepot.setDepotResource(ResType.STONE);
        pe.addLeaderDepot(newLeaderDepot);
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.STONE));
        //Try moving resources to leader depots
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.SERVANT));
        assertDoesNotThrow(()->pe.addResourceToDepot(ResType.STONE));
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
