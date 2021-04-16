package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class LeaderDepot extends LeaderCard {
    private Depot depot;

    public LeaderDepot(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements, int capacity, ResType resource){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        depot=new Depot(capacity);
        depot.setDepotResource(resource);
    }

    public void effectOnActivate(PersonalBoard personalBoard){
        personalBoard.addLeaderDepot(depot);
        isPlayed=true;
    }
    public void effectOnMarketBuy(PersonalBoard personalBoard,ResType[] newResources){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){
        personalBoard.removeLeaderDepot(depot);
    }
}