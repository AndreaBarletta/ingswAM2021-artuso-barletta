package it.polimi.ingsw;

public enum MessageType {
    INFO,DISCONNECTED,ERROR,
    PICK_PLAYERNAME,
    ASK_NUMBER_OF_PLAYERS,NUMBER_OF_PLAYERS,GAME_CREATED,
    WAIT_FOR_OTHER_PLAYERS,NEW_PLAYER, GAME_JOINED,
    GAME_STARTED,
    SHOW_LEADER_CARDS,CHOOSE_LEADER_CARDS,
    INKWELL_GIVEN,
    ASK_INITIAL_RESOURCES,CHOOSE_INITIAL_RESOURCES,
    WAIT_YOUR_TURN, TURN_START,
    INCREMENT_FAITH_TRACK,
    ASK_LEADER_ACTION, CHOOSE_LEADER_ACTION,
    LEADER_ACTION_ACTIVATE,LEADER_ACTION_DISCARD,LEADER_ACTION_SKIP,
    ASK_TURN_ACTION,
    ACTIVATE_PRODUCTIONS,BUY_DEV_CARDS,VISIT_MARKET;
}
