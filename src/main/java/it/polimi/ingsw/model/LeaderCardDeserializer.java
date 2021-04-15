package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class LeaderCardDeserializer implements JsonDeserializer<LeaderCard> {
    @Override
    public LeaderCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = json.getAsJsonObject().get("type").getAsString();
        switch(type) {
            case "depot":
                return context.deserialize(json, LeaderDepot.class);
            case "discounter":
                return context.deserialize(json, LeaderDevCardDiscounter.class);
            case "producer":
                return context.deserialize(json, LeaderProduction.class);
            case "converter":
                return context.deserialize(json, LeaderWhiteMarbleConverter.class);
            default:
                throw new IllegalArgumentException("Unknown leader cart type");
        }
    }
}