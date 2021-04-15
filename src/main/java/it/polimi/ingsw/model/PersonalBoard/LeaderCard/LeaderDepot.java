package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class LeaderDepot extends LeaderCard {
    private Depot depot;

    public LeaderDepot(int victoryPoints, Map<CardType, Integer> requirements, int capacity, ResType resource){
        super(victoryPoints,requirements);
        depot=new Depot(capacity);
        depot.setDepotResource(resource);
    }
    public void effectOnDraw(PersonalBoard personalBoard){}
    public void effectOnMarketBuy(PersonalBoard personalBoard){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){}
}