package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

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
}
