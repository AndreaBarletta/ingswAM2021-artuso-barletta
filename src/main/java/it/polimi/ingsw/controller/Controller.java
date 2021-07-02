package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevelopmentCard.DevelopmentCard;
import it.polimi.ingsw.model.Lorenzo.LorenzoEventListener;
import it.polimi.ingsw.model.PersonalBoard.Depot;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoard;
import it.polimi.ingsw.model.PersonalBoard.PersonalBoardEventListener;
import it.polimi.ingsw.view.LightDepot;
import it.polimi.ingsw.view.LightStrongbox;

import java.util.*;
import java.util.stream.Stream;

public class Controller implements PersonalBoardEventListener, LorenzoEventListener {
    private final List<ClientHandler> clientHandlers;
    private Game game;

    public Controller(){
        clientHandlers = new ArrayList<>();
    }

    /**
     * Adds a new client handler to the client handler list, also check if a new game has to be created, joined or started
     * @param clientHandler New client handler
     * @return Whether or not another client handler with the same name is not already present
     */
    public synchronized boolean addClientHandler(ClientHandler clientHandler){
        if(clientHandlers.size()==0){
            //First player connected, ask for size of the game
            clientHandlers.add(clientHandler);
            clientHandler.getAutomaton().evolve("ASK_NUMBER_OF_PLAYERS",null);
            return true;
        }else{
            clientHandler.getAutomaton().evolve("JOIN_GAME",null);
            try {
                game.addPlayer(clientHandler.getPlayerName());
            }catch(Exception e){
                return false;
            }

            for(ClientHandler c:clientHandlers){
                c.getAutomaton().evolve("NEW_PLAYER",new String[]{clientHandler.getPlayerName()});
            }

            clientHandlers.add(clientHandler);

            clientHandler.getAutomaton().evolve("WAIT_FOR_OTHER_PLAYERS",null);
            if(clientHandlers.size() == game.getNumOfPlayers()) {
                game.addPersonalBoardsEventListener(this);
                for (ClientHandler c : clientHandlers)
                    c.getAutomaton().evolve( "START_GAME", null);
            }
            return true;
        }
    }

    /**
     * Send a message to all the client handlers
     * @param message Message to be sent to all the client handlers
     */
    public synchronized void broadcast(Message message){
        for(ClientHandler ch:clientHandlers)
            ch.send(message);
    }

    /**
     * Create a new game. If the game is singleplayer, start the game
     * @param clientHandler Client handler
     * @param numberOfPlayers Number of players
     * @return Whether or not the game was created successfully (number of players correct, resources loaded correctly etc.)
     */
    public synchronized boolean createGame(ClientHandler clientHandler,int numberOfPlayers){
        if(numberOfPlayers>=1&&numberOfPlayers<=4){
            game=new Game(numberOfPlayers);

            if(!game.loadDevelopmentCardGridFromFile("/developmentCards.json")) return false;
            if(!game.loadLeaderCardsFromFile("/leaderCards.json")) return false;

            try{
                game.addPlayer(clientHandler.getPlayerName());
            }catch(Exception e){
                return false;
            }
            if(numberOfPlayers==1){
                try {
                    game.addLorenzo();
                    game.addLorenzoEventListener(this);
                    game.addPersonalBoardsEventListener(this);
                }catch(Exception e){
                    System.out.println("Error while loading faithtrack");
                    return false;
                }
                clientHandler.getAutomaton().evolve("START_GAME",null);
            } else {
                clientHandler.getAutomaton().evolve("WAIT_FOR_OTHER_PLAYERS",null);
            }
            return true;
        }
        return false;
    }

    /**
     * Get an array containing the players already present in the game
     * @return Array of players already present in the game
     */
    public synchronized String[] getPlayers() {
        List<String> players=new ArrayList<>();
        for(ClientHandler c:clientHandlers){
            players.add(c.getPlayerName());
        }
        
        String[] playersArray = new String[players.size()];
        players.toArray(playersArray);
        return playersArray;
    }

    /**
     * A player has disconnected from the game, notify the others
     * @param clientHandler Player that disconnected from the game
     */
    public synchronized void disconnected(ClientHandler clientHandler){
        clientHandlers.remove(clientHandler);
        game.removePlayer(clientHandler.getPlayerName());
        for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.DISCONNECTED,new String[]{clientHandler.getPlayerName()}));
        }
    }

    public synchronized String[][] getDevCardsGridIds(){
        return game.getDevelopmentCardsGridIds();
    }

    public synchronized ResType[][] getMarketTray(){
        return game.getMarketTray();
    }

    public synchronized ResType getLeftoverMarble(){
        return game.getLeftoverMarble();
    }

    /**
     * Show the initial leader cards to the player
     * @param clientHandler Client handler of the player
     */
    public synchronized void showInitialLeaderCards(ClientHandler clientHandler){
        String[] ids=game.getInitialLeaderCards(clientHandlers.indexOf(clientHandler));
        clientHandler.send(new Message(MessageType.SHOW_LEADER_CARDS,ids));
    }

    /**
     * Player has chosen the leader cards to keep and those to discard. Add them to the personal board
     * @param clientHandler Client handler that sent the request
     * @param leaderCardsId Ids of the leader cards chosen
     * @return Whether or not the leader cards could be added to the personal board (ie. they were among the 4 given)
     */
    public synchronized boolean leaderCardsChosen(ClientHandler clientHandler,String[] leaderCardsId){
        if(game.addLeaderCards(clientHandlers.indexOf(clientHandler), Arrays.stream(leaderCardsId).mapToInt(Integer::parseInt).toArray())){
            boolean ok=true;
            broadcast(new Message(MessageType.LEADER_CARDS_CHOSEN,
                    Stream.concat(
                            Arrays.stream(new String[]{clientHandler.getPlayerName()}),
                            Arrays.stream(leaderCardsId))
                            .toArray(String[]::new))
            );
            for(ClientHandler c:clientHandlers){
                if(c.getAutomaton().getState()!=GameState.LEADER_CARDS_CHOSEN){
                    ok=false;
                    break;
                }
            }
            if(ok){
                game.giveInkwell();
                String[] playerOrder=game.getPlayerOrder();
                for (ClientHandler c:clientHandlers){
                    c.getAutomaton().evolve("DISTRIBUTE_INKWELL",playerOrder);
                    if(game.getPlayerOrdinal(c.getPlayerName())!=0){
                        c.getAutomaton().evolve("ASK_INITIAL_RESOURCES",null);
                    }else{
                        c.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Add the initial additional resource to a player's depot
     * @param clientHandler Client handler of the player
     * @param resource Type of resource to be added
     */
    public synchronized boolean addInitialResource(ClientHandler clientHandler, ResType resource){
        if(resource!=ResType.FAITH&&resource!=ResType.ANY&&resource!=ResType.WHITEMARBLE){
            addResourcesToDepot(clientHandler,new ResType[]{resource});
            String playerName=clientHandler.getPlayerName();
            String lightDepotsAsJson = getLightDepotsAsJson(playerName);
            String leaderLightDepotsAsJson = getLeaderLightDepotsAsJson(playerName);
            String lightStrongboxAsJson = getLightStrongboxAsJson(playerName);
            broadcast(new Message(MessageType.CHOOSE_INITIAL_RESOURCES,new String[]{playerName,resource.name()}));
            broadcast(new Message(MessageType.UPDATE_RESOURCES,new String[]{playerName,lightDepotsAsJson,leaderLightDepotsAsJson,lightStrongboxAsJson}));

            //Check based on the player order, if they are allowed another additional resource
            //Third and fourth player are granted a faith point, fourth player is granted an additional resource
            switch(game.getPlayerOrdinal(playerName)){
                case 2:
                    game.getPersonalBoard(playerName).incrementFaithTrack(1);
                    clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    break;
                case 3:
                    if(!game.getPersonalBoard(playerName).hasAlreadyChosenInitialResources()){
                        game.getPersonalBoard(playerName).setHasAlreadyChosenInitialResources(true);
                        clientHandler.getAutomaton().evolve("ASK_INITIAL_RESOURCES",null);
                    }else{
                        game.getPersonalBoard(playerName).incrementFaithTrack(1);
                        clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
                    }
                    break;
                default:
                    clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
            }
            return true;
        }
        return false;
    }

    /**
     * Check if a game can start
     */
    public synchronized void playersWaiting(){
        if(!game.hasStarted()){
            boolean ok=true;
            for(ClientHandler c:clientHandlers){
                if(c.getAutomaton().getState()!=GameState.WAITING_FOR_YOUR_TURN){
                    ok=false;
                    break;
                }
            }
            if(ok){
                //Start the game
                game.setHasStarted(true);
                String activePlayer=game.getCurrentPlayer();
                for(ClientHandler c:clientHandlers){
                    c.send(new Message(MessageType.TURN_START,new String[]{activePlayer}));
                    if(c.getPlayerName().equals(activePlayer)){
                        c.getAutomaton().evolve("ASK_LEADER_ACTION",null);
                    }
                }
            }
        }
    }

    /**
     * Activate a player's leader card
     * @param clientHandler Client handler of the player
     * @param id id of the leader card to be activated
     * @throws CardNotFoundException The player doesn't have the selected leader card
     * @throws CardTypeException The player doesn't have enough development cards of a type required by the leader card
     * @throws ResourcesException The player doesn't have enough resources of a type required by the leader card
     * @throws LevelException The player doesn't have enough development cards of a level required by the leader card
     * @throws AlreadyActiveException The leader card has already been activated
     */
    public void activateLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException, CardTypeException, LevelException, ResourcesException,AlreadyActiveException {
        game.activateLeaderCards(clientHandler.getPlayerName(),Integer.parseInt(id));
    }

    /**
     * Discard a player's leader card, advance on the faith track and check if a vatican report can be sent or the game can be terminated
     * @param clientHandler Client handler of the player
     * @param id id of the leader card to be discarded
     * @throws CardNotFoundException The player doesn't have the selected leader card
     */
    public void discardLeaderCard(ClientHandler clientHandler, String id) throws CardNotFoundException {
        game.discardLeaderCards(clientHandler.getPlayerName(), Integer.parseInt(id));
        incrementFaithTrack(clientHandler.getPlayerName(),1);
        //Check if a vatican report can be activated
        int result=canSendVaticanReport();
        if(result!=-1){
            sendVaticanReport(result);
        }
    }

    /**
     * Activate a player's productions and check if a vatican report can be sent or the game can be terminated
     * @param playerName Name of the player
     * @param productionsId Productions to activate, 0 for base production, 1 to 3 for development cards, 4 and 5 for leader cards
     * @throws ResourcesException The player doesn't have enough ingredients to activate all the productions
     * @throws AnyResourceException A production has a chooseable resource as ingredient or product
     */
    public void activateProductions(String playerName, String[] productionsId) throws ResourcesException,AnyResourceException {
        //convert ids in productions
        Production[]  productions = new Production[productionsId.length];
        PersonalBoard pb=game.getPersonalBoard(playerName);
        int i=0;
        for(String p : productionsId) {
            Production tempProd;
            if(p.equals("0")) {
                tempProd=pb.getBaseProduction();
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
            else if(p.equals("1")) {
                tempProd=pb.getDevelopmentCardSlots()[0].getTopCard().getProduction();
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
            else if(p.equals("2")) {
                tempProd=pb.getDevelopmentCardSlots()[1].getTopCard().getProduction();
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
            else if(p.equals("3")) {
                tempProd=pb.getDevelopmentCardSlots()[2].getTopCard().getProduction();
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
            else if(p.equals("4")) {
                tempProd=pb.getLeaderProductions().get(0);
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
            else if(p.equals("5")) {
                tempProd=pb.getLeaderProductions().get(1);
                productions[i]=new Production(tempProd.getIngredients(),tempProd.getProducts());
                i++;
            }
        }

        game.activateProductions(playerName, productions);
        //Check if a vatican report can be triggered
        int result=canSendVaticanReport();
        if(result!=-1){
            sendVaticanReport(result);
        }
    }

    public String getLightDepotsAsJson(String playerName) {
        Depot[] depots=game.getDepots(playerName);
        List<LightDepot> lightDepots=new ArrayList<>();
        for(Depot d:depots)
            lightDepots.add(new LightDepot(d.getDepotResources(),d.getCounter(),d.getCapacity()));

        Gson gson=new Gson();
        return gson.toJson(lightDepots.toArray());
    }

    public String getLeaderLightDepotsAsJson(String playerName) {
        List<Depot> depots=game.getLeaderDepots(playerName);
        List<LightDepot> lightDepots=new ArrayList<>();
        for(Depot d:depots)
            lightDepots.add(new LightDepot(d.getDepotResources(),d.getCounter(),d.getCapacity()));

        Gson gson=new Gson();
        return gson.toJson(lightDepots.toArray());
    }

    public String getLightStrongboxAsJson(String playerName) {
        Map<ResType,Integer> strongbox=game.getStrongboxContent(playerName);
        Gson gson=new Gson();
        String json=gson.toJson(new LightStrongbox(strongbox));
        gson.fromJson(json,LightStrongbox.class);
        return json;
    }

    /**
     * Checks if a card can be bought and placed in the player board
     * @param clienthandler Client handler of the player
     * @param id Id of the card to be bought
     * @param discountIds Optional discounts
     * @throws ResourcesException The player doesn't have enough resources to buy the development card selected
     * @throws LevelException The player doesn't have an high enough card in the selected slot to buy the development card selected
     */
    public void canBuyDevCard(ClientHandler clienthandler,String id,String[] discountIds) throws ResourcesException,LevelException,CardNotFoundException {
        game.canBuyDevCard(clienthandler.getPlayerName(),Integer.parseInt(id),discountIds!=null?Arrays.stream(discountIds).mapToInt(Integer::parseInt).toArray():null);
    }

    /**
     * Buy a dev card
     * @param clientHandler Client handler of the player that buys the dev card
     * @param id Id of the card
     * @param slot Slot where to put the car
     * @param discountIds Optional discounts
     * @throws LevelException Cannot add the card to the selected slot
     */
    public void buyDevCard(ClientHandler clientHandler,String id,String slot,int[] discountIds) throws LevelException{
        game.buyDevCard(clientHandler.getPlayerName(),Integer.parseInt(id),Integer.parseInt(slot),discountIds);
    }

    /**
     * Acquire resources from the market
     * @param clientHandler Client handler of the player
     * @param rowOrColumn Whether the player wants to acquire a row or a column
     * @param index Index of the row or column
     * @return Resources acquired
     */
    public ResType[] acquireFromMarket(ClientHandler clientHandler, String rowOrColumn, String index){
        ResType[] acquiredResources=game.acquireFromMarket(clientHandler.getPlayerName(), rowOrColumn.equals("row"), Integer.parseInt(index));
        broadcast(new Message(MessageType.UPDATE_MARKET,new String[]{rowOrColumn,index}));
        return acquiredResources;
    }

    public boolean canAddToDepot(ClientHandler clientHandler, ResType[] resources){
        return game.canAddToDepot(clientHandler.getPlayerName(),resources);
    }

    public boolean canDiscount(ClientHandler clientHandler,String[] ids){
        return game.canDiscount(clientHandler.getPlayerName(), Arrays.stream(ids).mapToInt(Integer::parseInt).toArray());
    }

    public String[] removeDevCardFromGrid(String id){
        return game.removeDevCardFromGrid(id);
    }

    public void endTurnAction(ClientHandler clientHandler){
        PersonalBoard personalBoard= game.getPersonalBoard(clientHandler.getPlayerName());
        personalBoard.setHasAlreadyPlayedLeaderAction(true);
    }

    /**
     * End the leader action, if it's the last leader action, let the next player play its turn
     * @param clientHandler Client handler of the player
     */
    public void endLeaderAction(ClientHandler clientHandler){
        PersonalBoard personalBoard= game.getPersonalBoard(clientHandler.getPlayerName());
        if(personalBoard.hasAlreadyPlayedLeaderAction()){
            personalBoard.setHasAlreadyPlayedLeaderAction(false);
            clientHandler.getAutomaton().evolve("WAIT_FOR_YOUR_TURN",null);
            game.nextPlayer();
            if(!game.hasEnded()){
                getClientHandler(game.getCurrentPlayer()).getAutomaton().evolve("ASK_LEADER_ACTION",null);
            }else{
                //Announce winner
                String winner=game.getWinner();
                for(ClientHandler c:clientHandlers){
                    c.send(new Message(MessageType.WINNER,new String[]{winner}));
                    c.getAutomaton().evolve("END_GAME",null);
                }
            }
        }else{
            clientHandler.getAutomaton().evolve("ASK_TURN_ACTION",null);
        }
    }

    private ClientHandler getClientHandler(String playerName){
        for(ClientHandler c:clientHandlers)
            if(c.getPlayerName().equals(playerName))
                return c;

        return null;
    }

    public void addResourcesToDepot(ClientHandler clientHandler, ResType[] resources){
        for(ResType r:resources)
            try{
                game.getPersonalBoard(clientHandler.getPlayerName()).addResourceToDepot(r);
            }catch(Exception e){}

        //Check if a vatican report can be activated
        int result=canSendVaticanReport();
        if(result!=-1){
            sendVaticanReport(result);
        }
    }

    @Override
    public void incrementFaithTrack(String playername, int increment) {
        broadcast(new Message(MessageType.INCREMENT_FAITH_TRACK,new String[]{playername,String.valueOf(increment)}));
    }

    @Override
    public void incrementLorenzoFaithTrack(int increment) {
        broadcast(new Message(MessageType.LORENZO_INCREMENT_FAITH_TRACK,new String[]{(String.valueOf(increment))}));
    }

    @Override
    public void removeBottomCard(CardType cardType,int[] level){
        broadcast((new Message(MessageType.LORENZO_DISCARD,new String[]{cardType.toString()})));
        for(int i:level){
            DevelopmentCard removedCard=game.getDevelopmentCardGrid().getTopCard(i,cardType);
            broadcast((new Message(MessageType.UPDATE_DEV_CARD_GRID,new String[]{
                    String.valueOf(i),
                    cardType.name(),
                    String.valueOf(removedCard==null?-1:removedCard.getId())
            })));
        }
    }

    @Override
    public void lorenzoShuffle() {
        broadcast((new Message(MessageType.LORENZO_SHUFFLE, null)));
    }

    @Override
    public void lorenzoWon() {
        broadcast(new Message(MessageType.LORENZO_WON, null));
    }

    public void discardResource(ClientHandler clientHandler, int numberOfResourcesDiscarded){
        for(ClientHandler c:clientHandlers)
            if(!c.getPlayerName().equals(clientHandler.getPlayerName()))
                game.getPersonalBoard(c.getPlayerName()).incrementFaithTrack(numberOfResourcesDiscarded);

        incrementLorenzoFaithTrack(numberOfResourcesDiscarded);
        //Check if a vatican report can be activated
        int result=canSendVaticanReport();
        if(result!=-1){
            sendVaticanReport(result);
        }
    }

    /**
     * Check if a player has bought 7 development cards, hence trigger the end of the game
     * @param clientHandler Client handler that just bought a development card
     */
    public void checkDevCardEnd(ClientHandler clientHandler){
        int devCards=game.getNumberOfDevCards(clientHandler.getPlayerName());
        if(devCards>=7){
            game.lastTurn();
            for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.LAST_TURNS,null));
            }
        }
    }

    public int canSendVaticanReport(){
        return game.canSendVaticanReport();
    }

    /**
     * Send a vatican report and tell all the clients
     * @param k index of the vatican report to send
     */
    public void sendVaticanReport(int k){
        Map<String,Boolean> reportResults=game.sendVaticanReport(k);
        for(ClientHandler c:clientHandlers){
            c.send(new Message(MessageType.VATICAN_REPORT_RESULTS,new String[]{reportResults.toString()}));
        }
        isFaithTrackEnd();
    }

    /**
     * Check if a player is at the end of the faith track
     */
    public void isFaithTrackEnd(){
        if(game.isFaithTrackEnd()){
            game.lastTurn();
            for(ClientHandler c:clientHandlers){
                c.send(new Message(MessageType.LAST_TURNS,null));
            }
        }
    }

    /**
     * Choose a resource to replace the chooseable resource in productions
     * @param anyResource Resource to replace the chooseable resource
     */
    public void chooseAnyResource(ResType anyResource){
        game.chooseAnyResource(anyResource);
    }

    /**
     * Get the game mode
     * @return "single" for singleplayer, "multi" for multiplayer
     */
    public String getGameMode(){
        return game.getNumberOfPlayer()==1?"single":"multi";
    }
}