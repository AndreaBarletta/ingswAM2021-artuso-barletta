package it.polimi.ingsw.model.PersonalBoard.FaithTrack;

public class VaticanReport {

    private final int startPosition;
    private final int popeSpace;
    private PopeFavourCard popeFavourCard;

    public VaticanReport(int startPosition, int popeSpace) {
        this.startPosition = startPosition;
        this.popeSpace = popeSpace;
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
     *
     * @param faithMarker
     * When the report is sent, decides to discard or activate pope favour card
     */
    public void sendReport(int faithMarker){
        if(faithMarker>=startPosition)
            popeFavourCard.activate();
        else
            popeFavourCard.discard();
    }

    /**
     *
     * @return Victory points from pope favour card
     */
    public int getVictoryPoints(){
        return popeFavourCard.getVictoryPoints();
    }
}
