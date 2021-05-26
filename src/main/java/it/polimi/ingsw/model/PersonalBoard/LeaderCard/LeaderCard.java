package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.exceptions.CardTypeException;
import it.polimi.ingsw.exceptions.LevelException;
import it.polimi.ingsw.exceptions.ResourcesException;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LeaderCard {
    protected boolean isActive;
    protected int victoryPoints;
    protected Map<CardType, Integer> levelRequirements;
    protected Map<CardType, Integer> cardRequirements;
    protected Map<ResType, Integer> resourceRequirements;
    protected int id;

    public LeaderCard(int victoryPoints,Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements){
        this.victoryPoints=victoryPoints;
        isActive=false;
        this.levelRequirements=levelRequirements;
        this.cardRequirements=cardRequirements;
        this.resourceRequirements=resourceRequirements;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getId(){
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate(){ isActive=true;
    }
    public abstract void effectOnActivate(PersonalBoard personalBoard);
    public abstract void effectOnMarketBuy(PersonalBoard personalBoard,ResType[] newResources);
    public abstract void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard);
    public abstract void effectOnDiscard(PersonalBoard personalBoard);

    /**
     * Checks if a leader card can be bought
     * @param resources Resources a player has
     * @param devCards Development cards a players has
     * @return Whether or not the card can be bought
     */
    public void canActivate(Map<ResType,Integer> resources, List<DevelopmentCard> devCards) throws CardTypeException, LevelException, ResourcesException {
        //Check resource requirements
        if(resourceRequirements!=null){
            for(Map.Entry<ResType,Integer> p:resourceRequirements.entrySet()){
                if(!resources.containsKey(p.getKey()))
                    throw new ResourcesException();
                else if(resources.get(p.getKey())<p.getValue())
                    throw new ResourcesException();
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
                    throw new CardTypeException();
                else if(cardTypes.get(p.getKey())<p.getValue())
                    throw new CardTypeException();
            }
        }

        //Check level requirements
        if(levelRequirements!=null){
            for(Map.Entry<CardType,Integer> p: levelRequirements.entrySet()){
                if(!cardMaxLevels.containsKey(p.getKey()))
                    throw new LevelException();
                else if(cardMaxLevels.get(p.getKey())<p.getValue())
                    throw new LevelException();
            }
        }
    }

    public String toString(){
        String leaderToString="Id: "+id+"\tVictory points: "+victoryPoints+"\n"+"Requirements: \n";
        if(cardRequirements!=null){
            leaderToString+="\tCard Requirements: ";
            for(Map.Entry<CardType,Integer> me:cardRequirements.entrySet()){
                leaderToString+=me.getKey()+"="+me.getValue()+" ";
            }
            leaderToString+="\n";
        }
        if(levelRequirements!=null){
            leaderToString+="\tLevel Requirements: ";
            for(Map.Entry<CardType,Integer> me:levelRequirements.entrySet()){
                leaderToString+=me.getKey()+"="+me.getValue()+" ";
            }
            leaderToString+="\n";
        }
        if(resourceRequirements!=null){
            leaderToString+="\tResource Requirements: ";
            for(Map.Entry<ResType,Integer> me:resourceRequirements.entrySet()){
                leaderToString+=me.getKey()+"="+me.getValue()+" ";
            }
            leaderToString+="\n";
        }
        return leaderToString;
    }
}
