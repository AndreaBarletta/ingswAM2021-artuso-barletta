package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResType;

import java.util.Map;

public class LightStrongbox {
    private final Map<ResType,Integer> content;

    public LightStrongbox(Map<ResType,Integer> content){
        this.content=content;
    }

    public Map<ResType, Integer> getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder strongboxToString= new StringBuilder();
        if(content.isEmpty())
            return "Empty";
        for(Map.Entry<ResType,Integer> me:content.entrySet())
            strongboxToString.append(me.getKey().getSymbol()).append("x").append(me.getValue()).append(" ");

        return strongboxToString.toString();
    }
}
