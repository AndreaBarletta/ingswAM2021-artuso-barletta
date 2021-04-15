package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

import java.util.Map;

public class FaithTrack {
    //attributes
    private Map<Integer,Integer> victoryPoints;
    private int blackCrossMarker;
    private int faithMarker;
    private VaticanReport[] vaticanReports;

    //constructor
    public FaithTrack() {
        this.faithMarker=0;
        this.blackCrossMarker=0;
        this.vaticanReports = new VaticanReport[3];
    }

    /**
     * move the faithMarker forward of faithPoint's number
     * @param faithPoint number of faith point made
     */
    public void incrementFaithTrack(int faithPoint) {
        faithMarker+=faithPoint;
    }

    public int getPosition() {
        return faithMarker;
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
     * sums the points of each vatican report and the points earned by advancements
     * @return total points
     */
    public int getVictoryPoints(){
        int victoryPoints=0;
        for (VaticanReport v:vaticanReports)    victoryPoints+=v.getVictoryPoints();
        victoryPoints+=this.victoryPoints.get(faithMarker);
        return victoryPoints;
    }
}