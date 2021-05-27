package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResType;

public class LightMarket {
    private ResType[][] marketTray;
    private ResType leftoverMarble;

    public LightMarket(ResType[][] marketTray,ResType leftoverMarble){
        this.marketTray=marketTray;
        this.leftoverMarble=leftoverMarble;
    }

    public void updateRow(){}

    public void updateColumn(){}

    @Override
    public String toString(){
        String marketToString="Market:\n";
        marketToString += "Leftover Marble: " + leftoverMarble + "\n";
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                marketToString += marketTray[i][j] + " ";
            }
            marketToString += "\n";
        }
        return marketToString;
    }
}
