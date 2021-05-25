package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.AbstractMap;
import java.util.Map;

public class LeaderDevCardDiscounter extends LeaderCard {
    private Map<ResType,Integer> discounts;

    public LeaderDevCardDiscounter(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements, Map<ResType, Integer> resourceRequirements, Map<ResType, Integer> discounts){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.discounts=discounts;
    }

    public void effectOnActivate(PersonalBoard personalBoard){
        isPlayed=true;
    }
    public void effectOnMarketBuy(PersonalBoard personalBoard,ResType[] newResources){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){
        discounts.forEach((k,v)->developmentCard.getCost().put(k,developmentCard.getCost().get(k)-v));
    }
    public void effectOnDiscard(PersonalBoard personalBoard){}
    public String toString(){
        return super.toString()+"\nType:Discounter\nDiscounts: "+discounts;
    }
}
