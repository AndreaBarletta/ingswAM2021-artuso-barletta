package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResType;

public class LightMarket {
    private final ResType[][] marketTray;
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
        StringBuilder marketToString= new StringBuilder("Market:\n");
        marketToString.append("Leftover Marble: ").append(leftoverMarble.getSymbol()).append("\n");
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                marketToString.append(marketTray[i][j].getSymbol()).append(" ");
            }
            marketToString.append("\n");
        }
        return marketToString.toString();
    }

    public ResType[][] getMarketTray(){ return marketTray; }

    public ResType getLeftoverMarble(){ return leftoverMarble; }

}
