package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market {

    private ResType[][] marketTray;
    private ResType leftoverMarble;

    /**
     * Create market and shuffle the marbles
     */
    public Market() {
        marketTray = new ResType[3][4];

        List<ResType> marbles=new ArrayList<>();
        marbles.add(ResType.WHITEMARBLE);
        marbles.add(ResType.WHITEMARBLE);
        marbles.add(ResType.WHITEMARBLE);
        marbles.add(ResType.WHITEMARBLE);
        marbles.add(ResType.SHIELD);
        marbles.add(ResType.SHIELD);
        marbles.add(ResType.STONE);
        marbles.add(ResType.STONE);
        marbles.add(ResType.COIN);
        marbles.add(ResType.COIN);
        marbles.add(ResType.SERVANT);
        marbles.add(ResType.SERVANT);
        marbles.add(ResType.FAITH);
        Collections.shuffle(marbles);

        for(int i=0;i<3;i++){
            marbles.subList(i*4,(i+1)*4).toArray(marketTray[i]);
        }

        leftoverMarble = marbles.get(12);
    }

    /**
     *
     * @return Actual market tray
     */
    public ResType[][] getMarketTray() {
        return marketTray;
    }

    public ResType[] getRow(int row) {
        ResType[] rowToGet = new ResType[4];
        System.arraycopy(marketTray[row], 0, rowToGet, 0, 4);
        return rowToGet;
    }

    public ResType[] getColumn(int column) {
        ResType[] columnToGet = new ResType[3];
        System.arraycopy(marketTray[column], 0, columnToGet, 0, 3);
        return columnToGet;
    }

    /**
     *
     * @param row
     * Update market tray after buying a row
     */
    private void updateRow(int row) {
        ResType newLeftoverMarble = marketTray[row][0];
        System.arraycopy(marketTray[row], 1, marketTray[row], 0, 3);
        marketTray[row][3] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

    /**
     *
     * @param column
     * Update market tray after buying a column
     */
    private void updateColumn(int column) {
        ResType newLeftoverMarble = marketTray[0][column];
        for (int i = 0; i < 2; i++) {
            marketTray[i][column] = marketTray[i + 1][column];
        }
        marketTray[2][column] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

    /**
     *
     * @param row
     * @return Resources player is acquiring
     */
    public ResType[] acquireRow(int row){
        ResType[] acquiredRow = new ResType[4];
        System.arraycopy(marketTray[row], 0, acquiredRow, 0, 4);
        updateRow(row);
        return acquiredRow;
    }

    /**
     *
     * @param column
     * @return Resources player is acquiring
     */
    public ResType[] acquireColumn(int column){
        ResType[] acquiredColumn = new ResType[3];
        for(int i=0; i<3; i++){
            acquiredColumn[i] = marketTray[i][column];
        }
        updateColumn(column);
        return acquiredColumn;
    }

    @Override
    public String toString(){
        String marketToString = new String();
        marketToString = "Leftover Marble: " + leftoverMarble + "\n";
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                marketToString += marketTray[i][j] + " ";
            }
            marketToString += "\n";
        }
        return marketToString;
    }

}

