package it.polimi.ingsw;

import com.google.gson.Gson;

public class Message {
    public MessageType messageType;
    public String[] params;

    public Message(MessageType messageType,String[] params){
        this.messageType=messageType;
        this.params=params;
    }

    @Override
    public String toString() {
        Gson gson=new Gson();
        return gson.toJson(this);
    }
}
