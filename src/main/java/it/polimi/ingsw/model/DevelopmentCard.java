package it.polimi.ingsw.model;

import java.util.Map;

public class DevelopmentCard {
    private int level;
    private Map<ResType,Integer> cost;
    private int victoryPoints;
    private CardType cardType;
    private Production production;


    public DevelopmentCard(int level, Map<ResType,Integer> cost, int victoryPoints, CardType cardType, Production production){
        this.level=level;
        this.cost=cost;
        this.victoryPoints=victoryPoints;
        this.cardType=cardType;
        this.production=production;
    }

    /**
     *
     * @return Level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @return Resources requires to buy the development card
     */
    public Map<ResType, Integer> getCost() {
        return cost;
    }

    /**
     *
     * @return Victory points of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     *
     * @return Type of the card
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     *
     * @return Resources produced by the development card
     */
    public Map<ResType, Integer> produce() {
        return production.getProduct();
    }

    /**
     *
     * @return Resources required by the development card to activate production
     */
    public Map<ResType, Integer> getIngredients(){
        return production.getIngredients();
    }
}
