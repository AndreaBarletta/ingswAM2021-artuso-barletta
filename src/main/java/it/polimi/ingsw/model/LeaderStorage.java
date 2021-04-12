package it.polimi.ingsw.model;

public class LeaderStorage extends LeaderCard {
    //attributes
    private final ResType resources;
    private int counter;
    private final static int capacity = 2;

    //constructor
    public LeaderStorage(ResType resources) {
        this.resources=resources;
    }

    //methods

    public void activateAbility(PersonalBoard personalBoard){
    }

    private boolean checkSpace(int quantity){
        return quantity < capacity;
    }

    private boolean checkResType(ResType resource){
        return resources == resource;
    }

    private void removeFromStorage(ResType resource, int quantity){
        if(checkResType(resource))
            if(checkSpace(quantity))
                counter+=quantity;
    }

    public int getCounter(){
        return counter;
    }
}