package it.polimi.ingsw.model;

import it.polimi.ingsw.view.Colors;

public enum CardType {
    GREEN,BLUE,YELLOW,PURPLE;

    @Override
    public String toString(){
        switch(this){
            case GREEN:
                return Colors.GREEN.escape()+"G"+Colors.RESET.escape();
            case BLUE:
                return Colors.BLUE.escape()+"B"+Colors.RESET.escape();
            case YELLOW:
                return Colors.YELLOW.escape()+"Y"+Colors.RESET.escape();
            case PURPLE:
                return Colors.MAGENTA.escape()+"P"+Colors.RESET.escape();
            default:
                return "";
        }
    }
}
