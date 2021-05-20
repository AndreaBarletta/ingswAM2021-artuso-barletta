package it.polimi.ingsw.model.DevelopmentCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class DevelopmentCard {
    // Id of the card
    private int id;
    // Level of the card
    private int level;
    // Cost of the card
    private Map<ResType,Integer> cost;
    //Victory points given by the card
    private int victoryPoints;
    // Card type
    private CardType cardType;
    // Production of the card
    private Production production;

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

    /**
     * Gets the level of the card
     * @return Level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the resources requires to buy the card
     * @return Resources requires to buy the card
     */
    public Map<ResType, Integer> getCost() {
        return cost;
    }

    /**
     * Gets the victory points of the card
     * @return Victory points of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Gets the type of the card
     * @return Type of the card
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Gets the production of the card
     * @return The production of the card
     */
    public Production getProduction(){
        return production;
    }
}
