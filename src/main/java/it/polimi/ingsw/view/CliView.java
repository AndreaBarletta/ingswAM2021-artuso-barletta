package it.polimi.ingsw.view;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.exceptions.DuplicatedIdException;
import it.polimi.ingsw.exceptions.NotEnoughArgumentsException;
import it.polimi.ingsw.exceptions.UnknownCommandException;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCardDeserializer;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class CliView{
    static Socket clientSocket;
    static final int defaultPortNumber=4545;
    static private PrintWriter out;
    static private BufferedReader in;
    static private LeaderCard[] leaderCards;
    static private List<DevelopmentCard> developmentCards;
    static private String playerName;

    static private CommandParser commandParser=new CommandParser();

    public static void main(String[] args){
        if(!loadLeaderCardsFromFile("src/main/resources/leaderCards.json")){
            return;
        }

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
            System.out.println("Error while opening client socket");
        }

        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.print("Error while creating buffers");
        }
        try{
            commandParser.addCommand("playername",1,MessageType.PICK_PLAYERNAME);
            commandParser.addCommand("creategame",2,MessageType.CREATE_GAME);
        }catch(DuplicatedIdException e){
            System.out.println("Error adding commands");
            return;
        }


        new Thread(CliView::sendToServer).start();
        new Thread(CliView::receiveFromServer).start();
    }

    public static void sendToServer(){
        String input;
        Scanner inConsole = new Scanner(System.in);
        Message message;
        while(true){
            try {
                input = inConsole.nextLine();
            } catch (Exception e) {
                return;
            }
            try {
                message=commandParser.parseCommand(input);
                out.println(message);
            }catch(UnknownCommandException e){
                System.out.println("Unknown command");
            }catch(NotEnoughArgumentsException e){
                System.out.println("Not Enough Arguments! Required "+commandParser.getNumberOfArguments(input));
            }
        }
    }

    public static void receiveFromServer(){
        Gson gson=new Gson();
        Message message;
        while(true){
            try {
                message = gson.fromJson(in.readLine(), Message.class);
                switch(message.messageType){
                    case NEW_PLAYER:
                        System.out.println("New player has joined");
                    case ERROR:
                        System.out.println("Error: "+message.params[0]);
                }
            }catch(Exception e){}
        }
    }

    /**
     * Loads the leader cards from a json file
     * @param path Path of the json file containing the list of leader cards
     * @return Whether or not the leader cards were loaded successfully
     */
    public static boolean loadLeaderCardsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading leader cards i.e. wrong path");
            return false;
        }

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LeaderCard.class,new LeaderCardDeserializer());
        Gson gson=gsonBuilder.create();
        try{
            leaderCards=gson.fromJson(content, LeaderCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            return false;
        }

        return true;
    }
}
