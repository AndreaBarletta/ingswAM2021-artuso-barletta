package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.Production;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class LeaderProduction extends LeaderCard{
    private Production production;

    public LeaderProduction(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements, Map<ResType, Integer> resourceRequirements, Production production){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.production=production;
    }

    public void effectOnDraw(PersonalBoard personalBoard){}
    public void effectOnMarketBuy(PersonalBoard personalBoard){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){}
}
