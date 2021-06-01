package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.DepotResourceTypeException;
import it.polimi.ingsw.exceptions.DepotSpaceException;
import it.polimi.ingsw.exceptions.NegQuantityException;

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
        counter=0;
        depotResource=ResType.ANY;
    }

    /**
     * Changes the type of resource currently stored in the depot
     * @param depotResource New type of resource
     */
    public void setDepotResource(ResType depotResource){
        this.depotResource=depotResource;
    }

    /**
     * Gets the type of resource currently stored in the depot
     * @return The type of resource currently stored in the depot; null if the depot is empty
     */
    public ResType getDepotResources(){
        return depotResource;
    }

    /**
     * Add a specified quantity and type of resources to the depot
     * @param resourceType Type of the resource to be added
     * @param quantity Quantity of the resource to be added
     * @throws DepotResourceTypeException
     * @throws DepotSpaceException
     */
    public void add(ResType resourceType,int quantity) throws DepotResourceTypeException,DepotSpaceException {
        if(counter+quantity>capacity){
            throw new DepotSpaceException();
        }
        if(depotResource==ResType.ANY){
            depotResource=resourceType;
        }else if(depotResource!=resourceType) {
            throw new DepotResourceTypeException();
        }


        counter+=quantity;
    }

    /**
     * remove a specified quantity and type of resources from the depot
     * @param resourceType Type of the resource to be removed
     * @param quantity Quantity of the resource to be removed
     * @throws NegQuantityException Trying to delete more resources than there are in the depot
     * @throws DepotResourceTypeException Resource type is not valid
     */
    public void remove(ResType resourceType, int quantity) throws NegQuantityException, DepotResourceTypeException {
        if(depotResource!=resourceType) {
            throw new DepotResourceTypeException();
        }

        if(counter-quantity<0){
            throw new NegQuantityException();
        }
        counter-=quantity;
    }

    public void removeAll(){
        counter=0;
    }

    /**
     * Gets the content of the depot
     * @return A tuple containing the type of the resource and the quantity
     */
    public AbstractMap.SimpleEntry<ResType,Integer> getContent(){
        return new AbstractMap.SimpleEntry<>(depotResource, counter);
    }

    public int getCapacity(){
        return capacity;
    }

    public int getCounter() {
        return counter;
    }
}
