package it.polimi.ingsw.model.Lorenzo;

import it.polimi.ingsw.model.CardType;
import it.polimi.ingsw.model.Lorenzo.Lorenzo;
import it.polimi.ingsw.view.Colors;

public enum SoloActionTokens {
    DISCARDGREEN, DISCARDYELLOW, DISCARDBLUE, DISCARDPURPLE, MOVE, SHUFFLE;

    public String getSymbol() {
        switch (this) {
            case DISCARDGREEN:
                return Colors.GREEN.escape()+"DISCARDGREEN"+Colors.RESET.escape();
            case DISCARDYELLOW:
                return Colors.YELLOW.escape()+"DISCARDYELLOW"+Colors.RESET.escape();
            case DISCARDBLUE:
                return Colors.CYAN.escape()+"DISCARDBLUE"+Colors.RESET.escape();
            case DISCARDPURPLE:
                return Colors.MAGENTA.escape()+"DISCARDPURPLE"+Colors.RESET.escape();
            case MOVE:
                return Colors.RED.escape()+"MOVE"+Colors.RESET.escape();
            case SHUFFLE:
                return Colors.WHITE.escape()+"SHUFFLE"+Colors.RESET.escape();
            default:
                return "";
        }
    }

    public void effectOnDraw (Lorenzo lorenzo) {
        switch (this) {
            case DISCARDGREEN:
                lorenzo.removeBottomCard(CardType.GREEN);
                break;
            case DISCARDYELLOW:
                lorenzo.removeBottomCard(CardType.YELLOW);
                break;
            case DISCARDBLUE:
                lorenzo.removeBottomCard(CardType.BLUE);
                break;
            case DISCARDPURPLE:
                lorenzo.removeBottomCard(CardType.PURPLE);
                break;
            case MOVE:
                lorenzo.incrementFaithTrack(2);
                break;
            case SHUFFLE:
                lorenzo.shuffle();
                break;
        }
    }
}
