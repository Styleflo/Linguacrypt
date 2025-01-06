package linguacrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Plateau;
import linguacrypt.utils.WordsFileHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        WordsFileHandler wordsFileHandler = new WordsFileHandler("cards.json");
        ArrayList<String> words = wordsFileHandler.getWordsByTheme("nature");
        Plateau plateau = new Plateau(5, 5, words);
        plateau.prettyPrint();
        plateau.getKey().prettyPrint();
    }
}
