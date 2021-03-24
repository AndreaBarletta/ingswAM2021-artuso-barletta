package it.polimi.ingsw.model;

import java.util.Map;

public abstract class LeaderCard {
    private boolean isPlayed;
    private final int victoryPoints;
    private Map<ResType, Integer> requirements;

    public LeaderCard(int victoryPoints, Map<ResType, Integer> requirements){
        this.victoryPoints=victoryPoints;
        isPlayed=false;
        this.requirements=requirements;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public abstract void activateAbility(PersonalBoard personalBoard);
}
