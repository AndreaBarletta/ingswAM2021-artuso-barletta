package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.model.exceptions.NegQuantityException;

import java.util.HashMap;
import java.util.Map;

public class Strongbox {
    //attributes
    private Map<ResType,Integer> resources;

    //constructor
    public Strongbox(){
        resources=new HashMap<>();
    }

    //methods
    public Map<ResType,Integer> getContent(){
        return resources;
    }

    /**
     * adds an amount of a resource
     * @param newResources Resources to be added to the strongbox
     */
    public void add(Map<ResType,Integer> newResources){
        newResources.forEach((k,v)->resources.merge(k,v,Integer::sum));
    }

    /**
     * removes an amount of a resource
     * @param toRemove Resources to remove from the strongbox
     */
    public void remove(Map<ResType,Integer> toRemove) throws NegQuantityException {
        HashMap<ResType,Integer> tempResources=new HashMap<>(resources);
        for (Map.Entry<ResType, Integer> entry : toRemove.entrySet()) {
            ResType k = entry.getKey();
            Integer v = entry.getValue();
            resources.merge(k, -v, Integer::sum);
            if (resources.get(k) < 0) {
                resources = new HashMap<>(tempResources);
                throw new NegQuantityException();
            }
        }
    }
}