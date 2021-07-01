package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

public class VaticanReport {

    private final int startPosition;
    private final int popeSpace;
    private PopeFavourCard popeFavourCard;

    public VaticanReport(int startPosition, int popeSpace) {
        this.startPosition = startPosition;
        this.popeSpace = popeSpace;
    }

    public void addPopeFavourCard(PopeFavourCard popeFavourCard){
        this.popeFavourCard=popeFavourCard;
    }
    /**
     *
     * @param faithMarker
     * @return Whether or not the player can send report
     */
    public boolean canSendReport(int faithMarker){
        if(popeFavourCard.isActive() || popeFavourCard.isDiscarded())
            return false;
        return faithMarker >= popeSpace;
    }

    /**
     * Send the vatican report, either discarding the pope favour card or activating it
     * @param faithMarker Current position on the faith track of the player
     * @return True if the report was activated, false if it was discarded
     */
    public boolean sendReport(int faithMarker){
        if(faithMarker>=startPosition){
            popeFavourCard.activate();
            return true;
        }
        else{
            popeFavourCard.discard();
            return false;
        }
    }

    /**
     *
     * @return Victory points from pope favour card
     */
    public int getVictoryPoints(){
        return popeFavourCard.getVictoryPoints();
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getPopeSpace() {
        return popeSpace;
    }
}
