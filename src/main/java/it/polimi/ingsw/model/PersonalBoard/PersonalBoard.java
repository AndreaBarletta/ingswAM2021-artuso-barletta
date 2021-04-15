package it.polimi.ingsw.model.PersonalBoard;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalBoard implements ControllerEventListener {
    private String playerName;
    private List<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private DevelopmentCardSlot[] developmentCardSlots;
    private Production baseProduction;
    private Strongbox strongbox;
    private Depot[] depots;
    private List<PersonalBoardEventListener> eventListeners;
    private boolean inkwell = false;


    public PersonalBoard(String playerNickname){
        this.playerName =playerNickname;
        eventListeners=new ArrayList<>();

        //Create components
        developmentCardSlots=new DevelopmentCardSlot[3];
        baseProduction=new Production();
        leaderCards=new ArrayList<>();
        //Create depots
        depots=new Depot[3];
        for(int i=0;i<depots.length;i++){
            depots[i]=new Depot(i+1);
        }
    }

    /**
     * Adds a new personal board event listener to the listener list
     * @param newEventListener new personal board event listener to be added to the listeners list
     */
    public void addEventListener(PersonalBoardEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * Loads the faith track from a json file
     * @param path Path of the json file containing the faith track information
     * @return Whether or not the faith track was loaded successfully
     */
    public boolean loadFaithTrackFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading faith track i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            faithTrack=gson.fromJson(content, FaithTrack.class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for faith track");
            return false;
        }

        return true;
    }

    /**
     * Gets the name of the player associated with this player board
     * @return Name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     *
     * @return Whether or not the production was successful
     */
    public boolean activateProduction(){
        return true;
    }

    /**
     * Checks if the given productions can be activated
     * @param productions Productions to activate (including base production, leader card productions and development cards)
     * @return Whether or not there are enough resources to activate the production
     */
    private boolean canProduce(Production[] productions){
        //Get chest and storage contents

        //Get the ingredients requires by the productions that have been selected
        Map<ResType,Integer> requirements=new HashMap<>();
        for(Production p:productions){
            p.getIngredients().forEach(
                    (key,value)->requirements.merge(key,value, Integer::sum)
            );
        }
        return true;
    }

    public void buyDevCard(DevelopmentCard card){

    }

    /**
     * Pick 2 leader cards among 4 given by the game
     * @param leaderCards 4 leader cards given by the game
     */
    public void chooseLeaderCards(LeaderCard[] leaderCards){
        for(PersonalBoardEventListener p:eventListeners){
            int[] indices=p.chooseLeaderCards(leaderCards,playerName);
            for(int i:indices){
                this.leaderCards.add(leaderCards[i]);
            }
        }
    }

    /**
     * Add resources acquired from the market to the storage
     * @param newResources Resources to be added
     */
    public void acquireResources(ResType[] newResources){

    }

    /**
     * Add resources to storage
     * @param newResources Resourced to be added
     */
    public void addResourcesToStorage(ResType[] newResources){
    }

    public boolean hasInkwell(){
        return inkwell;
    }

    public void receiveInkwell(){
        inkwell = true;
    }

}
