package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.exceptions.NegQuantityException;

import java.util.HashMap;
import java.util.Map;

public class Strongbox {
    //attributes
    private final Map<ResType,Integer> resources;

    //constructor
    public Strongbox(){
        resources=new HashMap<>();
    }

    //methods
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
     * Removes a resource from the strongbox
     * @param toRemove Resource to remove from the strongbox
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