package it.polimi.ingsw.model.Lorenzo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lorenzo {
    private final DevelopmentCardGrid developmentCardGrid;
    private FaithTrack faithTrack;
    private SoloActionTokens[] tokens;
    private int i; // signs which action Lorenzo has already done
    private boolean firstShuffle=true;

    private final List<LorenzoEventListener> eventListeners;

    /**
     * Creates Lorenzo
     * @param developmentCardGrid The development card grid being used
     */
    public Lorenzo(DevelopmentCardGrid developmentCardGrid){
        this.developmentCardGrid=developmentCardGrid;
        this.eventListeners = new ArrayList<>();

        tokens = new SoloActionTokens[6];
        tokens[0]=SoloActionTokens.DISCARDGREEN;
        tokens[1]=SoloActionTokens.DISCARDBLUE;
        tokens[2]=SoloActionTokens.DISCARDYELLOW;
        tokens[3]=SoloActionTokens.DISCARDPURPLE;
        tokens[4]=SoloActionTokens.MOVE;
        tokens[5]=SoloActionTokens.SHUFFLE;

        shuffle();
    }

    /**
     * Adds a new lorenzo board event listener to the listener list
     * @param newEventListener new lorenzo board event listener to be added to the listeners list
     */
    public void addEventListener(LorenzoEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * Loads the faith track from a json file
     * @param fileString Path of the json file containing the faith track information
     * @return Whether or not the faith track was loaded successfully
     */
    public boolean loadFaithTrackFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading faith track i.e. wrong path");
            return false;
        }
        Reader reader=new InputStreamReader(inputStream);

        Gson gson=new Gson();
        try{
            faithTrack=gson.fromJson(reader, FaithTrack.class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for faith track");
            return false;
        }
        return true;
    }

    /**
     * Plays a lorenzo action
     */
    public void lorenzoAction(){
        SoloActionTokens action = tokens[i];
        i++;
        action.effectOnDraw(this);
    }

    /**
     * Increment lorenzo's faith track
     * @param faithPoint number of slots to increment of
     */
    public void incrementFaithTrack(int faithPoint){
        faithTrack.incrementFaithTrack(faithPoint);
        for(LorenzoEventListener l:eventListeners)
            l.incrementLorenzoFaithTrack(faithPoint);
    }

    /**
     * Shuffle the solo action token array. Activated at the start of the game, and everytime the shuffle token gets picked
     */
    public void shuffle(){
        List<SoloActionTokens> tokenList = Arrays.asList(tokens);
        Collections.shuffle(tokenList);
        tokens = tokenList.toArray(SoloActionTokens[]::new);
        i=0;
        if(!firstShuffle){
            for(LorenzoEventListener l:eventListeners)
                l.lorenzoShuffle();
        }
        if(firstShuffle) firstShuffle=false;
    }

    /**
     * Remove two cards from the bottom the card grid
     * @param cardType Card type to remove
     */
    public void removeBottomCard(CardType cardType) {
        int removedLevels[]=new int[2];
        removedLevels[0]= developmentCardGrid.removeBottomCard(cardType);
        if(removedLevels[0]==-1)
            for(LorenzoEventListener l:eventListeners)
                l.lorenzoWon();

        removedLevels[1]= developmentCardGrid.removeBottomCard(cardType);
        if(removedLevels[1]==-1)
            for(LorenzoEventListener l:eventListeners)
                l.lorenzoWon();
        for(LorenzoEventListener l:eventListeners)
            l.removeBottomCard(cardType,removedLevels);
    }

    public int canSendVaticanReport(){
        return faithTrack.canSendVaticanReport();
    }

    /**
     * Send the k-th vatican report
     * @param k Index of the vatican report to send
     * @return Whether or not the report was activated
     */
    public boolean sendVaticanReport(int k) {
        return faithTrack.sendVaticanReport(k);
    }

    public void setVaticanReports(){
        faithTrack.setVaticanReports();
    }

    public boolean isAtEnd(){
        return faithTrack.isAtEnd();
    }
}
