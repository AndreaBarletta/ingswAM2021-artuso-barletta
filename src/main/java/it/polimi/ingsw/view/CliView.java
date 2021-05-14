package it.polimi.ingsw.view;
import com.google.gson.Gson;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.controller.ControllerEventListener;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliView{
    static Socket clientSocket;
    static final int defaultPortNumber=4545;
    static private PrintWriter out;
    static private BufferedReader in;
    static private List<LeaderCard> leaderCards;
    static private List<DevelopmentCard> developmentCards;
    static private String playerName;
    static private String input;
    static private Scanner inConsole = new Scanner(System.in);
    static private String[] inputSplit;
    static private boolean canWrite=false;
    static private boolean isValid=false;
    static private Gson gson=new Gson();
    static private Message message;

    public static void main(String[] args){
        //Args[0]=server ip
        //Args[1]=server port
        int serverPort;
        if(args.length==1){
            serverPort=defaultPortNumber;
        }else if(args.length==2){
            serverPort=Integer.parseInt(args[1]);
        }else{
            return;
        }

        try{
            clientSocket=new Socket(args[0],serverPort);
        }catch(Exception e){
            System.out.print("Error while opening client socket");
        }

        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.print("Error while creating buffers");
        }
        while(true){
            sendToServer();
            recieveFromServer();
        }
    }

    public static void sendToServer(){
        while(canWrite&&!isValid) {
            System.out.print("Enter command: ");
            try {
                input = inConsole.nextLine();
            } catch (Exception e) {
                return;
            }
            inputSplit = input.split(" ");
            switch (inputSplit[0]) {
                case "connect":
                    if (inputSplit.length == 2) {
                        out.println(new Message(MessageType.CONNECT, new String[]{inputSplit[1]}));
                        playerName=inputSplit[1];
                        isValid=true;
                    } else {
                        System.out.println("Invalid number of arguments, required 1");
                    }
                    break;
                case "creategame":
                    if (inputSplit.length == 3) {
                        out.println(new Message(MessageType.CREATEGAME, new String[]{inputSplit[1], inputSplit[2]}));
                        isValid=true;
                    } else {
                        System.out.println("Invalid number of arguments, required 2");
                    }
                    break;
                case "joingame":
                    if (inputSplit.length == 2) {
                        out.println(new Message(MessageType.JOINGAME, new String[]{inputSplit[1]}));
                        isValid=true;
                    } else {
                        System.out.println("Invalid number of arguments, required 1");
                    }
                    break;
                    //TODO: fix leader cards picking
                /*case "chooseleaders":
                    if(inputSplit.length==3){
                        out.println(new Message(MessageType.LEADERCARDSCHOSEN,new String[]{inputSplit[1],inputSplit[2]}));
                        isValid=true;
                    }else{
                        System.out.println("Invalid number of arguments, required 2");
                    }
                    break;*/
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
        isValid=false;
    }
    public static void recieveFromServer(){
        try{
            message=gson.fromJson(in.readLine(),Message.class);
            switch(message.messageType){
                case OK:
                    if(message.params.length==1){
                        canWrite=Boolean.parseBoolean(message.params[0]);
                    }else if(message.params.length==2){
                        canWrite=Boolean.parseBoolean(message.params[0]);
                        System.out.println(message.params[1]);
                    }
                    break;
                case NEWPLAYER:
                    System.out.println("Player "+message.params[0]+" has joined the game");
                    break;
                case DISCONNECTED:
                    System.out.println("Player "+message.params[0]+" has left the game");
                    break;
                case ERROR:
                    System.out.println("Error: "+message.params[0]);
                    canWrite=true;
                    break;
                case STARTGAME:
                    System.out.println("Game has started");
                    break;
                case INKWELLGIVEN:
                    System.out.println("Player "+ message.params[0]+" has recieved the inkwell");
                    break;
                case CHOOSELEADERCARDS:
                    System.out.println("Leader cards with ids "+message.params[0]);
                    break;
            }
        }catch(Exception e){}
    }
}
