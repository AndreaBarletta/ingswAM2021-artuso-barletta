package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightPersonalBoard;

import java.util.Map;

public class LeaderProduction extends LeaderCard{
    private final Production production;

    public LeaderProduction(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements, Map<ResType, Integer> resourceRequirements, Production production){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.production=production;
    }

    public void effectOnActivate(PersonalBoard personalBoard){ personalBoard.addLeaderProduction(production); }
    public void effectOnActivate(LightPersonalBoard lightPersonalBoard) { lightPersonalBoard.addLeaderProduction(production); }
    public void effectOnDiscard(PersonalBoard personalBoard){
        personalBoard.removeLeaderProduction(production);
    }
    public void effectOnDiscard(LightPersonalBoard lightPersonalBoard){
        lightPersonalBoard.removeLeaderProduction(production);
    }
    public String toString(){
        return super.toString()+"Type:Producer\n"+production;
    }
}
