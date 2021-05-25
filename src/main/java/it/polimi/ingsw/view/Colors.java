package it.polimi.ingsw.view;

public enum Colors {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m");

    private String escape;
    static final String RESET = "\u001B[0m";

    Colors(String escape) {
        this.escape = escape;
    }

    public String escape(){
        return escape;
    }
}