package it.polimi.ingsw.model;

import java.util.Map;

public abstract class LeaderCard {
    private boolean isPlayed;
    private int victoryPoints;
    private Map<ResType, Integer> requirements;

    public LeaderCard(){}

    public LeaderCard(int victoryPoints, Map<ResType, Integer> requirements){
        this.victoryPoints=victoryPoints;
        isPlayed=false;
        this.requirements=requirements;
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
     * @return Whether or not the card has already been played
     */
    public boolean isPlayed() {
        return isPlayed;
    }

    public abstract void activateAbility(PersonalBoard personalBoard);
}