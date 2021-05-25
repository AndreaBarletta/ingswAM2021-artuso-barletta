package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LeaderCard {
    protected boolean isPlayed;
    protected int victoryPoints;
    protected Map<CardType, Integer> levelRequirements;
    protected Map<CardType, Integer> cardRequirements;
    protected Map<ResType, Integer> resourceRequirements;
    protected int id;

    public LeaderCard(int victoryPoints,Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements){
        this.victoryPoints=victoryPoints;
        isPlayed=false;
        this.levelRequirements=levelRequirements;
        this.cardRequirements=cardRequirements;
        this.resourceRequirements=resourceRequirements;
    }

    /**
     *
     * @return Victory points of the leader card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     *
     * @return The id of the card
     */
    public int getId(){
        return id;
    }
    /**
     *
     * @return Whether or not the card has already been played
     */
    public boolean isPlayed() {
        return isPlayed;
    }

    public abstract void effectOnActivate(PersonalBoard personalBoard);
    public abstract void effectOnMarketBuy(PersonalBoard personalBoard,ResType[] newResources);
    public abstract void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard);
    public abstract void effectOnDiscard(PersonalBoard personalBoard);
    public abstract String toString();

    /**
     * Checks if a leader card can be bought
     * @param resources Resources a player has
     * @param devCards Development cards a players has
     * @return Whether or not the card can be bought
     */
    public boolean canActivate(Map<ResType,Integer> resources, List<DevelopmentCard> devCards) {
        //Check resource requirements
        if(resourceRequirements!=null){
            for(Map.Entry<ResType,Integer> p:resourceRequirements.entrySet()){
                if(!resources.containsKey(p.getKey()))
                    return false;
                else if(resources.get(p.getKey())<p.getValue())
                    return false;
            }
        }

        //Get number of cards per type and maximum level per type
        Map<CardType,Integer> cardTypes=new HashMap<>();
        Map<CardType,Integer> cardMaxLevels=new HashMap<>();
        for(DevelopmentCard d:devCards){
            cardTypes.compute(d.getCardType(),(k,v)->v==null?d.getLevel():v+d.getLevel());
            cardMaxLevels.compute(d.getCardType(),(k,v)->d.getLevel());
        }

        //Check card requirements
        if(cardRequirements!=null){
            for(Map.Entry<CardType,Integer> p:cardRequirements.entrySet()){
                if(!cardTypes.containsKey(p.getKey()))
                    return false;
                else if(cardTypes.get(p.getKey())<p.getValue())
                    return false;
            }
        }

        //Check level requirements
        if(levelRequirements!=null){
            for(Map.Entry<CardType,Integer> p: levelRequirements.entrySet()){
                if(!cardMaxLevels.containsKey(p.getKey()))
                    return false;
                else if(cardMaxLevels.get(p.getKey())<p.getValue())
                    return false;
            }
        }

        return true;
    }
}
