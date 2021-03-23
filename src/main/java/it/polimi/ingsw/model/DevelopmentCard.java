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

    public Production getProduction() {
        return production;
    }

    public Map<ResType, Integer> getIngredients(){
        return production.getIngredients();
    }
}
