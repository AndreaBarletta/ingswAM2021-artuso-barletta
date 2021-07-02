package it.polimi.ingsw.model.PersonalBoard;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCardGrid;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.PersonalBoard.FaithTrack.PopeFavourCard;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.LightDepot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class PersonalBoard implements ControllerEventListener {
    //Properties
    private final String playerName;
    private boolean inkwell;
    private boolean hasAlreadyChosenInitialResources;
    private boolean hasAlreadyPlayedLeaderAction;
    //Components
    private FaithTrack faithTrack;
    private final DevelopmentCardSlot[] developmentCardSlots;
    private Production baseProduction;
    //Storage
    private final Strongbox strongbox;
    private Depot[] depots;
    //Leader cards
    private List<LeaderCard> initialLeaderCards;
    private List<LeaderCard> leaderCards;
    private List<Depot> leaderDepots;
    private final List<Production> leaderProductions;
    private final List<ResType> leaderConverts;
    private final List<Map<ResType,Integer>> leaderDiscounts;

    private final List<PersonalBoardEventListener> eventListeners;

    public PersonalBoard(String playerNickname){
        this.playerName =playerNickname;
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
        leaderConverts=new ArrayList<>();
        leaderDiscounts=new ArrayList<>();
        //Create depots
        depots=new Depot[3];
        for(int i=0;i<3;i++){
            depots[i]=new Depot(i+1);
        }
        //Create strongbox
        strongbox=new Strongbox();
        //Create base production
        Map<ResType,Integer> baseIngredients=new HashMap<>();
        baseIngredients.put(ResType.ANY,2);
        Map<ResType,Integer> baseProducts=new HashMap<>();
        baseProducts.put(ResType.ANY,1);
        baseProduction=new Production(baseIngredients,baseProducts);
        hasAlreadyChosenInitialResources=false;
        hasAlreadyPlayedLeaderAction=false;
        inkwell=false;
    }

    /**
     * Adds a new personal board event listener to the listener list
     * @param newEventListener new personal board event listener to be added to the listeners list
     */
    public void addEventListener(PersonalBoardEventListener newEventListener){
        eventListeners.add(newEventListener);
    }

    /**
     * Loads the faith track from a json file
     * @param fileString Path of the json file containing the faith track information
     * @return Whether or not the faith track was loaded successfully
     */
    public boolean loadFaithTrackFromFile(String fileString){
        InputStream inputStream=getClass().getResourceAsStream(fileString);
        if(inputStream==null){
            System.out.println("Error reading from file while loading faith track i.e. wrong path");
            return false;
        }
        Reader reader=new InputStreamReader(inputStream);

        Gson gson=new Gson();
        try{
            faithTrack=gson.fromJson(reader, FaithTrack.class);
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

    public Production getBaseProduction() {
        return baseProduction;
    }

    public DevelopmentCardSlot[] getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    /**
     * Gets the resources stored in the player board
     * @return The resources stored
     */
    private Map<ResType,Integer> getResources(){
        Map<ResType,Integer> resources;
        resources=getDepotsResource();
        strongbox.getContent().forEach((k,v)-> resources.merge(k,v,Integer::sum));
        resources.remove(null);
        return resources;
    }

    private Map<ResType,Integer> getDepotsResource(){
        Map<ResType,Integer> resources=new HashMap<>();
        for(Depot d:depots){
            resources.merge(d.getContent().getKey(),d.getContent().getValue(),Integer::sum);
        }
        for(Depot d:leaderDepots){
            resources.merge(d.getContent().getKey(),d.getContent().getValue(),Integer::sum);
        }
        resources.remove(null);
        resources.remove(ResType.ANY);
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
        leaderDepots.removeIf((d)->d.getDepotResources().equals(leaderDepot.getDepotResources()));
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
        leaderProductions.removeIf((d)->d.equals(leaderProduction));
    }

    /**
     * Adds a leader convert to the leader convert list
     * @param newLeaderConvert New convert to be added
     */
    public void addLeaderConvert(ResType newLeaderConvert){
        leaderConverts.add(newLeaderConvert);
    }

    /**
     * Remove a leader convert from the leader convert list
     * @param leaderConvert Convert to be removed
     */
    public void removeLeaderConvert(ResType leaderConvert){
        leaderConverts.removeIf((d)->d.equals(leaderConvert));
    }

    /**
     * Adds a leader discount to the leader discount list
     * @param newLeaderDiscount New discount to be added
     */
    public void addLeaderDiscount(Map<ResType,Integer> newLeaderDiscount){
        leaderDiscounts.add(newLeaderDiscount);
    }

    /**
     * Remove a leader discount from the leader discount list
     * @param leaderDiscount Discount to be removed
     */
    public void removeLeaderDiscount(Map<ResType,Integer> leaderDiscount){
        leaderDiscounts.removeIf((d)->d.equals(leaderDiscount));
    }

    public List<Production> getLeaderProductions() {
        return leaderProductions;
    }

    public ResType convert(){
        if(leaderConverts.size()==1)
            return leaderConverts.get(0);
        if(leaderConverts.size()==2)
            return ResType.WHITEMARBLE;

        return ResType.ANY;
    }

    /**
     * Checks if a card can be bought and placed in the player board
     * @param devCard Development card to buy
     * @throws ResourcesException The player doesn't have enough resources to buy the development card selected
     * @throws LevelException The player doesn't have an high enough card in the selected slot to buy the development card selected
     */
    public void canBuyDevCard(DevelopmentCard devCard,int [] discountIds) throws ResourcesException,LevelException {
        Map<ResType,Integer> discounts=new HashMap<>();
        if(discountIds!=null)
            for(int id:discountIds){
                for(Map.Entry<ResType,Integer> me:leaderDiscounts.get(id).entrySet())
                    discounts.compute(me.getKey(),(k,v)->v==null?me.getValue():v+me.getValue());
            }
        if(!devCard.canBeBought(getResources(),discounts))
            throw new ResourcesException();

        boolean canAdd=false;
        for(int i=0;i<3;i++){
            try{
                developmentCardSlots[i].canAddCard(devCard);
                canAdd=true;
            }catch(LevelException e){}
        }

        if(!canAdd)
            throw new LevelException();
    }

    public void addDevCardToSlot(DevelopmentCard card, int slot) throws LevelException{
        developmentCardSlots[slot].addCard(card);
    }

    /**
     * Add a resource to a depot (automatically makes space if possible by moving the resources around)
     * @param newResource Resource to be added
     */
    public void addResourceToDepot(ResType newResource) throws DepotSpaceException {
        //Try to place the new resource in the board depot
        boolean added=false;
        for(Depot d:depots){
            try{
                boolean canPlaceInAny=true;
                for(Depot dep:depots){
                    if(dep.getDepotResources()==newResource){
                        canPlaceInAny=false;
                        break;
                    }
                }
                if(canPlaceInAny){
                    d.add(newResource,1);
                    added=true;
                }
            }catch(DepotResourceTypeException e) {}catch(DepotSpaceException e){}
            if(added) {
                break;
            }
        }
        //Try to place the new resource in the leader depots
        if(!added){
            for(Depot d:leaderDepots){
                try{
                    d.add(newResource,1);
                    added=true;
                }catch(DepotResourceTypeException e) {}catch(DepotSpaceException e){}
                if(added) {
                    break;
                }
            }
        }
        if(!added){
            //Rearrange the resource to make space
            //Save the current resources
            Map<ResType,Integer> resourcesToPlace=getDepotsResource();
            resourcesToPlace.compute(newResource,(k,v)->v==null?1:v+1);
            //Create temporary empty depots
            List<Depot> newDepots=new ArrayList<>();
            List<Depot> newLeaderDepots=new ArrayList<>();
            for(Depot d:leaderDepots){
                Depot newLeaderDepot=new Depot(d.getCapacity());
                newLeaderDepot.setDepotResource(d.getDepotResources());
                newLeaderDepots.add(newLeaderDepot);
            }
            for(Depot d:depots){
                newDepots.add(new Depot(d.getCapacity()));
            }
            //Place in the leader depots first
            for(Map.Entry<ResType,Integer> me:resourcesToPlace.entrySet()){
                for(Depot d:newLeaderDepots){
                    //Add the maximum amount possible
                    try {
                        d.add(me.getKey(), me.getValue()>2?2:me.getValue());
                        resourcesToPlace.put(me.getKey(),me.getValue()>2?me.getValue()-2:0);
                    }catch(Exception e){}
                }
            }
            //Place the remaining resources in the depots
            for(Map.Entry<ResType,Integer> me:resourcesToPlace.entrySet()){
                boolean resourceAdded;
                if(me.getValue()>3){
                    break;
                }
                resourceAdded=false;
                for(Depot d:newDepots){
                    if(d.getCapacity()>=me.getValue()) {
                        try {
                            d.add(me.getKey(), me.getValue());
                            me.setValue(0);
                            resourceAdded=true;
                        }catch(Exception e){}
                    }
                    if(resourceAdded)
                        break;
                }
                if(!resourceAdded){
                    break;
                }
            }
            boolean resourceAdded=true;
            for(Map.Entry<ResType,Integer> me:resourcesToPlace.entrySet()){
                if(me.getValue()!=0){
                    resourceAdded=false;
                    break;
                }
            }
            if(!resourceAdded){
                throw new DepotSpaceException();
            }else{
                depots=newDepots.toArray(Depot[]::new);
                leaderDepots=newLeaderDepots;
                added=true;
            }
        }
        if(!added)
            throw new DepotSpaceException();
    }

    public void addResourcesToStrongbox(ResType resource,int quantity){
        strongbox.add(resource,quantity);
    }

    public void payResource(ResType resource,int quantity,int[] discountIds){
        int leftToRemove=quantity;
        if(discountIds!=null){
            for(int id:discountIds){
                leftToRemove-=leaderDiscounts.get(id).getOrDefault(resource,0);
            }
        }
        //Try to remove from the depots first
        for(Depot d:depots){
            try{
                if(d.getDepotResources()==resource){
                    if(d.getCounter()>=quantity){
                        //All the resources can be removed from here
                        d.remove(resource, quantity);
                        return;
                    }else{
                        //Remove only part of the resources to remove
                        leftToRemove-=d.getCounter();
                        d.remove(resource,d.getCounter());
                        if(leftToRemove==0)
                            return;
                    }
                }
            }catch(Exception e){}
        }
        //Then try to remove from the leader depots
        for(Depot d:leaderDepots){
            try{
                if(d.getDepotResources()==resource){
                    if(d.getCounter()>=quantity){
                        //All the resources can be removed from here
                        d.remove(resource, quantity);
                        return;
                    }else{
                        //Remove only part of the resources to remove
                        leftToRemove-=d.getCounter();
                        d.remove(resource,d.getCounter());
                        if(leftToRemove==0)
                            return;
                    }
                }
            }catch(Exception e){}
        }
        //Lastly, try to remove from the strongbox
        try{
            strongbox.remove(resource,leftToRemove);
        }catch(Exception e){}
    }

    public Map<ResType,Integer> getDepotsContent(){
        Map<ResType,Integer> content=new HashMap<>();
        for(Depot d:depots)
            content.merge(d.getDepotResources(),d.getCounter(),Integer::sum);
        return content;
    }

    public Depot[] getDepots(){
        return depots;
    }

    public Map<ResType,Integer> getLeaderDepotsContent(){
        Map<ResType,Integer> content=new HashMap<>();
        for(Depot ld:leaderDepots)
            content.merge(ld.getDepotResources(),ld.getCounter(),Integer::sum);
        return content;
    }

    public List<Depot> getLeaderDepots(){
        return leaderDepots;
    }

    public Map<ResType,Integer> getStrongboxContent(){
        return strongbox.getContent();
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
        for(PersonalBoardEventListener p:eventListeners)
            p.incrementFaithTrack(playerName,faithPoint);
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

    /**
     * If the player has already chosen a leader action during their turn.
     * If it's false, the player can choose another leader action after the turn action
     * @return Whether or not the player has already chosen a leader action during their turn
     */
    public boolean hasAlreadyPlayedLeaderAction(){
        return hasAlreadyPlayedLeaderAction;
    }

    /**
     * Setter for hasAlreadyPlayedLeaderAction
     * @param hasAlreadyPlayedLeaderAction Value to assign to hasAlreadyPlayedLeaderAction
     */
    public void setHasAlreadyPlayedLeaderAction(boolean hasAlreadyPlayedLeaderAction){
        this.hasAlreadyPlayedLeaderAction=hasAlreadyPlayedLeaderAction;
    }

    public void activateLeaderCard(LeaderCard leaderCard) throws CardNotFoundException, CardTypeException, ResourcesException, LevelException,AlreadyActiveException {
        if(leaderCards.contains(leaderCard)){
            List<DevelopmentCard> devCards=new ArrayList<>();
            for(DevelopmentCardSlot ds:developmentCardSlots)
                devCards.addAll(ds.getCards());

            if(leaderCard.isActive())
                throw new AlreadyActiveException();

            leaderCard.canActivate(getResources(),devCards);
            leaderCard.activate();
            leaderCard.effectOnActivate(this);
        }else{
            throw new CardNotFoundException();
        }
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
            leaderCardToDiscard.effectOnDiscard(this);
            faithTrack.incrementFaithTrack(1);
        }else{
            throw new CardNotFoundException();
        }
    }

    /**
     * Checks if the given productions can be activated
     * @param productions Productions to activate (including base production, leader card productions and development cards)
     * @return Whether or not there are enough resources to activate the production
     */
    private boolean canProduce(Production[] productions){
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
            if(requirements.get(entry.getKey())==null)
                return false;
            if(requirements.get(entry.getKey())>entry.getValue()){
                return false;
            }
        }

        return true;
    }

    public void activateProductions(Production[] productions) throws ResourcesException {
        //Remove ingredients
        if(canProduce(productions)) {
            for(Production p : productions) {
                p.getIngredients().forEach((k,v)->payResource(k,v,null));
            }
        } else {
            throw new ResourcesException();
        }
        //Add products
        for(Production p : productions) {
            for(Map.Entry<ResType,Integer> product:p.getProducts().entrySet())
                addResourcesToStrongbox(product.getKey(),product.getValue());
        }
    }

    public boolean canDiscount(int[] ids){
        for(int id:ids)
            if(id>leaderDiscounts.size())
                return false;

        return true;
    }

    public boolean canAddToDepot(ResType[] resources){
        //Save a copy of the current depots
        List<Depot> tempLeaderDepots=new ArrayList<>();
        Depot[] tempDepots=new Depot[3];

        for(int i=0;i<3;i++){
            tempDepots[i]=new Depot(depots[i].getCapacity());
            try{
                tempDepots[i].add(depots[i].getDepotResources(),depots[i].getCounter());
            }catch(DepotSpaceException e){}catch(DepotResourceTypeException e){}
        }

        for(Depot leaderDepot:leaderDepots){
            tempLeaderDepots.add(new Depot(leaderDepot.getCapacity()));
            try{
                tempLeaderDepots.get(tempLeaderDepots.size()-1).add(leaderDepot.getDepotResources(),leaderDepot.getCounter());
            }catch(DepotSpaceException e){}catch(DepotResourceTypeException e){}
        }

        //Try adding all the new resources
        for(ResType resource:resources){
            try{
                addResourceToDepot(resource);
            }catch(DepotSpaceException e){
                //Restore the previous depots condition
                depots=tempDepots;
                leaderDepots=tempLeaderDepots;
                return false;
            }
        }
        depots=tempDepots;
        leaderDepots=tempLeaderDepots;
        return true;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
}