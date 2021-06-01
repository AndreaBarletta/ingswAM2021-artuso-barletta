package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.Strongbox;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.util.*;
import java.util.stream.Collectors;

public class LightPersonalBoard {
    private String playerName;
    private List<Integer> leaderCards;
    private FaithTrack faithTrack;
    private Stack<Integer>[] developmentCardSlots;
    private Strongbox strongbox;
    private List<Depot> depots;
    private boolean inkwell = false;
    private Production baseProduction;
    private List<Production> leaderProductions;
    private List<Depot> leaderDepots;

    public LightPersonalBoard(String playerName) {
        this.playerName=playerName;
        developmentCardSlots=new Stack[3];
        for(int i=0;i<3;i++){
            developmentCardSlots[i]=new Stack<>();
        }
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

    public List<Integer> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(List<Integer> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public Stack<Integer>[] getDevelopmentCardSlots() {
        return developmentCardSlots;
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

    public void addLeaderProduction(Production newLeaderProduction) {
        leaderProductions.add(newLeaderProduction);
    }

    public void removeLeaderProduction(Production leaderProduction){leaderProductions.remove(leaderProduction);}

    public void addLeaderDepot(Depot newDepot) {
        leaderDepots.add(newDepot);
    }

    public void removeLeaderDepot(Depot leaderDepot){
        leaderDepots.remove(leaderDepot);
    }

    public void setDevCardSlot(int id,int slot){
        developmentCardSlots[slot].push(id);
    }

    public void discardLeaderCard(int id){
        leaderCards.remove(Integer.valueOf(id));
    }
}
