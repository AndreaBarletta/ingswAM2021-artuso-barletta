package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market {

    private final ResType[][] marketTray;
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

    public ResType[][] getMarketTray() {
        return marketTray;
    }

    public ResType getLeftoverMarble(){ return leftoverMarble;}

    /**
     * Update market tray after acquiring a row
     * @param row Row acquired
     */
    private void updateRow(int row) {
        ResType newLeftoverMarble = marketTray[row][0];
        System.arraycopy(marketTray[row], 1, marketTray[row], 0, 3);
        marketTray[row][3] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

    /**
     * Update market tray after acquiring a column
     * @param column Column acquired
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
     * Acquires the resources on a row, and update the row
     * @param row Row to acquire the resources from
     * @return The resources acquired
     */
    public ResType[] acquireRow(int row){
        ResType[] acquiredRow = new ResType[4];
        System.arraycopy(marketTray[row], 0, acquiredRow, 0, 4);
        updateRow(row);
        return acquiredRow;
    }

    /**
     * Acquires the resources on a column, and update the column
     * @param column Column to acquire the resources from
     * @return The resources acquired
     */
    public ResType[] acquireColumn(int column){
        ResType[] acquiredColumn = new ResType[3];
        for(int i=0; i<3; i++){
            acquiredColumn[i] = marketTray[i][column];
        }
        updateColumn(column);
        return acquiredColumn;
    }
}

