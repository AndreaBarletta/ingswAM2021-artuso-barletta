package it.polimi.ingsw.model;

import java.util.AbstractMap;
import java.util.Map;

public class Chest {
    //attributes
    private Map<ResType,Integer> resources;

    //constructor
    public Chest(Map<ResType,Integer> resources){
        this.resources=resources;
    }

    //methods
    public Map<ResType,Integer> getContent(){
        return resources;
    }

    /**
     * adds an amount of a resource
     * @param resToAdd the resource
     * @param quantity the amount
     */
    public void add(ResType resToAdd, int quantity){
        quantity+=resources.get(resToAdd);
        resources.put(resToAdd,quantity);
    }

    /**
     * removes an amount of a resource
     * @param resToRem the resource
     * @param quantity the amount
     */
    public void remove(ResType resToRem, int quantity){
        quantity-=resources.get(resToRem);
        //if(quantity<0)  quantity=0;
        resources.put(resToRem,quantity);
    }
}