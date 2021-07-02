package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import it.polimi.ingsw.controller.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private final GameStateAutomaton automaton;
    private final Controller controller;

    public ClientHandler(Socket clientSocket,Controller controller){
        this.clientSocket=clientSocket;
        this.controller=controller;
        automaton=new GameStateAutomaton(controller,this);
    }

    @Override
    public void run() {
        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Gson gson=new Gson();
            String incomingString;
            incomingString=in.readLine();
            System.out.println(incomingString);
            while(incomingString!=null){
                Message incomingMessage=gson.fromJson(incomingString,Message.class);
                if(!automaton.evolve(incomingMessage.messageType.toString(), incomingMessage.params)){
                    send(new Message(MessageType.ERROR,new String[]{automaton.getErrorMessage()}));
                }
                incomingString=in.readLine();
            }
        }catch(Exception e){
            controller.disconnected(this);
            System.out.println("Exception in client "+e.getStackTrace()+clientSocket.getInetAddress());         //player disconnected?
        }
    }

    public void send(Message message){
        System.out.println("Send message to player");
        out.println(message);
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameStateAutomaton getAutomaton(){
        return automaton;
    }
}
