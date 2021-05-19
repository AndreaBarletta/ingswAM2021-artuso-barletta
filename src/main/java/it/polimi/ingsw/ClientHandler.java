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
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private GameStateAutomaton automaton;
    private Controller controller;

    public ClientHandler(Socket clientSocket,Controller controller){
        this.clientSocket=clientSocket;
        this.controller=controller;
        automaton=new GameStateAutomaton();
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
                Message incomingMesage=gson.fromJson(incomingString,Message.class);
                if(!automaton.evolve(controller,this,incomingMesage)){
                    send(new Message(MessageType.ERROR,new String[]{automaton.getErrorMessage()}));
                }
                incomingString=in.readLine();
            }
        }catch(Exception e){
            System.out.println("Error starting client handler for "+clientSocket.getInetAddress());         //player disconnected?
        }
    }

    public void send(Message message){
        System.out.println("Send message to player");
        out.println(message);
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName=playerName;
    }
}
