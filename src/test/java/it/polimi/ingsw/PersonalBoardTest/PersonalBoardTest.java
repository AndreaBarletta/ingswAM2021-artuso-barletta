package it.polimi.ingsw.PersonalBoardTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.DepotSpaceException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalBoardTest {
    @Test
    public void testLoadFaithTrackFromFile(){
        PersonalBoard personalBoard=new PersonalBoard("test");
        assertTrue(personalBoard.loadFaithTrackFromFile("/faithTrack.json"));
    }

    @Test
    public void testCanBuyDevCard(){
        PersonalBoard personalBoard=new PersonalBoard("test");
        DevelopmentCard[] developmentCards=loadDevelopmentCardsFromFile("/developmentCards.json");

        Map<ResType,Integer> resources=new HashMap<>();
        /*
        "level":2,
        "cost":{
          "SERVANT":3,
          "COIN":2
        },*/
        personalBoard.addResourcesToStrongbox(ResType.SERVANT,2);
        personalBoard.addResourcesToStrongbox(ResType.COIN,2);
        assertThrows(ResourcesException.class,()->personalBoard.canBuyDevCard(developmentCards[29],null));
        personalBoard.addResourcesToStrongbox(ResType.SERVANT,2);
        personalBoard.addResourcesToStrongbox(ResType.COIN,2);
        assertThrows(LevelException.class,()->personalBoard.canBuyDevCard(developmentCards[29],null));
        /*"id": 3,
                "level":1,
                "cost":{
            "SHIELD":3
        },*/
        personalBoard.addResourcesToStrongbox(ResType.SHIELD,2);
        assertThrows(ResourcesException.class,()->personalBoard.canBuyDevCard(developmentCards[3],null));
        personalBoard.addResourcesToStrongbox(ResType.SHIELD,2);
        assertDoesNotThrow(()->personalBoard.canBuyDevCard(developmentCards[3],null));
    }

    @Test
    public void testAddResourcesToDepot(){
        PersonalBoard pe=new PersonalBoard("test");
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

    @Test
    public void testPayResource(){
        PersonalBoard pe=new PersonalBoard("test");
        pe.addResourcesToStrongbox(ResType.STONE,10);
        pe.addResourcesToStrongbox(ResType.COIN,10);
        try{
            pe.addResourceToDepot(ResType.COIN);
            pe.addResourceToDepot(ResType.COIN);
            pe.addResourceToDepot(ResType.COIN);
            pe.addResourceToDepot(ResType.SERVANT);
            pe.addResourceToDepot(ResType.SERVANT);
        }catch(Exception e){}
        //Remove from strongbox
        pe.payResource(ResType.STONE,10,null);
        assertEquals(pe.getStrongboxContent().get(ResType.STONE),0);
        assertEquals(pe.getStrongboxContent().get(ResType.COIN),10);
        //Remove from depot
        pe.payResource(ResType.SERVANT,1,null);
        assertEquals(pe.getDepotsContent().get(ResType.SERVANT),1);
        //Check if depots get prioritized
        pe.payResource(ResType.COIN,10,null);
        assertEquals(pe.getDepotsContent().get(ResType.COIN),0);
        assertEquals(pe.getStrongboxContent().get(ResType.COIN),3);

    }

    @Test
    public void testCanAddToDepot(){
        PersonalBoard personalBoard=new PersonalBoard("test");
        Map<ResType,Integer> expectedContent=new HashMap<>();
        expectedContent.put(ResType.COIN,1);
        expectedContent.put(ResType.STONE,2);
        expectedContent.put(ResType.ANY,0);
        assertDoesNotThrow(()->personalBoard.addResourceToDepot(ResType.COIN));
        assertDoesNotThrow(()->personalBoard.addResourceToDepot(ResType.STONE));
        assertDoesNotThrow(()->personalBoard.addResourceToDepot(ResType.STONE));
        //Check function is correct
        assertTrue(personalBoard.canAddToDepot(new ResType[]{ResType.STONE}));
        //Check no resource gets added
        assertEquals(personalBoard.getDepotsContent(),expectedContent);
        //Check function is correct
        assertFalse(personalBoard.canAddToDepot(new ResType[]{ResType.STONE,ResType.COIN,ResType.SERVANT,ResType.STONE}));
        //Check no resource gets added
        assertEquals(personalBoard.getDepotsContent(),expectedContent);
    }
    private DevelopmentCard[] loadDevelopmentCardsFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading development cards i.e. wrong path");
            fail();
        }
        Reader reader=new InputStreamReader(inputStream);

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            DevelopmentCard[] developmentCardArray=gson.fromJson(reader, DevelopmentCard[].class);
            return developmentCardArray;
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for develompent cards");
            return null;
        }
    }
}
