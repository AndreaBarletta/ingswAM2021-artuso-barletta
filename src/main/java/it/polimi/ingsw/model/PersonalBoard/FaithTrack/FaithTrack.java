package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

import it.polimi.ingsw.view.Colors;

import java.awt.*;
import java.util.Arrays;

public class FaithTrack {
    //attributes
    private int faithMarker;
    private VaticanReport[] vaticanReports;
    private int[] victoryPoints;
    private boolean isAtEnd;

    public FaithTrack() {
        isAtEnd=false;
        this.faithMarker=0;
    }

    /**
     * Initiallizes the vatican reports
     */
    public void setVaticanReports(){
        for(int i=0;i<vaticanReports.length;i++){
            vaticanReports[i].addPopeFavourCard(new PopeFavourCard(i+2));
        }
    }

    /**
     * Move the faith marker forward on the faith track
     * @param faithPoint Number of placed to move forward
     */
    public void incrementFaithTrack(int faithPoint) {
        faithMarker+=faithPoint;
        if(faithMarker>=victoryPoints.length-1){
            faithMarker=victoryPoints.length-1;
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

    /**
     * Send the k-th vatican report
     * @param k Index of the vatican report to send
     * @return Whether or not the report was activated
     */
    public boolean sendVaticanReport(int k) {
        return vaticanReports[k].sendReport(faithMarker);
    }

    /**
     * Sum of the points acquired by advancing on the faith track, plus the activated vatican reports
     * @return The number of victory points gained from the faith track
     */
    public int getVictoryPoints(){
        int victoryPoints = this.victoryPoints[faithMarker];
        for (VaticanReport v:vaticanReports)    victoryPoints += v.getVictoryPoints();
        return victoryPoints;
    }

    @Override
    public String toString() {
        StringBuilder faithTrackToString= new StringBuilder();
        for(int i=0;i<victoryPoints.length;i++){
            faithTrackToString.append(Colors.GRAY.escape());

            for(VaticanReport v:vaticanReports){
                if(i>=v.getStartPosition()&&i<v.getPopeSpace())
                    faithTrackToString.append(Colors.BRIGHT_CYAN.escape());
                if(i==v.getPopeSpace())
                    faithTrackToString.append(Colors.YELLOW.escape());
            }
            if(faithMarker==i)
                faithTrackToString.append(Colors.RED.escape());

            faithTrackToString.append("["+victoryPoints[i]+"]"+Colors.RESET.escape());
        }

        return faithTrackToString.toString();
    }
}