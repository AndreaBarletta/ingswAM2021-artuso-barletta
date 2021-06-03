package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightPersonalBoard;

import java.util.AbstractMap;
import java.util.Map;

public class LeaderDevCardDiscounter extends LeaderCard {
    private Map<ResType,Integer> discounts;

    public LeaderDevCardDiscounter(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements, Map<ResType, Integer> resourceRequirements, Map<ResType, Integer> discounts){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.discounts=discounts;
    }

    public void effectOnActivate(PersonalBoard personalBoard) {personalBoard.addLeaderDiscount(discounts);}
    public void effectOnActivate(LightPersonalBoard lightPersonalBoard) {lightPersonalBoard.addLeaderDiscount(discounts);}
    public void effectOnDiscard(PersonalBoard personalBoard){personalBoard.removeLeaderDiscount(discounts);}
    public void effectOnDiscard(LightPersonalBoard lightPersonalBoard){lightPersonalBoard.removeLeaderDiscount(discounts);}
    public String toString(){
        return super.toString()+"Type:Discounter\nDiscounts: "+discounts;
    }
}
