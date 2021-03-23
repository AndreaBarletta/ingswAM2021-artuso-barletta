package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Production {
    private Map<ResType,Integer> ingredients;
    private Map<ResType,Integer> product;

    public Production(){
        product=new HashMap<>();
        ingredients=new HashMap<>();
    }

    public Production(Map<ResType,Integer> ingredients,Map<ResType,Integer> product){
        this.ingredients=ingredients;
        this.product=product;
    }

    public Map<ResType, Integer> getIngredients() {
        return ingredients;
    }

    public Map<ResType, Integer> getProduct() {
        return product;
    }

    public void setIngredients(Map<ResType, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public void setProduct(Map<ResType, Integer> product) {
        this.product = product;
    }
}
