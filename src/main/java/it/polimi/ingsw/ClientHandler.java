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
    private Map<String, Controller> games;
    private String playerName;
    private Controller currentGame;

    public ClientHandler(Socket clientSocket,Map<String,Controller> games){
        this.clientSocket=clientSocket;
        this.games=games;
    }

    @Override
    public void run() {
        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String messageString=in.readLine();
            Gson gson=new Gson();
            while(messageString!=null){
                System.out.println(messageString);
                Message message=gson.fromJson(messageString,Message.class);
                switch(message.messageType){
                    case CREATEGAME:
                        if(message.params.length==2){
                            if(games.get(message.params[0])==null){
                                Controller newController=new Controller();
                                newController.createGame(this,message.params[0],Integer.valueOf(message.params[1]));
                                games.put(message.params[0],newController);
                                currentGame=newController;
                            }else{
                                //Game with the same name already created
                                //send(new Message);
                            }
                        }
                        break;
                    case JOINGAME:
                        Controller game=games.get(message.params[0]);
                        if(game!=null){
                            game.joinGame(this);
                            currentGame=game;
                        }
                        break;
                    case CONNECT:
                        System.out.println("Player "+message.params[0]+"has connected");
                        this.playerName=message.params[0];
                        break;
                }
                messageString=in.readLine();
            }
        }catch(Exception e){
            System.out.println("Error starting client handler for "+clientSocket.getInetAddress());
            return;
        }
    }

    public void send(Message message){
        System.out.println("Send message to player");
        out.println(message);
    }

    public String getPlayerName(){
        return playerName;
    }
}
