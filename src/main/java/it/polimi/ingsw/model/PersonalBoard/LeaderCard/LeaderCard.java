package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

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

    public abstract void effectOnDraw(PersonalBoard personalBoard);
    public abstract void effectOnMarketBuy(PersonalBoard personalBoard);
    public abstract void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard);
    public abstract void effectOnDiscard(PersonalBoard personalBoard);
}
