package it.polimi.ingsw.model.PersonalBoard;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.GameState;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalBoard implements ControllerEventListener {
    private String playerName;
    private List<LeaderCard> initialLeaderCards;
    private List<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private DevelopmentCardSlot[] developmentCardSlots;
    private Production baseProduction;
    private List<Production> leaderProductions;
    private Strongbox strongbox;
    private List<Depot> depots;
    private List<Depot> leaderDepots;
    private List<PersonalBoardEventListener> eventListeners;
    private boolean inkwell = false;
    private DevelopmentCardGrid cardGrid;
    private Market market;
    private boolean hasAlreadyChosenInitialResources;

    public PersonalBoard(String playerNickname, DevelopmentCardGrid cardGrid, Market market){
        this.playerName =playerNickname;
        this.cardGrid=cardGrid;
        this.market=market;
        //Create components
        eventListeners=new ArrayList<>();
        developmentCardSlots=new DevelopmentCardSlot[3];
        for(int i=0;i<3;i++){
            developmentCardSlots[i]=new DevelopmentCardSlot();
        }
        baseProduction=new Production();
        leaderCards=new ArrayList<>();
        leaderDepots=new ArrayList<>();
        leaderProductions=new ArrayList<>();
        //Create depots
        depots=new ArrayList<>();
        for(int i=0;i<3;i++){
            depots.add(new Depot(i+1));
        }
        //Create strongbox
        strongbox=new Strongbox();
        Map<ResType,Integer> baseIngredients=new HashMap<>();
        baseIngredients.put(ResType.ANY,2);
        Map<ResType,Integer> baseProducts=new HashMap<>();
        baseProducts.put(ResType.ANY,1);
        baseProduction=new Production(baseIngredients,baseProducts);
        hasAlreadyChosenInitialResources=false;
    }

    /**
     * Adds a new personal board event listener to the listener list
     * @param newEventListener new personal board event listener to be added to the listeners list
     */
    public void addEventListener(PersonalBoardEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * A player has discarded some resources, advance faith track
     * @param numberOfResources Number of resources discarded
     * @param playerName Name of the player that discarded the resources
     */
    public void discardResources(int numberOfResources,String playerName) {
        if(!this.playerName.equals(playerName)){
            faithTrack.incrementFaithTrack(numberOfResources);
        }
    }

    /**
     * Loads the faith track from a json file
     * @param path Path of the json file containing the faith track information
     * @return Whether or not the faith track was loaded successfully
     */
    public boolean loadFaithTrackFromFile(String path){
        String content;

        File file=new File(path);
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

    /**
     * Gets the name of the player associated with this player board
     * @return Name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the resources stored in the player board
     * @return The resources stored
     */
    private Map<ResType,Integer> getResources(){
        Map<ResType,Integer> resources=new HashMap<>();
        for(Depot d:depots){
            resources.merge(d.getContent().getKey(),d.getContent().getValue(),Integer::sum);
        }
        for(Depot d:leaderDepots){
            resources.merge(d.getContent().getKey(),d.getContent().getValue(),Integer::sum);
        }
        strongbox.getContent().forEach((k,v)-> resources.merge(k,v,Integer::sum));
        resources.remove(null);
        return resources;
    }

    /**
     * Adds a leader depot to the leader depot list
     * @param newLeaderDepot New depot to be added
     */
    public void addLeaderDepot(Depot newLeaderDepot){
        leaderDepots.add(newLeaderDepot);
    }

    /**
     * Removes a leader depot from the leader depot list
     * @param leaderDepot Depot to be removed
     */
    public void removeLeaderDepot(Depot leaderDepot){
        leaderDepots.remove(leaderDepot);
    }

    /**
     * Adds a leader production to the leader production list
     * @param newLeaderProduction New production to be added
     */
    public void addLeaderProduction(Production newLeaderProduction){
        leaderProductions.add(newLeaderProduction);
    }

    /**
     * Remove a leader production from the leader production list
     * @param leaderProduction Production to be removed
     */
    public void removeLeaderProduction(Production leaderProduction){
        leaderProductions.remove(leaderProduction);
    }

    /**
     *
     * @return Whether or not the production was successful
     */
    public boolean activateProduction(){
        /*for(PersonalBoardEventListener p:eventListeners){
            //Create a list of all the productions
            List<Production> productions=new ArrayList<>();
            for(Production lp:leaderProductions){
                productions.add(lp);
            }
            for(DevelopmentCardSlot d:developmentCardSlots){
                productions.add(d.getTopCard().getProduction());
            }
            productions.add(baseProduction);
            List<Production> selectedProductions=p.chooseProductions(productions,playerName);
            if(canProduce(selectedProductions)){
                for(Production pr:selectedProductions){
                    addResourcesToStrongbox(pr.getProducts());
                }
            }else{
                return false;
            }
        }*/
        return true;
    }

    public void addResourcesToStrongbox(Map<ResType,Integer> newResources){
        strongbox.add(newResources);
    }

    /**
     * Checks if the given productions can be activated
     * @param productions Productions to activate (including base production, leader card productions and development cards)
     * @return Whether or not there are enough resources to activate the production
     */
    private boolean canProduce(List<Production> productions){
        //Get chest and storage contents
        Map<ResType,Integer> resources=getResources();
        //Get the ingredients requires by the productions that have been selected
        Map<ResType,Integer> requirements=new HashMap<>();
        for(Production p:productions){
            p.getIngredients().forEach(
                    (key,value)->requirements.merge(key,value, Integer::sum)
            );
        }

        //Checks if there are enough resources
        for (Map.Entry<ResType, Integer> entry : resources.entrySet()) {
            if(requirements.get(entry.getKey())>entry.getValue()){
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a card can be bought and placed in the player board
     * @param devCard Development card to buy
     * @throws ResourcesException The player doesn't have enough resources to buy the development card selected
     * @throws LevelException The player doesn't have an high enough card in the selected slot to buy the development card selected
     */
    public void canBuyDevCard(DevelopmentCard devCard) throws ResourcesException,LevelException {
        if(!devCard.canBeBought(getResources()))
            throw new ResourcesException();

        boolean canAdd=false;
        for(int i=0;i<3;i++){
            try{
                canAddCardToSlot(devCard,i);
                canAdd=true;
            }catch(LevelException e){}
        }

        if(!canAdd)
            throw new LevelException();
    }

    /**
     * Checks if a given development card can be added to a slot
     * @param card Development card to be added
     * @param slot Slot in the playerboard whereto place the card
     * @throws LevelException The player doesn't have an high enough card in the selected slot to buy the development card selected
     */
    private void canAddCardToSlot(DevelopmentCard card,int slot) throws LevelException {
        if(developmentCardSlots[slot].getTopCard()!=null) {
            if (developmentCardSlots[slot].getTopCard().getLevel() == card.getLevel() - 1) {
                throw new LevelException();
            }
        }else{
            if (card.getLevel()!=1) {
                throw new LevelException();
            }
        }
    }

    /**
     * Add resources to depot
     * @param newResources Resourced to be added
     */
    public void addResourcesToDepot(ResType[] newResources) throws DepotSpaceException {
        Map<ResType,Integer> resources=new HashMap<>();
        for(ResType r:newResources){
            resources.merge(r, 1, Integer::sum);
        }

        for(ResType r:newResources){
            if(r==ResType.FAITH){
                faithTrack.incrementFaithTrack(1);
                r=null;
            }else{
                //Try adding to depots and leader depots
                for(Depot d: Stream.concat(depots.stream(),leaderDepots.stream()).collect(Collectors.toList())){
                    try{
                        d.add(r,1);
                        r=null;
                    } catch (DepotException e) {}
                }
            }
        }

        for(ResType r:newResources){
            if(r!=null){
                throw new DepotSpaceException();
            }
        }

    }

    public boolean hasInkwell(){
        return inkwell;
    }

    public void receiveInkwell(){
        inkwell = true;
    }

    /**
     * Gets the total amount of victory points, given by faith track, resources, leader cards and development cards
     * @return The total amount of victory points
     */
    public int getVictoryPoints(){
        int victoryPoints=0;

        for(DevelopmentCardSlot s:developmentCardSlots){
            for(DevelopmentCard d:s.getCards()){
                victoryPoints+=d.getVictoryPoints();
            }
        }

        victoryPoints+= faithTrack.getVictoryPoints();

        for(LeaderCard l:leaderCards){
            victoryPoints+=l.getVictoryPoints();
        }

        int numberOfResources=0;
        for(Map.Entry<ResType,Integer> e:getResources().entrySet()){
            numberOfResources+=e.getValue();
        }

        victoryPoints+=Math.floor((float)numberOfResources/5);

        return victoryPoints;
    }

    public void incrementFaithTrack(int faithPoint){
        faithTrack.incrementFaithTrack(faithPoint);
    }

    public void setInitialLeaderCards(List<LeaderCard> initialLeaderCards){
        this.initialLeaderCards=initialLeaderCards;
    }

    /**
     * Sets the initial chosen leader cards (among the 4)
     * @param leaderCards List of leader cards chosen among the 4
     * @return Whether or not the cards were among the 4 chosen (and could successfully be added)
     */
    public boolean setLeaderCards(List<LeaderCard> leaderCards){
        for(LeaderCard l:leaderCards){
            //Check if the card was chosen among the ones initially given
            boolean found=false;
            for(LeaderCard il:initialLeaderCards){
                if(il.getId()==l.getId()){
                    found=true;
                    break;
                }
            }
            if(!found) return false;
        }
        this.leaderCards=leaderCards;
        return true;
    }

    public List<LeaderCard> getLeaderCards() {      return leaderCards;     }

    /**
     * If the player has already chosen the initial resources.
     * Used for the 4th player of a game since he has to choose 2 types of resources
     * @return Whether or not the player has already chosen the initial resources
     */
    public boolean hasAlreadyChosenInitialResources(){
        return hasAlreadyChosenInitialResources;
    }

    /**
     * Setter for hasAlreadyChosenInitialResources
     * @param hasAlreadyChosenInitialResources Value to assign to hasAlreadyChosenInitialResources
     */
    public void setHasAlreadyChosenInitialResources(boolean hasAlreadyChosenInitialResources){
        this.hasAlreadyChosenInitialResources=hasAlreadyChosenInitialResources;
    }

    public void activateLeaderCard(int id) throws CardNotFoundException, CardTypeException, ResourcesException, LevelException {
        LeaderCard leaderCardToActivate=null;
        for(LeaderCard l:leaderCards){
            if(l.getId()==id){
                leaderCardToActivate=l;
                break;
            }
        }
        if(leaderCardToActivate==null){
            throw new CardNotFoundException();
        }

        List<DevelopmentCard> devCards=new ArrayList<>();
        for(DevelopmentCardSlot ds:developmentCardSlots)
            devCards.addAll(ds.getCards());

        leaderCardToActivate.canActivate(getResources(),devCards);
        leaderCardToActivate.activate();
        leaderCardToActivate.effectOnActivate(this);
    }

    public void discardLeaderCard(int id) throws CardNotFoundException {
        LeaderCard leaderCardToDiscard=null;
        for(LeaderCard l:leaderCards){
            if(l.getId()==id){
                leaderCardToDiscard=l;
                break;
            }
        }
        if(leaderCardToDiscard!=null){
            leaderCards.remove(leaderCardToDiscard);
            faithTrack.incrementFaithTrack(1);
        }else{
            throw new CardNotFoundException();
        }
    }
}
