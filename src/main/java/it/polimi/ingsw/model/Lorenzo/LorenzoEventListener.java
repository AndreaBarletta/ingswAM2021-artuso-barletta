package it.polimi.ingsw.model.Lorenzo;

public interface LorenzoEventListener {
    public void incrementLorenzoFaithTrack(int increment);
    public void removeBottomCard(String cardType);
    public void lorenzoShuffle();
    public void lorenzoWon();
}
