package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Production {
    private Map<ResType,Integer> ingredients;
    private Map<ResType,Integer> products;

    public Production(){
        products=new HashMap<>();
        ingredients=new HashMap<>();
    }

    public Production(Map<ResType,Integer> ingredients,Map<ResType,Integer> products){
        this.ingredients=ingredients;
        this.products=products;
    }

    public Map<ResType, Integer> getIngredients() {
        return ingredients;
    }

    public Map<ResType, Integer> getProduct() {
        return products;
    }
}
