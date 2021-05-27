package it.polimi.ingsw.view;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.exceptions.DuplicatedIdException;
import it.polimi.ingsw.exceptions.IncorrectAmountArgumentsException;
import it.polimi.ingsw.exceptions.UnknownCommandException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.PersonalBoard.LeaderCard.LeaderCard;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliView{
    static Socket clientSocket;
    static final int defaultPortNumber=4545;
    static private PrintWriter out;
    static private BufferedReader in;
    static private LeaderCard[] leaderCardDeck;
    static private DevelopmentCard[] developmentCardDeck;
    static private int[][] developmentCardGrid;
    static private LightMarket market;
    static private String playerName;
    static private CommandParser commandParser=new CommandParser();
    static private List<LightPersonalBoard> lightPersonalBoards;

    public static void main(String[] args){
        if(!loadLeaderCardsFromFile("src/main/resources/leaderCards.json")){
            return;
        }
        if(!loadDevelopmentCardsFromFile("src/main/resources/developmentCards.json")){
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
            commandParser.addCommand("numberofplayers",1,MessageType.NUMBER_OF_PLAYERS);
            commandParser.addCommand("chooseleaders",2,MessageType.CHOOSE_LEADER_CARDS);
            commandParser.addCommand("initialresource",1,MessageType.CHOOSE_INITIAL_RESOURCES);
            commandParser.addCommand("leaderskip",0,MessageType.LEADER_ACTION_SKIP);
            commandParser.addCommand("leaderactivate",1,MessageType.LEADER_ACTION_ACTIVATE);
            commandParser.addCommand("leaderdiscard",1,MessageType.LEADER_ACTION_DISCARD);
            commandParser.addCommand("visitmarket",0,MessageType.SHOW_MARKET);
            commandParser.addCommand("chooseresources", 2, MessageType.CHOOSE_RESOURCES);
            commandParser.addCommand("buydevcard",0,MessageType.BUY_DEV_CARD);
            commandParser.addCommand("choosedevcard",1,MessageType.CHOOSE_DEV_CARD);
            commandParser.addCommand("activateproductions",0,MessageType.ACTIVATE_PRODUCTIONS);
            commandParser.addCommand("activate",1,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("cancel",0,MessageType.CANCEL);
        }catch(DuplicatedIdException e){
            System.out.println("Error adding commands");
            return;
        }

        lightPersonalBoards=new ArrayList<>();
        System.out.println(Colors.YELLOW.escape()+"Welcome to Masters of Renaissance"+Colors.RESET.escape());
        System.out.println("Insert your player name");
        System.out.print("(playername {name}): ");

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
            }catch(IncorrectAmountArgumentsException e){
                String output="Incorrect amount of Arguments! Required ";
                for(Integer i: commandParser.getNumberOfArguments(input)){
                    output+=i.toString()+" ";
                }
                System.out.println(output);
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
                    case SET_PLAYERNAME:
                        playerName=message.params[0];
                        lightPersonalBoards.add(new LightPersonalBoard(playerName));
                        break;
                    case NEW_PLAYER:
                        System.out.println("Player \""+message.params[0]+"\" has joined");
                        lightPersonalBoards.add(new LightPersonalBoard(message.params[0]));
                        break;
                    case ERROR:
                        System.out.println("Error: "+message.params[0]);
                        break;
                    case WAIT_FOR_OTHER_PLAYERS:
                        System.out.println("Waiting for other players");
                        break;
                    case ASK_NUMBER_OF_PLAYERS:
                        System.out.println("Insert the number of players");
                        System.out.print("(numberofplayers {2/3/4}): ");
                        break;
                    case GAME_CREATED:
                        System.out.println("A new game has been created");
                        break;
                    case GAME_JOINED:
                        System.out.print(message.params.length+" player already in the game:");
                        for(String s:message.params){
                            System.out.print(" "+s);
                            lightPersonalBoards.add(new LightPersonalBoard(s));
                        }
                        System.out.print("\n");
                        break;
                    case GAME_STARTED:
                        System.out.println("Game has started");
                        break;
                    case SET_DEV_CARD_GRID:
                        developmentCardGrid=gson.fromJson(message.params[0],int[][].class);
                        break;
                    case SET_MARKET:
                        market=new LightMarket(gson.fromJson(message.params[0], ResType[][].class),ResType.valueOf(message.params[1]));
                        break;
                    case SHOW_LEADER_CARDS:
                        System.out.println("Pick 2 between the following leader cards: ");
                        for(String s: message.params){
                            System.out.println(leaderCardDeck[Integer.parseInt(s)].toString());
                        }
                        System.out.print("(chooseleaders {id1} {id2}): ");
                        break;
                    case LEADER_CARDS_CHOSEN:
                        System.out.println("Player "+message.params[0]+" has chosen their leader cards");
                        break;
                    case INKWELL_GIVEN:
                        System.out.println("Player \""+message.params[0]+"\" has received the inkwell");
                        System.out.print("The turn order is the following:");
                        for(String s:message.params){
                            System.out.print(" "+s);
                        }
                        System.out.print("\n");
                        break;
                    case ASK_INITIAL_RESOURCES:
                        System.out.println("What initial resource do you want to obtain?");
                        System.out.print("(initialresource {COIN/STONE/SERVANT/SHIELD}): ");
                        break;
                    case CHOOSE_INITIAL_RESOURCES:
                        System.out.println("Player "+message.params[0]+" has received "+message.params[1]+" as initial resource");
                        break;
                    case INCREMENT_FAITH_TRACK:
                        System.out.println("Player "+message.params[0]+" has advanced "+message.params[1]+" spaces in the faith track");
                        break;
                    case WAIT_YOUR_TURN:
                        System.out.println("Wait for your turn to begin");
                        break;
                    case TURN_START:
                        System.out.println("Player "+message.params[0]+" has started their turn");
                        break;
                    case ASK_LEADER_ACTION:
                        System.out.println("You have the following leader cards:");
                        for(String s: message.params){
                            System.out.println(leaderCardDeck[Integer.parseInt(s)].toString());
                        }
                        System.out.print("What leader action do you want to play? (leaderskip/leaderactivate {id}/leaderdiscard {id}): ");
                        break;
                    case LEADER_ACTIVATED:
                        for(LightPersonalBoard lp : lightPersonalBoards) {
                            if(lp.getPlayerName().equals(message.params[0])) {
                                leaderCardDeck[Integer.parseInt(message.params[1])].effectOnActivate(lp);
                            }
                        }
                        break;
                    case LEADER_DISCARDED:
                        System.out.println("Player "+message.params[0]+" has discarded leader card "+message.params[1]);
                        break;
                    case LEADER_SKIPPED:
                        System.out.println("Player "+message.params[0]+" has skipped the leader action");
                        break;
                    case ASK_TURN_ACTION:
                        System.out.print("Choose a turn action (visitmarket/activateproductions/buydevcard): ");
                        break;
                    case TURN_CHOICE:
                        System.out.println("Player "+message.params[0]+" has chosen to "+message.params[1]);
                        break;
                    case SHOW_PRODUCTIONS:
                        for(LightPersonalBoard lp: lightPersonalBoards) {
                            if (lp.getPlayerName().equals(playerName)) {
                                //base production
                                System.out.println("Base production ID 0:\n"+lp.getBaseProduction());
                                //production from Dev Card
                                int i = 1;
                                for(int devId : lp.getDevelopmentCardSlots()) {
                                    if(devId!=-1)
                                        System.out.println("Dev Card production ID "+i+":\n"+developmentCardDeck[devId].getProduction());
                                    i++;
                                }
                                //production from Leader Card
                                for(Production p : lp.getLeaderProductions()) {
                                    System.out.println("Leader Card production ID "+i+":\n"+p);
                                    i++;
                                }
                                break;
                            }
                        }
                        System.out.print("Choose which productions to activate (activate {id}) or go back (cancel): ");
                        break;
                    case UPDATE_RESOURCES:
                        //TODO
                        break;
                    case SHOW_DEV_CARD_GRID:
                        for(int[] row:developmentCardGrid){
                            for(int id:row){
                                System.out.println(developmentCardDeck[id]);
                            }
                        }
                        System.out.print("Choose a development card to buy (choosedevcard {id}) or go back (cancel): ");
                        break;
                    case UPDATE_DEV_CARD_GRID:
                        developmentCardGrid[Integer.parseInt(message.params[0])][Integer.parseInt(message.params[1])]=Integer.parseInt(message.params[2]);
                        break;
                    case ASK_DEV_CARD_SLOT:
                        System.out.print("Choose a development card to buy (choosedevcard {id})");
                        break;
                    case SHOW_MARKET:
                        System.out.print(market+"Choose a row (row {0/1/2}) or a column (column {0/1/2/3}): ");
                        break;
                    case CHOOSE_RESOURCES:
                        System.out.println("Player "+message.params[0]+"has acquired resources to the market. The market has been updated");
                    case DISCONNECTED:
                        System.out.println("Player "+message.params[0]+" has disconnected ");
                        break;
                }
            }catch(Exception e){
                System.out.println("Exception while receiving message from server");
                break;
            }
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
            leaderCardDeck=gson.fromJson(content, LeaderCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error loading json file for leader cards");
            return false;
        }

        return true;
    }

    /**
     * Loads the leader cards from a json file
     * @param path Path of the json file containing the list of the development cards
     * @return Whether or not the development cards were loaded successfully
     */
    public static boolean loadDevelopmentCardsFromFile(String path){
        String content;

        File file=new File(path);
        try{
            content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Error reading from file while loading development cards i.e. wrong path");
            return false;
        }

        Gson gson=new Gson();
        try{
            developmentCardDeck=gson.fromJson(content, DevelopmentCard[].class);
        }catch(JsonSyntaxException e){
            System.out.println("Error parsing json file for development cards");
            return false;
        }

        return true;
    }
}
