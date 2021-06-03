package it.polimi.ingsw;

public enum MessageType {
    INFO,DISCONNECTED,ERROR,
    PICK_PLAYERNAME,SET_PLAYERNAME,
    ASK_NUMBER_OF_PLAYERS,NUMBER_OF_PLAYERS,GAME_CREATED,
    WAIT_FOR_OTHER_PLAYERS,NEW_PLAYER, GAME_JOINED,
    GAME_STARTED,SET_DEV_CARD_GRID,SET_MARKET,
    SHOW_LEADER_CARDS,CHOOSE_LEADER_CARDS,LEADER_CARDS_CHOSEN,
    INKWELL_GIVEN,
    ASK_INITIAL_RESOURCES,CHOOSE_INITIAL_RESOURCES,
    WAIT_YOUR_TURN, TURN_START,
    INCREMENT_FAITH_TRACK,
    ASK_LEADER_ACTION,
    LEADER_ACTION_ACTIVATE,LEADER_ACTION_DISCARD,LEADER_ACTION_SKIP,LEADER_ACTIVATED,LEADER_DISCARDED,LEADER_SKIPPED,
    ASK_TURN_ACTION,TURN_CHOICE,CANCEL,
    ACTIVATE_PRODUCTIONS,SHOW_PRODUCTIONS,CHOOSE_PRODUCTIONS,SHOW_CHOSEN_PRODUCTIONS,UPDATE_RESOURCES,
    BUY_DEV_CARD,SHOW_DEV_CARD_GRID,CHOOSE_DEV_CARD,UPDATE_DEV_CARD_GRID,ASK_DEV_CARD_SLOT,CHOOSE_DEV_CARD_SLOT,UPDATE_DEV_CARD_SLOT,
    SHOW_MARKET,CHOOSE_ROW_OR_COLUMN, UPDATE_MARKET,ASK_DISCARD_RESOURCE,DISCARD_RESOURCE;
}
