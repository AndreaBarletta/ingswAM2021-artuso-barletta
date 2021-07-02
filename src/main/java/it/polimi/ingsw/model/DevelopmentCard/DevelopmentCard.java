package it.polimi.ingsw.model.DevelopmentCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class DevelopmentCard {
    //Images for the GUI
    private String cardFront;
    private String cardBack;
    // Id of the card
    private int id;
    // Level of the card
    private final int level;
    // Cost of the card
    private final Map<ResType,Integer> cost;
    //Victory points given by the card
    private final int victoryPoints;
    // Card type
    private final CardType cardType;
    // Production of the card
    private final Production production;

    /**
     * Creates a new card with the given level, cost, victoryPoints, cardType and production
     * @param level Level of the card
     * @param cost Cost of the card
     * @param victoryPoints Victory points given by the card
     * @param cardType Card type
     * @param production Production of the card
     */
    public DevelopmentCard(int level, Map<ResType,Integer> cost, int victoryPoints, CardType cardType, Production production){
        this.level=level;
        this.cost=cost;
        this.victoryPoints=victoryPoints;
        this.cardType=cardType;
        this.production=production;
    }

    public int getLevel() {
        return level;
    }

    public Map<ResType, Integer> getCost() {
        return cost;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Production getProduction(){
        return production;
    }

    @Override
    public String toString(){
        String devCardToString="Id: "+id+"\tLevel: "+level+"\tVictory Points: "+victoryPoints+"\tCard Type: "+cardType+"\nCost:";
        for(Map.Entry<ResType,Integer> me:cost.entrySet()){
            devCardToString+=me.getKey()+"="+me.getValue()+" ";
        }
        devCardToString+="\n"+production;

        return devCardToString;
    }

    public int getId(){
        return id;
    }

    /**
     * Check if the development card an be bought
     * @param resources Resources available
     * @param discounts Additional discounts
     * @return Whether or not the card can be bought
     */
    public boolean canBeBought(Map<ResType,Integer> resources,Map<ResType,Integer> discounts){
        for(Map.Entry<ResType,Integer> me:cost.entrySet()){
            if(resources.containsKey(me.getKey())){
                if(resources.get(me.getKey())<me.getValue()-(discounts==null?0:discounts.getOrDefault(me.getKey(),0))){
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    public String getCardBack() {
        return cardBack;
    }

    public String getCardFront() {
        return cardFront;
    }
}
