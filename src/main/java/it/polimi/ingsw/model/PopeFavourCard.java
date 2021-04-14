package it.polimi.ingsw.model;

public class PopeFavourCard {
    private int victoryPoints;
    private boolean isActive;
    private boolean isDiscarded;

    /**
     * Create a Pope favour card
     * @param victoryPoints victory points given by the pope favour card when activated
     */
    public PopeFavourCard(int victoryPoints){
        this.victoryPoints=victoryPoints;
        isActive=false;
        isDiscarded=false;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }

    public void discard(){
        isDiscarded=true;
    }

    public boolean isDiscarded(){
        return isDiscarded;
    }

    public void activate(){
        isActive=true;
    }

    public boolean isActive(){
        return isActive;
    }

}
