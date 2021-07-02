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

    /**
     * Create a production given existing ingredients and products, used to create copies of productions
     * @param ingredients Ingredients
     * @param products Products
     */
    public Production(Map<ResType,Integer> ingredients,Map<ResType,Integer> products){
        this.ingredients=new HashMap<>(ingredients);
        this.products=new HashMap<>(products);
    }

    public Map<ResType, Integer> getIngredients() {
        return ingredients;
    }

    public Map<ResType, Integer> getProducts() {
        return products;
    }

    @Override
    public String toString(){
        StringBuilder productionToString= new StringBuilder("Ingredients: ");
        for(Map.Entry<ResType,Integer> me:ingredients.entrySet()) productionToString.append(me.getKey().getSymbol()).append("=").append(me.getValue()).append(" ");
        productionToString.append("\n");
        productionToString.append("Products: ");
        for(Map.Entry<ResType,Integer> me:products.entrySet()) productionToString.append(me.getKey().getSymbol()).append("=").append(me.getValue()).append(" ");
        return productionToString.toString();
    }
}
