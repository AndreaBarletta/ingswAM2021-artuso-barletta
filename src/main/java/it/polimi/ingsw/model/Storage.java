package it.polimi.ingsw.model;

import java.util.AbstractMap;

public class Storage {
    private final int capacity;
    private int counter;
    private ResType storageResource;

    public Storage(int capacity){
        this.capacity=capacity;
        this.counter=0;
    }

    public void setStorageResource(ResType storageResource){
        this.storageResource=storageResource;
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
        return storageResource==resourceType;
    }

    private boolean checkSpace(int newCounter){
        return newCounter<=capacity;
    }

    public AbstractMap.SimpleEntry<ResType,Integer> getContent(){
        return new AbstractMap.SimpleEntry<>(storageResource, counter);
    }
}
