package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public abstract class LeaderCard {
    private boolean isPlayed;
    private int victoryPoints;
    private Map<CardType, Integer> levelRequirements;
    private Map<CardType, Integer> cardRequirements;
    private Map<ResType, Integer> resourceRequirements;

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
