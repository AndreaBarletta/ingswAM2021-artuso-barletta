package it.polimi.ingsw.model.Lorenzo;

import it.polimi.ingsw.model.CardType;

public interface LorenzoEventListener {
    public void incrementLorenzoFaithTrack(int increment);
    public void removeBottomCard(CardType cardType, int[] level);
    public void lorenzoShuffle();
    public void lorenzoWon();
}
