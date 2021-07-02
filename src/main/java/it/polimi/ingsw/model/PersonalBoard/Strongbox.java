package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.NegQuantityException;

import java.util.HashMap;
import java.util.Map;

public class Strongbox {
    private final Map<ResType,Integer> resources;

    public Strongbox(){
        resources=new HashMap<>();
    }

    public Map<ResType,Integer> getContent(){
        return resources;
    }

    /**
     * Adds a resource to the strongbox
     * @param newResource Resource to be added to the strongbox
     */
    public void add(ResType newResource,int quantity){
        resources.compute(newResource,(k,v)->v==null?quantity:v+quantity);
    }

    /**
     * Remove a resource from the strongbox
     * @param toRemove Resource to be removed
     * @param quantity Quantity to be removed
     * @throws NegQuantityException The quantity removed exceeds the content of the strongbox
     */
    public void remove(ResType toRemove,int quantity) throws NegQuantityException {
        if(resources.get(toRemove)!=null){
            if(resources.get(toRemove)<quantity){
                throw new NegQuantityException();
            }
            resources.put(toRemove, resources.get(toRemove)-quantity);
        }else{
            throw new NegQuantityException();
        }
    }
}