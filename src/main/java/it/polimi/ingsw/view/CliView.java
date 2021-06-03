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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class CliView implements View,Runnable{
    private final CommandParser commandParser;
    private final PrintWriter out;

    public CliView(PrintWriter out){
        this.out=out;
        commandParser=new CommandParser();
    }

    @Override
    public void run(){
        try{
            commandParser.addCommand("playername",1,MessageType.PICK_PLAYERNAME);
            commandParser.addCommand("numberofplayers",1,MessageType.NUMBER_OF_PLAYERS);
            commandParser.addCommand("chooseleaders",2,MessageType.CHOOSE_LEADER_CARDS);
            commandParser.addCommand("initialresource",1,MessageType.CHOOSE_INITIAL_RESOURCES);
            commandParser.addCommand("leaderskip",0,MessageType.LEADER_ACTION_SKIP);
            commandParser.addCommand("leaderactivate",1,MessageType.LEADER_ACTION_ACTIVATE);
            commandParser.addCommand("leaderdiscard",1,MessageType.LEADER_ACTION_DISCARD);
            commandParser.addCommand("visitmarket",0,MessageType.SHOW_MARKET);
            commandParser.addCommand("choose", 2, MessageType.CHOOSE_ROW_OR_COLUMN);
            commandParser.addCommand("buydevcard",0,MessageType.BUY_DEV_CARD);
            commandParser.addCommand("choosedevcard",1,MessageType.CHOOSE_DEV_CARD);
            commandParser.addCommand("choosedevcard",2,MessageType.CHOOSE_DEV_CARD);
            commandParser.addCommand("choosedevcard",3,MessageType.CHOOSE_DEV_CARD);
            commandParser.addCommand("chooseslot",1,MessageType.CHOOSE_DEV_CARD_SLOT);
            commandParser.addCommand("activateproductions",0,MessageType.ACTIVATE_PRODUCTIONS);
            commandParser.addCommand("activate",1,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("activate",2,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("activate",3,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("activate",4,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("activate",5,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("activate",6,MessageType.CHOOSE_PRODUCTIONS);
            commandParser.addCommand("cancel",0,MessageType.CANCEL);
        }catch(DuplicatedIdException e){
            System.out.println("Error adding commands");
            return;
        }

        System.out.println(Colors.YELLOW.escape()+"Welcome to Masters of Renaissance"+Colors.RESET.escape());
        System.out.println("Insert your player name");
        System.out.print("(playername {name}): ");

        sendToServer();
    }

    private void sendToServer(){
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

    private LightPersonalBoard LBPByName(String playerName){
        for(LightPersonalBoard lpb:lightModel.getLightPersonalBoards()){
            if(lpb.getPlayerName().equals(playerName)){
                return lpb;
            }
        }
        return null;
    }

    @Override
    public void setPlayerName(String playerName) {
        lightModel.setPlayerName(playerName);
        lightModel.getLightPersonalBoards().add(new LightPersonalBoard(playerName));
    }

    @Override
    public void newPlayerConnected(String newPlayerName) {
        System.out.println("Player \""+newPlayerName+"\" has joined");
        lightModel.getLightPersonalBoards().add(new LightPersonalBoard(newPlayerName));
    }

    @Override
    public void error(String errorMessage) {
        System.out.println("Error: "+errorMessage);
    }

    @Override
    public void waitForOtherPlayers() {
        System.out.println("Waiting for other players");
    }

    @Override
    public void askForNumberOfPlayers() {
        System.out.print("Insert the number of players (numberofplayers {2/3/4}): ");
    }

    @Override
    public void gameCreated() {
        System.out.println("A new game has been created");
    }

    @Override
    public void gameJoined(String[] playerNames) {
        System.out.print(playerNames.length+" player already in the game:");
        for(String s:playerNames){
            System.out.print(" "+s);
            lightModel.getLightPersonalBoards().add(new LightPersonalBoard(s));
        }
        System.out.print("\n");
    }

    @Override
    public void gameStarted() {
        System.out.println("Game has started");
    }

    @Override
    public void setDevCardGrid(int[][] devCardGridIds) {
        lightModel.setDevelopmentCardGrid(devCardGridIds);
    }

    @Override
    public void setMarket(LightMarket lightMarket) {
        lightModel.setLightMarket(lightMarket);
    }

    @Override
    public void showInitialLeaderCards(String[] leaderCardsIds) {
        System.out.println("Pick 2 between the following leader cards: ");
        for(String s: leaderCardsIds){
            System.out.println(lightModel.getLeaderCardDeck()[Integer.parseInt(s)].toString());
            System.out.print("\n");
        }
        System.out.print("(chooseleaders {id1} {id2}): ");
    }

    @Override
    public void leaderCardsChosen(String playerName,int[] ids) {
        System.out.println("Player "+playerName+" has chosen their leader cards");
        LBPByName(playerName).setLeaderCards(Arrays.stream(ids).boxed().collect(Collectors.toList()));
    }

    @Override
    public void inkwellGiven(String[] playerNamesOrdered) {
        LBPByName(playerNamesOrdered[0]).setInkwell(true);
        System.out.println("Player \""+playerNamesOrdered[0]+"\" has received the inkwell");
        System.out.print("The turn order is the following:");
        for(String s:playerNamesOrdered){
            System.out.print(" "+s);
        }
        System.out.print("\n");
    }

    @Override
    public void askInitialResource() {
        System.out.print("What initial resource do you want to obtain? (initialresource {COIN/STONE/SERVANT/SHIELD}): ");
    }

    @Override
    public void initialResourcesChosen(String playerName,ResType resource) {
        System.out.println("Player "+playerName+" has received "+resource+" as initial resource");
    }

    @Override
    public void incrementFaithTrack(String playerName, int increment) {
        System.out.println("Player "+playerName+" has advanced "+increment+" spaces in the faith track");
    }

    @Override
    public void waitYourTurn() {
        System.out.println("Wait for your turn to begin");
    }

    @Override
    public void turnStart(String playerName) {
        System.out.println("Player "+playerName+" has started their turn");
    }

    @Override
    public void askLeaderAction() {
        LightPersonalBoard lpb=LBPByName(lightModel.getPlayerName());
        System.out.println("You have the following leader cards:");
        for (int id : lpb.getLeaderCards()) {
            System.out.println(lightModel.getLeaderCardDeck()[id].toString());
            System.out.print("\n");
        }
        System.out.print("What leader action do you want to play? (leaderskip/leaderactivate {id}/leaderdiscard {id}): ");
    }

    @Override
    public void leaderActivate(String playerName,int leaderCardId) {
        LightPersonalBoard lpb=LBPByName(playerName);
        lightModel.getLeaderCardDeck()[leaderCardId].effectOnActivate(lpb);
        lightModel.getLeaderCardDeck()[leaderCardId].activate();
        System.out.println("Player "+playerName+" has activated one of their leader cards:");
        System.out.println(lightModel.getLeaderCardDeck()[leaderCardId]);
        System.out.print("\n");
    }

    @Override
    public void leaderDiscard(String playerName,int leaderCardId) {
        LightPersonalBoard lpb=LBPByName(playerName);
        lightModel.getLeaderCardDeck()[leaderCardId].effectOnDiscard(lpb);
        lpb.discardLeaderCard(leaderCardId);
        System.out.println("Player "+playerName+" has discarded one of their leader cards:");
        System.out.println(lightModel.getLeaderCardDeck()[leaderCardId]);
    }

    @Override
    public void leaderSkip(String playerName) {
        System.out.println("Player "+playerName+" has skipped the leader action");
    }

    @Override
    public void askTurnAction() {
        System.out.print("Choose a turn action (visitmarket/activateproductions/buydevcard): ");
    }

    @Override
    public void turnChoice(String playerName,String choice) {
        System.out.println("Player "+playerName+" has chosen to "+choice);
    }

    @Override
    public void showProductions() {
        LightPersonalBoard lpb=LBPByName(lightModel.getPlayerName());
        System.out.println("You have the following productions:");
        //base production
        System.out.println("Base production ID 0:\n"+lpb.getBaseProduction());
        //production from Dev Card
        int i = 1;
        for(Stack<Integer> devStack : lpb.getDevelopmentCardSlots()) {
            if(!devStack.empty())
                System.out.println("Dev Card production ID "+i+":\n"+lightModel.getDevelopmentCardDeck()[devStack.peek()].getProduction());
            i++;
        }
        //production from Leader Card
        for(Production p : lpb.getLeaderProductions()) {
            System.out.println("Leader Card production ID "+i+":\n"+p);
            i++;
        }
        System.out.print("Choose which productions to activate (activate {id}) or go back (cancel): ");
    }

    @Override
    public void showChosenProductions(String playerName,int[] activatedProductions) {
        LightPersonalBoard lpb=LBPByName(playerName);
        assert lpb != null;
        System.out.println("Player "+playerName+" has activated productions:");
        for(int production:activatedProductions){
            if(production==0){
                System.out.println("Base production\n"+lpb.getBaseProduction());
            }
            if(production==1||production==2||production==3){
                System.out.println("Development card production\n"+
                        lightModel.getDevelopmentCardDeck()[lpb.getDevelopmentCardSlots().get(production-1).peek()].getProduction()
                );
            }
            if(production==4||production==5){
                System.out.println("Leader card production\n"+
                        lpb.getLeaderProductions().get(production-4)
                );
            }
        }
    }

    @Override
    public void updateResources(String playerName, List<Map.Entry<ResType,Integer>> depots, List<Map.Entry<ResType,Integer>> leaderDepots, Map<ResType,Integer> strongbox) {
        LightPersonalBoard lpb=LBPByName(playerName);
        assert lpb != null;
        lpb.setDepots(depots);
        lpb.setLeaderDepots(leaderDepots);
        lpb.setStrongbox(strongbox);
        System.out.println("Player "+playerName+" now has this resources:");
        System.out.println("Depots: "+lpb.getDepots());
        System.out.println("Leader Depots: "+lpb.getLeaderDepots());
        System.out.println("Strongbox: "+lpb.getStrongbox());
    }

    @Override
    public void showDevCardGrid() {
        for(int[] row: lightModel.getDevelopmentCardGrid()){
            for(int id:row){
                System.out.println(lightModel.getDevelopmentCardDeck()[id]);
                System.out.print("\n");
            }
        }
        List<Map<ResType,Integer>> discounts=LBPByName(lightModel.getPlayerName()).getLeaderDiscounts();
        if(discounts.size()!=0){
            System.out.println("You have the following leader discounts:");
            int i=0;
            for(Map<ResType,Integer> m:discounts){
                for(Map.Entry<ResType,Integer> me:m.entrySet()){
                    System.out.println("Id "+i+": Discount "+me.getValue()+" "+me.getKey());
                }
            }
        }
        System.out.print("Choose a development card to buy (choosedevcard {id} [discount id] [discount id]) or go back (cancel): ");
    }

    @Override
    public void updateDevCardGrid(int level,CardType cardType, int newCardId) {
        lightModel.setCardInGrid(level,cardType,newCardId);
    }

    @Override
    public void askDevCardSlot() {
        LightPersonalBoard lpb=LBPByName(lightModel.getPlayerName());
        int i=0;
        for(Stack<Integer> d:lpb.getDevelopmentCardSlots()){
            System.out.println("Slot "+i+"\n"+(d.isEmpty()?"Empty":lightModel.getDevelopmentCardDeck()[d.peek()]));
            i++;
        }

        System.out.print("Choose where to put the new development card (chooseslot {0/1/2}: ");
    }

    @Override
    public void updateDevCardSlot(String playerName,int id,int slot) {
        LightPersonalBoard lpb=LBPByName(playerName);
        lpb.setDevCardSlot(id,slot);

        System.out.println("Player "+playerName+" has bought card "+
                lightModel.getDevelopmentCardDeck()[id]+
                "\nand placed it in slot "+slot+ " of their personal board"
        );
    }

    @Override
    public void showMarket() {
        System.out.print(lightModel.getLightMarket()+"Choose a row or a column (choose {row {0/1/2} / column {0/1/2/3}): ");
    }

    @Override
    public void chooseRowOrColumn() {

    }

    @Override
    public void updateMarket(boolean row,int index) {
        if(row){
            lightModel.getLightMarket().updateRow(index);
        }else{
            lightModel.getLightMarket().updateColumn(index);
        }
        System.out.println(row?"Row":"Column"+" "+index+" chosen");
        System.out.print("Updated "+lightModel.getLightMarket());
    }

    @Override
    public void disconnected() {

    }
}
