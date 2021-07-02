package it.polimi.ingsw.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LightModel {
    private String playerName;
    private LeaderCard[] leaderCardDeck;
    private DevelopmentCard[] developmentCardDeck;
    private int[][] developmentCardGrid;
    private LightMarket lightMarket;
    private final List<LightPersonalBoard> lightPersonalBoards;
    private FaithTrack lorenzoFaithTrack;

    public LightModel(){
        developmentCardGrid=new int[3][4];
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 4; col++){
                developmentCardGrid[row][col]=-1;
            }
        }
        lightPersonalBoards=new ArrayList<>();

        if(!loadLorenzoFaithTrackFromFile("/faithTrack.json")) {
            System.out.println("Error while loading lorenzo faithtrack");
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public LeaderCard[] getLeaderCardDeck() {
        return leaderCardDeck;
    }

    public DevelopmentCard[] getDevelopmentCardDeck() {
        return developmentCardDeck;
    }

    public int[][] getDevelopmentCardGrid() {
        return developmentCardGrid;
    }

    public void setCardInGrid(int level, CardType cardType,int newId) {
        developmentCardGrid[level-1][cardType.ordinal()]=newId;
    }

    public void setDevelopmentCardGrid(int[][] developmentCardGrid) {
        this.developmentCardGrid = developmentCardGrid;
    }

    public LightMarket getLightMarket() {
        return lightMarket;
    }

    public void setLightMarket(LightMarket lightMarket) {
        this.lightMarket = lightMarket;
    }

    public List<LightPersonalBoard> getLightPersonalBoards() {
        return lightPersonalBoards;
    }

    public boolean loadResources(){
        if((leaderCardDeck=loadLeaderCardDeckFromJson("/leaderCards.json"))==null)
            return false;
        return (developmentCardDeck = loadLDevelopmentCardDeckFromJson("/developmentCards.json")) != null;
    }

    /**
     * Loads the leader cards from a json file
     * @param fileName File name of the json file containing the list of leader cards
     * @return Whether or not the leader cards were loaded successfully
     */
    private LeaderCard[] loadLeaderCardDeckFromJson(String fileName){
        InputStream inputStream=getClass().getResourceAsStream(fileName);
        if(inputStream==null){
            System.out.println("Error reading from file while loading leader cards i.e. wrong path");
            return null;
        }
        Reader reader=new InputStreamReader(inputStream);

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            return gson.fromJson(reader, LeaderCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads the leader cards from a json file
     * @param fileName File name of the json file containing the list of the development cards
     * @return Whether or not the development cards were loaded successfully
     */
    private DevelopmentCard[] loadLDevelopmentCardDeckFromJson(String fileName){
        InputStream inputStream=getClass().getResourceAsStream(fileName);
        if(inputStream==null){
            System.out.println("Error reading from file while loading development cards i.e. wrong path");
            return null;
        }
        Reader reader=new InputStreamReader(inputStream);

        Gson gson=new Gson();
        try{
            return gson.fromJson(reader, DevelopmentCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            e.printStackTrace();
        }
        return null;
    }

    public boolean loadLorenzoFaithTrackFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading faith track i.e. wrong path");
            return false;
        }
        Reader reader=new InputStreamReader(inputStream);

        Gson gson=new Gson();
        try{
            lorenzoFaithTrack=gson.fromJson(reader, FaithTrack.class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for faith track");
            return false;
        }
        return true;
    }

    public void incrementLorenzoFaithTrack(int increment) {
        lorenzoFaithTrack.incrementFaithTrack(increment);
    }

    public FaithTrack getLorenzoFaithTrack() {
        return lorenzoFaithTrack;
    }
}
