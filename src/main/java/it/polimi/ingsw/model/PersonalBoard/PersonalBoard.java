package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PersonalBoardEventListener;
import it.polimi.ingsw.model.ResType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalBoard implements ControllerEventListener {
    private String playerNickname;
    private LeaderCard[] leaderCards;
    private FaithTrack faithTrack;
    private DevelopmentCardSlot[] developmentCardSlots;
    private Production baseProduction;
    private Strongbox strongbox;
    private Depot[] depots;
    private List<PersonalBoardEventListener> eventListeners;

    public PersonalBoard(){
        eventListeners=new ArrayList<>();
    }

    /**
     * Adds a new view event listener to the listener list
     * @param newEventListener new view event listener to be added to the listeners list
     */
    public void addEventListener(PersonalBoardEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    public PersonalBoard(String playerNickname){
        this.playerNickname=playerNickname;
        //developmentCardSlots=new DevelopmentCardSlots[3];
        baseProduction=new Production();
        depots =new Depot[3];
        //faithTrack=new FaithTrack();
    }

    /**
     *
     * @return Whether or not the production was successful
     */
    public boolean activateProduction(){
        return true;
    }

    /**
     *
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

    public void setLeaderCards(LeaderCard[] leaderCards){
        this.leaderCards=leaderCards;
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
}
