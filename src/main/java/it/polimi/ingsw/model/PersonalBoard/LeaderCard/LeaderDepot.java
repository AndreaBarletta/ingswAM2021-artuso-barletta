package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;

public class LeaderDepot extends LeaderCard {
    private Depot depot;

    public void effectOnDraw(PersonalBoard personalBoard){}
    public void effectOnMarketBuy(PersonalBoard personalBoard){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){}
}