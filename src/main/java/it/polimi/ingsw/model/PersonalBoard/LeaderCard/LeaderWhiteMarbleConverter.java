package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightPersonalBoard;

import java.util.Map;

public class LeaderWhiteMarbleConverter extends LeaderCard{
    private ResType convertedResource;

    public LeaderWhiteMarbleConverter(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements, ResType convertedResource){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.convertedResource=convertedResource;
    }

    public void effectOnActivate(PersonalBoard personalBoard) {}
    public void effectOnActivate(LightPersonalBoard lightPersonalBoard) {}

    public void effectOnMarketBuy(PersonalBoard personalBoard,ResType[] newResources){
        for(int i=0;i<newResources.length;i++)
            if(newResources[i]==ResType.WHITEMARBLE)
                newResources[i]=convertedResource;
    }

    public void effectOnDevCardBuy(PersonalBoard personalBoard, DevelopmentCard developmentCard){}
    public void effectOnDiscard(PersonalBoard personalBoard){}

    public String toString(){
        return super.toString()+"Type:Converter\nConverts into: "+convertedResource;
    }
}
