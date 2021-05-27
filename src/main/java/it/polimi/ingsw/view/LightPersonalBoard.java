package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.Strongbox;

import java.util.List;

public class LightPersonalBoard {
    private int[] leaderCards;
    private FaithTrack faithTrack;
    private int[] developmentCardSlots;
    private Strongbox strongbox;
    private List<Depot> depots;
    private boolean inkwell = false;

    public int[] getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(int[] leaderCards) {
        this.leaderCards = leaderCards;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public int[] getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    public void setDevelopmentCardSlots(int[] developmentCardSlots) {
        this.developmentCardSlots = developmentCardSlots;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(Strongbox strongbox) {
        this.strongbox = strongbox;
    }

    public List<Depot> getDepots() {
        return depots;
    }

    public void setDepots(List<Depot> depots) {
        this.depots = depots;
    }

    public boolean isInkwell() {
        return inkwell;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }
}
