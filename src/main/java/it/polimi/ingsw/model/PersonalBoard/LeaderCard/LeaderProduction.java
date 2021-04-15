package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.Production;

public class LeaderProduction extends LeaderCard{
    private Production production;

    public void effectOnDraw(PersonalBoard personalBoard){}
    public void effectOnMarketBuy(PersonalBoard personalBoard){}
    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){}
}
