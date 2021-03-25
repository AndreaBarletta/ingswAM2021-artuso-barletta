package it.polimi.ingsw.model;


public class Market {

    private ResType[][] marketTray;
    private ResType[] acquiredRow;
    private ResType[] acquiredColumn;
    private ResType leftoverMarble;
    private ResType newLeftoverMarble;

    public Market(ResType leftoverMarble) {
        this.marketTray = new ResType[3][4];
        this.acquiredRow = new ResType[4];
        this.acquiredColumn = new ResType[3];
        this.leftoverMarble = leftoverMarble;
    }

    /**
     *
     * @return Actual market tray
     */
    public ResType[][] getMarketTray() {
        return marketTray;
    }

    /**
     *
     * @param row
     * Update market tray after buying a row
     */
    private void updateRow(int row) {
        newLeftoverMarble = marketTray[row][0];
        System.arraycopy(marketTray[row], 1, marketTray[row], 0, 4);
        marketTray[row][3] = leftoverMarble;
        leftoverMarble = newLeftoverMarble;
    }

    /**
     *
     * @param column
     * Update market tray after buying a column
     */
    private void updateColumn(int column) {
        newLeftoverMarble = marketTray[0][column];
        for (int i = 0; i < 3; i++) {
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
        for(int i=0; i<3; i++){
            acquiredColumn[i] = marketTray[i][column];
        }
        updateColumn(column);
        return acquiredColumn;
    }

}

