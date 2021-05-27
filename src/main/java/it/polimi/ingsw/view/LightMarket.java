package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResType;

public class LightMarket {
    private ResType[][] marketTray;
    private ResType leftoverMarble;

    public LightMarket(ResType[][] marketTray,ResType leftoverMarble){
        this.marketTray=marketTray;
        this.leftoverMarble=leftoverMarble;
    }

    public void updateRow(int row) {
        ResType newLeftoverMarble = marketTray[row][0];
        System.arraycopy(marketTray[row], 1, marketTray[row], 0, 3);
        marketTray[row][3] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

    public void updateColumn(int column) {
        ResType newLeftoverMarble = marketTray[0][column];
        for (int i = 0; i < 2; i++) {
            marketTray[i][column] = marketTray[i + 1][column];
        }
        marketTray[2][column] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

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
