package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Game {
    private PersonalBoard[] personalBoards;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;

    public Game(){
        market=new Market();
        developmentCardGrid=new DevelopmentCardGrid();
    }

    public void loadDevelopmentCardsFromFile(String path) throws IOException {
        File file=new File(path);
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        Gson gson=new Gson();
        DevelopmentCard developmentCards[]=gson.fromJson(content, DevelopmentCard[].class);

        for(DevelopmentCard d:developmentCards){
            developmentCardGrid.addCard(d);
        }
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }
}