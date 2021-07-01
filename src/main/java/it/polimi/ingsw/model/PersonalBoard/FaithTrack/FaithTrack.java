package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

import it.polimi.ingsw.view.Colors;

import java.awt.*;
import java.util.Arrays;

public class FaithTrack {
    //attributes
    private final int blackCrossMarker;
    private int faithMarker;
    private VaticanReport[] vaticanReports;
    private int[] victoryPoints;
    private boolean isAtEnd;

    //constructor
    public FaithTrack() {
        isAtEnd=false;
        this.faithMarker=0;
        this.blackCrossMarker=0;
        for(int i=0;i<vaticanReports.length;i++){
            vaticanReports[i].addPopeFavourCard(new PopeFavourCard(i+2));
        }
    }

    /**
     * move the faithMarker forward of faithPoint's number
     * @param faithPoint number of faith point made
     */
    public void incrementFaithTrack(int faithPoint) {
        faithMarker+=faithPoint;
        if(faithMarker>=victoryPoints.length){
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
     * Check if there is a vatican report that can be sent.
     * @return The vatican report that can be sent, or -1
     */
    public int canSendVaticanReport() {
        //Search for the first vatican report that is neither activated nor discarded and check if it can be activated
        for(int i=0;i<vaticanReports.length;i++) {
            if (vaticanReports[i].canSendReport(faithMarker)) return i;
        }
        return -1;
    }

    public boolean sendVaticanReport(int k) {
        return vaticanReports[k].sendReport(faithMarker);
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

    @Override
    public String toString() {
        StringBuilder faithTrackToString= new StringBuilder();
        for(int i=0;i<victoryPoints.length;i++)
            faithTrackToString.append(faithMarker==i?
                    Colors.RED.escape()+"["+i+":"+victoryPoints[i]+"]"+ Colors.RESET.escape()
                    :Colors.GRAY.escape()+"["+i+":"+victoryPoints[i]+"]"+ Colors.RESET.escape());


        return faithTrackToString.toString();
    }
}