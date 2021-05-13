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
            out.println(new Message(MessageType.OK,new String[]{"true"}));
            String messageString=in.readLine();
            Gson gson=new Gson();
            while(messageString!=null){
                System.out.println(messageString);
                Message message=gson.fromJson(messageString,Message.class);
                switch(message.messageType){
                    case CONNECT:
                        System.out.println("Player "+message.params[0]+" has connected");
                        this.playerName=message.params[0];
                        out.println(new Message(MessageType.OK,new String[]{"true"}));
                        break;
                    case CREATEGAME:
                        if(message.params.length==2){
                            if(games.get(message.params[0])==null){
                                Controller newController=new Controller();
                                newController.createGame(this,message.params[0],Integer.valueOf(message.params[1]));
                                games.put(message.params[0],newController);
                                currentGame=newController;
                                out.println(new Message(MessageType.OK,new String[]{"false","Waiting for other players..."}));
                            }else{
                                //Game with the same name already created
                                send(new Message(MessageType.ERROR,new String[]{"Game with the same name already exists"}));
                            }
                        }else{
                            //Too few arguments
                            send(new Message(MessageType.NOTENOUGHARGUMENTS,new String[]{}));
                        }
                        break;
                    case JOINGAME:
                        Controller game=games.get(message.params[0]);
                        if(game!=null){
                            out.println(new Message(MessageType.OK,new String[]{"false","Waiting for other players..."}));
                            game.joinGame(this);
                            currentGame=game;
                        }else{
                            out.println(new Message(MessageType.ERROR,new String[]{"Game does not exist"}));
                        }
                        break;
                }
                messageString=in.readLine();
            }
        }catch(Exception e){
            System.out.println("Error starting client handler for "+clientSocket.getInetAddress());
        }
        currentGame.disconnected(this);
    }

    public void send(Message message){
        System.out.println("Send message to player");
        out.println(message);
    }

    public String getPlayerName(){
        return playerName;
    }
}
