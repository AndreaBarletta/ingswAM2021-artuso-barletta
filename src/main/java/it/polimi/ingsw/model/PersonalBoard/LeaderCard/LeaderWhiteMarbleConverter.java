package it.polimi.ingsw.model.PersonalBoard.LeaderCard;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightPersonalBoard;

import java.util.Map;

public class LeaderWhiteMarbleConverter extends LeaderCard{
    private final ResType convertedResource;

    public LeaderWhiteMarbleConverter(int victoryPoints, Map<CardType, Integer> levelRequirements, Map<CardType, Integer> cardRequirements,Map<ResType, Integer> resourceRequirements, ResType convertedResource){
        super(victoryPoints,levelRequirements,cardRequirements,resourceRequirements);
        this.convertedResource=convertedResource;
    }

    public void effectOnActivate(PersonalBoard personalBoard) {personalBoard.addLeaderConvert(convertedResource);}
    public void effectOnActivate(LightPersonalBoard lightPersonalBoard) {lightPersonalBoard.addLeaderConvert(convertedResource);}
    public void effectOnDiscard(PersonalBoard personalBoard){personalBoard.removeLeaderConvert(convertedResource);}
    public void effectOnDiscard(LightPersonalBoard lightPersonalBoard){lightPersonalBoard.removeLeaderConvert(convertedResource);}

    public String toString(){
        return super.toString()+"Type:Converter\nConverts into: "+convertedResource.getSymbol();
    }
}
