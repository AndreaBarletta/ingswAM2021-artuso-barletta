package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

public class FaithTrack {
    //attributes
    private int[] victoryPoints;
    private int blackCrossMarker;
    private int faithMarker;
    private VaticanReport[] vaticanReports;
    private boolean isAtEnd;

    //constructor
    public FaithTrack() {
        isAtEnd=false;
        this.faithMarker=0;
        this.blackCrossMarker=0;
        this.vaticanReports = new VaticanReport[3];
        this.victoryPoints = new int[24];
    }

    /**
     * move the faithMarker forward of faithPoint's number
     * @param faithPoint number of faith point made
     */
    public void incrementFaithTrack(int faithPoint) {
        faithMarker+=faithPoint;
        if(faithMarker>victoryPoints.length){
            faithMarker=victoryPoints.length;
            isAtEnd=true;
        }
    }

    public int getPosition() {
        return faithMarker;
    }

    public boolean isAtEnd(){
        return isAtEnd;
    }

    /**
     *call canSendReport in VaticanReport
     */
    public boolean canSendVaticanReport() {
        for (VaticanReport v:vaticanReports)
            if(v.canSendReport(faithMarker))    return true;

        return false;
    }

    //to be implemented input from file of vatican report
    public void sendVaticanReport(VaticanReport vaticanReport) {
        vaticanReport.sendReport(faithMarker);
    }

    /**
     * sums the points earned by advancements and the points of each vatican report
     * @return total points
     */
    public int getVictoryPoints(){
        int victoryPoints = this.victoryPoints[faithMarker];
        for (VaticanReport v:vaticanReports)    victoryPoints += v.getVictoryPoints();
        return victoryPoints;
    }
}