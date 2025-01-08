package linguacrypt.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import linguacrypt.model.Jeu;
import linguacrypt.utils.StringUtils;
import linguacrypt.utils.WordsFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
        updateCurrentThemeLabel();
        currentMots = wordsFileHandler.getWordsByTheme(themes.get(currentThemeIndex));
    }

    private void updateCurrentThemeLabel() {
        String name = themes.get(currentThemeIndex);
        themeLabel.setText(StringUtils.capitalizeFirstLetter(name));
    }

    private void afficherCartes() {
        if (jeu == null) return;

        gridPane.getChildren().clear();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(33));

        int row = 0;
        int col = 0;
        int maxCols = 5;

        for (int i = 0; i < currentMots.size(); i++) {
            AnchorPane carte = creerCarte(currentMots.get(i));

            assert carte != null;
            //create_transition(carte);
            int finalI = i;
            carte.setOnMouseClicked(event -> handleCardClick(currentMots.get(finalI), event.getScreenX(), event.getScreenY()));

            gridPane.add(carte, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    private void handleCardClick(String word, double x, double y) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteButton = new MenuItem("Supprimer \"" + word + "\" ?");

        deleteButton.setOnAction(coucou -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Suppression d'un mot");
            alert.setHeaderText(null);
            alert.setContentText("Voulez vous supprimer le mot \"" + word + "\" ?");
            if (alert.showAndWait().isPresent()) {
                WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();
                wordsFileHandler.removeWordFromCategory(themes.get(currentThemeIndex), word);
                currentMots.remove(word);

                wordsFileHandler.writeJsonFile();

                reagir();
            }
        });

        contextMenu.getItems().add(deleteButton);
        contextMenu.getItems().add(new MenuItem("Annuler"));
        contextMenu.show(gridPane.getScene().getWindow(), x, y);
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

    public void create_transition(AnchorPane carte) {
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

    @FXML
    public void nextCategory() {
        currentThemeIndex++;
        if (currentThemeIndex >= themes.size()) {
            currentThemeIndex = 0;
        }
        currentMots = jeu.getWordsFileHandler().getWordsByTheme(themes.get(currentThemeIndex));
        updateCurrentThemeLabel();
        afficherCartes();
    }

    @FXML
    public void previousCategory() {
        currentThemeIndex--;
        if (currentThemeIndex < 0) {
            currentThemeIndex = themes.size() - 1;
        }
        currentMots = jeu.getWordsFileHandler().getWordsByTheme(themes.get(currentThemeIndex));
        updateCurrentThemeLabel();
        afficherCartes();
    }

    @FXML
    private void handleAjouterMotAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un mot");
        dialog.setHeaderText("Ajouter un mot à la collection");
        dialog.setContentText("Veuillez entrer un mot :");

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> okButton.setDisable(newValue.trim().isEmpty()));

        Optional<String> result = dialog.showAndWait();
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();

        AtomicBoolean motRefuse = new AtomicBoolean(false);

        if (result.isPresent()) {
            String mot = result.get().toLowerCase().trim();

            if (mot.length() > 13) {
                // Afficher une boîte de dialogue d'erreur
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Mot trop long >:(");
                alert.setContentText("Le mot doit contenir moins de 13 lettres.");
                alert.showAndWait();
                motRefuse.set(true);
            } else {
                Object[] res = wordsFileHandler.addWordToCategory(themes.get(currentThemeIndex), mot);
                boolean success = (boolean) res[0];
                String message = (String) res[1];
                if (success) {
                    // Ajoute le mot à la catégorie actuelle
                    currentMots.add(mot);
                    reagir();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Mot existant");
                    alert.setContentText(message);
                    alert.showAndWait();
                    motRefuse.set(true);
                }
            }
        }

        if (motRefuse.get()) {
            handleAjouterMotAction();
        } else {
            wordsFileHandler.writeJsonFile();
        }
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Cartes")) {
            afficherCartes();
        }
    }
}
