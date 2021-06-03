package it.polimi.ingsw.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LightModel {
    private String playerName;
    private LeaderCard[] leaderCardDeck;
    private DevelopmentCard[] developmentCardDeck;
    private int[][] developmentCardGrid;
    private LightMarket lightMarket;
    private List<LightPersonalBoard> lightPersonalBoards;

    public LightModel(){
        developmentCardGrid=new int[3][4];
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 4; col++){
                developmentCardGrid[row][col]=-1;
            }
        }
        lightPersonalBoards=new ArrayList<>();
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
        if((leaderCardDeck=loadLeaderCardDeckFromJson("leaderCards.json"))==null)
            return false;
        if((developmentCardDeck=loadLDevelopmentCardDeckFromJson("developmentCards.json"))==null)
            return false;
        return true;
    }

    /**
     * Loads the leader cards from a json file
     * @param fileName File name of the json file containing the list of leader cards
     * @return Whether or not the leader cards were loaded successfully
     */
    private LeaderCard[] loadLeaderCardDeckFromJson(String fileName){
        InputStream inputStream = null;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        try {
            line = buf.readLine();
        } catch (IOException e) {
            System.out.println("Error while loading leader cards");
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();

        while (line != null) {
            stringBuilder.append(line).append("\n");
            try {
                line = buf.readLine();
            } catch (IOException e) {
                System.out.println("Error while reading leader cards file");
                e.printStackTrace();
            }
        }
        String fileAsString = stringBuilder.toString();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            return gson.fromJson(fileAsString, LeaderCard[].class);
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
        InputStream inputStream = null;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        try {
            line = buf.readLine();
        } catch (IOException e) {
            System.out.println("Error while loading development cards");
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();

        while (line != null) {
            stringBuilder.append(line).append("\n");
            try {
                line = buf.readLine();
            } catch (IOException e) {
                System.out.println("Error while reading development cards file");
                e.printStackTrace();
            }
        }
        String fileAsString = stringBuilder.toString();
        Gson gson=new Gson();
        try{
            return gson.fromJson(fileAsString, DevelopmentCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            e.printStackTrace();
        }
        return null;
    }
}
