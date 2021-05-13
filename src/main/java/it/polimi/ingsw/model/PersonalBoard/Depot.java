package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.DepotException;
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
        this.counter=0;
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
     * @throws DepotException The depot is already storing a resource of another type or it's full
     */
    public void add(ResType resourceType,int quantity) throws DepotException {
        if(depotResource!=resourceType) {
            throw new DepotResourceTypeException();
        }

        if(counter+quantity>capacity){
            throw new DepotSpaceException();
        }
        counter+=quantity;
    }

    /**
     * remove a specified quantity and type of resources from the depot
     * @param resourceType Type of the resource to be removed
     * @param quantity Quantity of the resource to be removed
     * @throws DepotException The depot is storing a resource of another type or if it's trying to remove more than there is
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

    /**
     * Gets the content of the depot
     * @return A tuple containing the type of the resource and the quantity
     */
    public AbstractMap.SimpleEntry<ResType,Integer> getContent(){
        return new AbstractMap.SimpleEntry<>(depotResource, counter);
    }
}
