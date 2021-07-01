package it.polimi.ingsw.model.Lorenzo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lorenzo {
    private final DevelopmentCardGrid developmentCardGrid;
    private FaithTrack faithTrack;
    private SoloActionTokens[] tokens;
    private int i; // signs which action Lorenzo has already done

    private final List<LorenzoEventListener> eventListeners;

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
     * Adds a new personal board event listener to the listener list
     * @param newEventListener new personal board event listener to be added to the listeners list
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

    public SoloActionTokens lorenzoAction(){
        SoloActionTokens action = tokens[i];
        i++;
        action.effectOnDraw(this);
        return action;
    }

    public void incrementFaithTrack(int faithPoint){
        faithTrack.incrementFaithTrack(faithPoint);
        for(LorenzoEventListener l:eventListeners)
            l.incrementLorenzoFaithTrack(faithPoint);
    }

    public void shuffle(){
        this.incrementFaithTrack(1);
        List<SoloActionTokens> tokenList = Arrays.asList(tokens);
        Collections.shuffle(tokenList);
        tokens = tokenList.toArray(SoloActionTokens[]::new);
        i=0;
    }

    public void removeBottomCard(CardType cardType) {
        if(developmentCardGrid.removeBottomCard(cardType))
            for(LorenzoEventListener l:eventListeners)
                l.lorenzoWon();
        if(developmentCardGrid.removeBottomCard(cardType))
            for(LorenzoEventListener l:eventListeners)
                l.lorenzoWon();
        for(LorenzoEventListener l:eventListeners)
            l.removeBottomCard(cardType.toString());
    }
}
