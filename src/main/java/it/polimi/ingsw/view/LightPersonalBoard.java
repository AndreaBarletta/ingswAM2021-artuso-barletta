package it.polimi.ingsw.view;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import javafx.scene.effect.Light;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class LightPersonalBoard {
    private final String playerName;
    private List<Integer> leaderCards;
    private FaithTrack faithTrack;
    private final List<Stack<Integer>> developmentCardSlots;
    private LightDepot[] depots;
    private List<LightDepot> leaderDepots;
    private LightStrongbox strongbox;
    private boolean inkwell;
    private final Production baseProduction;
    private final List<Production> leaderProductions;
    private final List<ResType> leaderConverts;
    private final List<Map<ResType,Integer>> leaderDiscounts;

    public LightPersonalBoard(String playerName) {
        this.playerName=playerName;
        developmentCardSlots= new ArrayList<>();
        for(int i=0;i<3;i++){
            developmentCardSlots.add(new Stack<>());
        }
        leaderProductions=new ArrayList<>();
        leaderDepots=new ArrayList<>();
        leaderConverts=new ArrayList<>();
        leaderDiscounts=new ArrayList<>();
        //create base production
        Map<ResType,Integer> baseIngredients=new HashMap<>();
        baseIngredients.put(ResType.ANY,2);
        Map<ResType,Integer> baseProducts=new HashMap<>();
        baseProducts.put(ResType.ANY,1);
        baseProduction=new Production(baseIngredients,baseProducts);
        depots = new LightDepot[3];
        inkwell = false;
    }

    public boolean loadFaithTrackFromFile(String fileString){
        String content;
        File file=new File(fileString);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading faith track i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            faithTrack=gson.fromJson(content, FaithTrack.class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for faith track");
            return false;
        }
        return true;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    public Production getBaseProduction() {
        return baseProduction;
    }

    public List<Stack<Integer>> getDevelopmentCardSlots() {
        return developmentCardSlots;
    }
    public void setDevCardSlot(int id, int slot){
        developmentCardSlots.get(slot).push(id);
    }

    public LightDepot[] getDepots() {
        return depots;
    }
    public void setDepots(LightDepot[] depots) {
        this.depots = depots;
    }

    public LightStrongbox getStrongbox() {
        return strongbox;
    }
    public void setStrongbox(LightStrongbox strongbox) {
        this.strongbox = strongbox;
    }

    public List<Integer> getLeaderCards() {
        return leaderCards;
    }
    public void setLeaderCards(List<Integer> leaderCards) {
        this.leaderCards = leaderCards;
    }
    public void discardLeaderCard(int id){
        leaderCards.remove(Integer.valueOf(id));
    }

    public List<Production> getLeaderProductions() {
        return leaderProductions;
    }
    public void addLeaderProduction(Production newLeaderProduction) {
        leaderProductions.add(newLeaderProduction);
    }
    public void removeLeaderProduction(Production leaderProduction){leaderProductions.remove(leaderProduction);}

    public List<LightDepot> getLeaderDepots() {
        return leaderDepots;
    }
    public void setLeaderDepots(List<LightDepot> leaderDepots) {
        this.leaderDepots = leaderDepots;
    }
    public void addLeaderDepot(Depot depot) { leaderDepots.add(new LightDepot(depot.getDepotResources(),0,depot.getCapacity())); }
    public void removeLeaderDepot(Depot depot) { leaderDepots.removeIf(d -> d.getResource() == depot.getDepotResources()); }

    public void addLeaderConvert(ResType newLeaderConvert){
        leaderConverts.add(newLeaderConvert);
    }
    public void removeLeaderConvert(ResType leaderConvert){
        leaderConverts.remove(leaderConvert);
    }
    public List<ResType> getLeaderConverts() {
        return leaderConverts;
    }

    public void addLeaderDiscount(Map<ResType,Integer> newLeaderDiscount){
        leaderDiscounts.add(newLeaderDiscount);
    }
    public void removeLeaderDiscount(Map<ResType,Integer> leaderDiscount){
        leaderDiscounts.remove(leaderDiscount);
    }
    public List<Map<ResType, Integer>> getLeaderDiscounts() {
        return leaderDiscounts;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
}
