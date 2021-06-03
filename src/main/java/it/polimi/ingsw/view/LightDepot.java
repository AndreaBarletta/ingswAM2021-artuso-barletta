package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResType;

public class LightDepot {
    public ResType resource;
    public int counter;
    public int capacity;

    public LightDepot(ResType resource,int counter,int capacity){
        this.resource=resource;
        this.counter=counter;
        this.capacity=capacity;
    }

    public ResType getResource() {
        return resource;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        if(counter==0)
            return "Empty";

        StringBuilder depotToString= new StringBuilder();
        for(int i=0;i<counter;i++){
            depotToString.append(resource.getSymbol()).append(" ");
        }

        return depotToString.toString();
    }
}
