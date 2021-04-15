package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FaithTrack {
    //attributes
    private int blackCrossMarker;
    private int faithMarker;
    private VaticanReport[] vaticanReports;

    //constructor
    public FaithTrack() {
        this.faithMarker=0;
        this.blackCrossMarker=0;
        this.vaticanReports = new VaticanReport[3];
    }

    //methods
    public boolean loadVaticanReportsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading Vatican Reports i.e. wrong path");
            return false;
        }
        //vaticanReports = content;
        return true;
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
     * sums the points of each vatican report
     * @return total points
     */
    public int getVictoryPoints(){
        int victoryPoints=0;
        for (VaticanReport v:vaticanReports)    victoryPoints+=v.getVictoryPoints();
        return victoryPoints;
    }
}