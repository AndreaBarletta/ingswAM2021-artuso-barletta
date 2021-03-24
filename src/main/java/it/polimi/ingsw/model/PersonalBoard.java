package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class PersonalBoard {
    private LeaderCard[] leaderCards;
    //private FaithTrack faithTrack;
    //private DevelopmentCardSlot[] developmentCardSlots;
    private Production baseProduction;
    private Chest chest;
    private Storage[] storages;

    public PersonalBoard(){
        leaderCards=new LeaderCard[2];
        //developmentCardSlots=new DevelopmentCardSlots[3];
        baseProduction=new Production();
        storages=new Storage[3];
    }

    /**
     *
     * @return Whether or not the production was successfull
     */
    public boolean activateProduction(){
        return true;
    }

    /**
     *
     * @param productions Prodoductions to activate (including base production, leader card productions and development cards)
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
}
