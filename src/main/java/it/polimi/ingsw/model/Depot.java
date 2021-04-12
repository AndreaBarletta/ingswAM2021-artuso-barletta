package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.DepotException;
import it.polimi.ingsw.model.exceptions.DepotResourceTypeException;
import it.polimi.ingsw.model.exceptions.DepotSpaceException;

import java.util.AbstractMap;

public class Depot {
    // Maximum capacity of the depot
    private final int capacity;
    // Number of resources currently present in the depot
    private int counter;
    // Type of resource currently present in the depot
    private ResType depotResource;

    /**
     * Creates a new depot
     * @param capacity Maximum capacity of the depot
     */
    public Depot(int capacity){
        this.capacity=capacity;
        this.counter=0;
    }

    /**
     * Changes the type of resource currently stored in the depot
     * @param depotResource New type of resource
     */
    public void setDepotResource(ResType depotResource){
        this.depotResource=depotResource;
    }

    public ResType getDepotResources(){
        return depotResource;
    }

    public void add(ResType resourceType,int quantity) throws DepotException {
        if(!checkResType(resourceType)) {
            throw new DepotResourceTypeException();
        }

        if(!checkSpace(counter+quantity)){
            throw new DepotSpaceException();
        }
        counter+=quantity;
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
