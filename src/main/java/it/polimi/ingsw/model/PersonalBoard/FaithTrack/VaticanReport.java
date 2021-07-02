package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

public class VaticanReport {

    private final int startPosition;
    private final int popeSpace;
    private PopeFavourCard popeFavourCard;

    /**
     * Create a vatican report space
     * @param startPosition Start position of the report on the faith track
     * @param popeSpace Pope space on the faith track (which is also the last space of the vatican report)
     */
    public VaticanReport(int startPosition, int popeSpace) {
        this.startPosition = startPosition;
        this.popeSpace = popeSpace;
    }

    /**
     * Add a pope favour card to the vatican report
     * @param popeFavourCard Pope favour card to be added
     */
    public void addPopeFavourCard(PopeFavourCard popeFavourCard){
        this.popeFavourCard=popeFavourCard;
    }

    /**
     * Check if a vatican report can be sent
     * @param faithMarker Current position of the faith marker on the faith track
     * @return WHether or not the report can be sent
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
