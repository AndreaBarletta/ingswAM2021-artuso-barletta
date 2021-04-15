package it.polimi.ingsw.model.PersonalBoard;

import it.polimi.ingsw.model.ResType;

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

    public void setIngredients(Map<ResType, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<ResType, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<ResType, Integer> products) {
        this.products = products;
    }
}
