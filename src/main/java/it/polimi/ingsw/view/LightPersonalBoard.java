package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.Strongbox;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightPersonalBoard {
    private String playerName;
    private int[] leaderCards;
    private FaithTrack faithTrack;
    private int[] developmentCardSlots;
    private Strongbox strongbox;
    private List<Depot> depots;
    private boolean inkwell = false;
    private Production baseProduction;
    private List<Production> leaderProductions;
    private List<Depot> leaderDepots;

    public LightPersonalBoard(String playerName) {
        this.playerName=playerName;
        developmentCardSlots=new int[3];
        for(int i=0;i<3;i++)
            developmentCardSlots[i]=-1;
        leaderProductions=new ArrayList<>();
        leaderDepots=new ArrayList<>();
        //create base production
        Map<ResType,Integer> baseIngredients=new HashMap<>();
        baseIngredients.put(ResType.ANY,2);
        Map<ResType,Integer> baseProducts=new HashMap<>();
        baseProducts.put(ResType.ANY,1);
        baseProduction=new Production(baseIngredients,baseProducts);
    }

    public String getPlayerName() {
        return playerName;
    }

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

    public void setDevelopmentCardSlots(int[] developmentCardSlots) { this.developmentCardSlots = developmentCardSlots; }

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

    public Production getBaseProduction() {
        return baseProduction;
    }

    public List<Production> getLeaderProductions() {
        return leaderProductions;
    }

    public void addLeaderProductions(Production newLeaderProduction) {
        leaderProductions.add(newLeaderProduction);
    }

    public void addLeaderDepot(Depot newDepot) {
        leaderDepots.add(newDepot);
    }

    public void setDevCardSlot(int id,int slot){
        developmentCardSlots[slot]=id;
    }
}
