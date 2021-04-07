package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Game {
    private PersonalBoard[] personalBoards;
    private Market market;
    private DevelopmentCardGrid developmentCardGrid;

    public void loadDevelopmentCardsFromFile(String path) throws IOException {
        File file=new File(path);
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        Gson gson=new Gson();
        developmentCardGrid=gson.fromJson(content,DevelopmentCardGrid.class);
    }

    public DevelopmentCardGrid getDevelopmentCardGrid(){
        return developmentCardGrid;
    }
}