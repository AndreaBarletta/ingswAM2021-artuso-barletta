package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightPersonalBoard;

import java.util.Map;

public class LeaderDepot extends LeaderCard {
    private final Depot depot;

    public LeaderDepot(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements, int capacity, ResType resource){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        depot=new Depot(capacity);
        depot.setDepotResource(resource);
    }

    public void effectOnActivate(PersonalBoard personalBoard){
        personalBoard.addLeaderDepot(depot);
    }
    public void effectOnActivate(LightPersonalBoard lightPersonalBoard){ lightPersonalBoard.addLeaderDepot(depot);  }
    public void effectOnDiscard(PersonalBoard personalBoard){
        personalBoard.removeLeaderDepot(depot);
    }
    public void effectOnDiscard(LightPersonalBoard lightPersonalBoard){lightPersonalBoard.removeLeaderDepot(depot);}
    public String toString(){
        return super.toString()+"Type:Depot\nStores: "+depot.getDepotResources().getSymbol();
    }
}