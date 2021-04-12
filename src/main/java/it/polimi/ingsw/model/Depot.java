package it.polimi.ingsw.model;

import java.util.AbstractMap;

public class Depot {
    // Maximum capacity of the depot
    private final int capacity;
    // Number of resources currently present in the depot
    private int counter;
    private ResType depotResource;

    /**
     * Creates a
     * @param capacity Maximum capacity of the depot
     */
    public Depot(int capacity){
        this.capacity=capacity;
        this.counter=0;
    }

    public void setDepotResource(ResType depotResource){
        this.depotResource=depotResource;
    }

    public ResType getDepotResources(){
        return depotResource;
    }

    public boolean add(ResType resourceType,int quantity){
        if(checkResType(resourceType)){
            if(checkSpace(counter+quantity)){
                counter+=quantity;
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean checkResType(ResType resourceType){
        return depotResource==resourceType;
    }

    private boolean checkSpace(int newCounter){
        return newCounter<=capacity;
    }

    public AbstractMap.SimpleEntry<ResType,Integer> getContent(){
        return new AbstractMap.SimpleEntry<>(depotResource, counter);
    }
}
