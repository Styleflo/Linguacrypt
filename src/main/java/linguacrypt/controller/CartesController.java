package linguacrypt.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import linguacrypt.model.Jeu;
import linguacrypt.utils.WordsFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartesController implements Observer {
    private Jeu jeu;

    @FXML
    private GridPane gridPane;
    @FXML
    private Label themeLabel;

    private List<String> currentMots;
    private int currentThemeIndex;
    private ArrayList<String> themes;

    public CartesController() {
        currentMots = new ArrayList<>();
        themes = new ArrayList<>();
        currentThemeIndex = 0;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();

        themes = wordsFileHandler.getAllThemes();
        currentThemeIndex = 0;
        currentMots = wordsFileHandler.getWordsByTheme(themes.get(currentThemeIndex));
    }

    private void afficherCartes() {
        if (jeu == null) return;

        gridPane.getChildren().clear();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(25));

        int row = 0;
        int col = 0;
        int maxCols = 7;


        for (int i = 0; i < currentMots.size(); i++) {
            AnchorPane carte = creerCarte(currentMots.get(i));

            create_transition(carte);

            gridPane.add(carte, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }

    }

    private AnchorPane creerCarte(String mot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Neutral_card.fxml"));
            AnchorPane card = loader.load();
            NeutralCardController controller = loader.getController();
            controller.setMot(mot);
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void create_transition(AnchorPane carte){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setToX(10);
        transition.setToY(5);

        carte.setOnMouseEntered(event -> {
            transition.setNode(carte);
            transition.play();
        });

        carte.setOnMouseExited(event -> {
            carte.setTranslateX(0);
            carte.setTranslateY(0);
        });
    }

    @FXML
    private void handleRevenirMenuAction() {
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Cartes")) {
            afficherCartes();
        }
    }

    @FXML
    public void nextCategory() {
        currentThemeIndex++;
        if (currentThemeIndex >= themes.size()) {
            currentThemeIndex = 0;
        }
        currentMots = jeu.getWordsFileHandler().getWordsByTheme(themes.get(currentThemeIndex));
        themeLabel.setText(themes.get(currentThemeIndex));
        afficherCartes();
    }

    @FXML
    public void previousCategory() {
        currentThemeIndex--;
        if (currentThemeIndex < 0) {
            currentThemeIndex = themes.size() - 1;
        }
        currentMots = jeu.getWordsFileHandler().getWordsByTheme(themes.get(currentThemeIndex));
        themeLabel.setText(themes.get(currentThemeIndex));
        afficherCartes();
    }
}
