package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.glass.ui.Application;
import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.ResType;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.gui.GuiView;
import javafx.scene.effect.Light;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Client {
    static View view;
    static Socket clientSocket;
    static final int defaultPortNumber=4545;
    static private PrintWriter out;
    static private BufferedReader in;

    /**
     * Run the client
     * @param args args[0]=gui/cli, args[1]=server ip, args[2]=server port (optional)
     */
    public static void main(String[] args){
        if(args.length!=2&&args.length!=3){
            System.out.println("Invalid number of arguments");
            return;
        }

        try{
            clientSocket=new Socket(args[1],args.length==2?defaultPortNumber:Integer.parseInt(args[2]));
        }catch(Exception e){
            System.out.println("Error while opening client socket");
            return;
        }

        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.print("Error while creating buffers");
        }

        if(args[0].equals("cli")){
            view=new CliView();
        }else if(args[0].equals("gui")){
            view=new GuiView();
        }else{
            System.out.println("Select gui or cli");
            return;
        }

        if(!view.lightModel.loadResources())
            return;


        Thread viewThread=new Thread(view);
        viewThread.setDaemon(true);
        view.setOutPrintWriter(out);
        viewThread.start();

        Thread receiveThread=new Thread(Client::receiveFromServer);
        receiveThread.start();
        boolean ready=view.isReady();
        while(!ready){
            try{
                Thread.sleep(10);
            }catch(Exception e){}
            ready=view.isReady();
        }
        view=view.getRunningView();
    }

    public static void receiveFromServer(){
        Gson gson=new Gson();
        Message message=null;
        boolean end=false;
        while(!end){
            try {
                message = gson.fromJson(in.readLine(), Message.class);
            }catch(Exception e){
                System.out.println("\nException while receiving message from server");
                break;
            }
            switch(message.messageType){
                case SET_PLAYERNAME:
                    view.setPlayerName(message.params[0]);
                    break;
                case NEW_PLAYER:
                    view.newPlayerConnected(message.params[0]);
                    break;
                case ERROR:
                    view.error(message.params[0]);
                    break;
                case WAIT_FOR_OTHER_PLAYERS:
                    view.waitForOtherPlayers();
                    break;
                case ASK_NUMBER_OF_PLAYERS:
                    view.askForNumberOfPlayers();
                    break;
                case GAME_CREATED:
                    view.gameCreated();
                    break;
                case GAME_JOINED:
                    view.gameJoined(message.params);
                    break;
                case GAME_STARTED:
                    view.gameStarted();
                    break;
                case SET_DEV_CARD_GRID:
                    view.setDevCardGrid(gson.fromJson(message.params[0],int[][].class));
                    break;
                case SET_MARKET:
                    view.setMarket(new LightMarket(gson.fromJson(message.params[0], ResType[][].class),ResType.valueOf(message.params[1])));
                    break;
                case SHOW_LEADER_CARDS:
                    view.showInitialLeaderCards(message.params);
                    break;
                case LEADER_CARDS_CHOSEN:
                    view.leaderCardsChosen(message.params[0],
                            Arrays.asList(message.params).subList(1,message.params.length)
                                    .stream().mapToInt(Integer::parseInt)
                                    .toArray()
                    );
                    break;
                case INKWELL_GIVEN:
                    view.inkwellGiven(message.params);
                    break;
                case ASK_INITIAL_RESOURCES:
                    view.askInitialResource();
                    break;
                case CHOOSE_INITIAL_RESOURCES:
                    view.initialResourcesChosen(message.params[0],ResType.valueOf(message.params[1]));
                    break;
                case INCREMENT_FAITH_TRACK:
                    view.incrementFaithTrack(message.params[0],Integer.parseInt(message.params[1]));
                    break;
                case WAIT_YOUR_TURN:
                    view.waitYourTurn();
                    break;
                case TURN_START:
                    view.turnStart(message.params[0]);
                    break;
                case ASK_LEADER_ACTION:
                    view.askLeaderAction();
                    break;
                case LEADER_ACTIVATED:
                    view.leaderActivate(message.params[0],Integer.parseInt(message.params[1]));
                    break;
                case LEADER_DISCARDED:
                    view.leaderDiscard(message.params[0],Integer.parseInt(message.params[1]));
                    break;
                case LEADER_SKIPPED:
                    view.leaderSkip(message.params[0]);
                    break;
                case ASK_TURN_ACTION:
                    view.askTurnAction();
                    break;
                case TURN_CHOICE:
                    view.turnChoice(message.params[0],message.params[1]);
                    break;
                case SHOW_PRODUCTIONS:
                    view.showProductions();
                    break;
                case SHOW_CHOSEN_PRODUCTIONS:
                    view.showChosenProductions(
                            message.params[0],
                            Arrays.asList(message.params).subList(1,message.params.length)
                                    .stream().mapToInt(Integer::parseInt)
                                    .toArray()
                    );
                    break;
                case UPDATE_RESOURCES:
                    LightDepot[] depots=gson.fromJson(message.params[1],LightDepot[].class);
                    LightDepot[] leaderDepots=gson.fromJson(message.params[2],LightDepot[].class);
                    LightStrongbox strongbox=gson.fromJson(message.params[3],LightStrongbox.class);
                    view.updateResources(message.params[0],depots,Arrays.asList(leaderDepots),strongbox);
                    break;
                case SHOW_DEV_CARD_GRID:
                    view.showDevCardGrid();
                    break;
                case UPDATE_DEV_CARD_GRID:
                    view.updateDevCardGrid(Integer.parseInt(message.params[0]),
                            CardType.valueOf(message.params[1]),
                            Integer.parseInt(message.params[2])
                    );
                    break;
                case ASK_DEV_CARD_SLOT:
                    view.askDevCardSlot();
                    break;
                case UPDATE_DEV_CARD_SLOT:
                    view.updateDevCardSlot(message.params[0],Integer.parseInt(message.params[1]),Integer.parseInt(message.params[2]));
                    break;
                case SHOW_MARKET:
                    view.showMarket();
                    break;
                case UPDATE_MARKET:
                    view.updateMarket(message.params[0].equals("row"),Integer.parseInt(message.params[1]));
                    break;
                case ASK_CONVERT_RESOURCE:
                    view.askResourceConvert(Integer.valueOf(message.params[0]));
                    break;
                case ASK_DISCARD_RESOURCE:
                    view.askResourceDiscard(gson.fromJson(message.params[0],ResType[].class));
                    break;
                case RESOURCE_DISCARDED:
                    view.resourceDiscarded(message.params[0],ResType.valueOf(message.params[1]));
                    break;
                case LAST_TURNS:
                    view.lastTurn();
                    break;
                case WINNER:
                    view.announceWinner(message.params[0]);
                    end=true;
                    break;
                case VATICAN_REPORT_RESULTS:
                    Type mapType=new TypeToken<Map<String,Boolean>>(){}.getType();
                    view.vaticanReportResults(gson.fromJson(message.params[0],mapType));
                    break;
                case LORENZO_DISCARD:
                    view.lorenzoDiscard(message.params[0]);
                    break;
                case LORENZO_INCREMENT_FAITH_TRACK:
                    view.lorenzoIncrementFaithTrack(Integer.parseInt(message.params[0]));
                    break;
                case LORENZO_SHUFFLE:
                    view.lorenzoShuffle();
                case LORENZO_WON:
                    view.lorenzoWon();
                    end=true;
                    break;
                case DISCONNECTED:
                    System.out.println("Player "+message.params[0]+" has disconnected ");
                    break;
            }
        }
        if(!end){
            System.out.println("Disconnected from server");
        }
    }
}
