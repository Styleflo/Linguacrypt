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
        setCurrentThemeIndex(0);
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
            // createTransition(carte);
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

    private Alert showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
    }

    private void handleCardClick(String word, double x, double y) {
        String theme = themes.get(currentThemeIndex);
        boolean lastWord = jeu.getWordsFileHandler().getWordsByTheme(theme).size() == 1;

        ContextMenu contextMenu = new ContextMenu();
        String deleteButtonLabel, alertTitle, alertContent;

        if (lastWord) {
            deleteButtonLabel = "Supprimer \"" + word + "\" et le thème \"" + theme + "\" ?";
            alertTitle = "Suppression d'un mot et d'un thème";
            alertContent = "Voulez vous supprimer le mot \"" + word + "\" et le thème \"" + theme + "\" ?";
        } else {
            deleteButtonLabel = "Supprimer \"" + word + "\" ?";
            alertTitle = "Suppression d'un mot";
            alertContent = "Voulez vous supprimer le mot \"" + word + "\" ?";
        }

        MenuItem deleteButton = new MenuItem(deleteButtonLabel);

        deleteButton.setOnAction(event -> {
            Alert alert = showAlert(AlertType.WARNING, alertTitle, null, alertContent);

            if (alert.showAndWait().isPresent()) {
                WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();
                wordsFileHandler.removeWordFromCategory(theme, word);
                currentMots.remove(word);

                if (lastWord) {
                    themes.remove(theme);
                    setCurrentThemeIndex(currentThemeIndex - 1);
                }

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

    public void createTransition(AnchorPane carte) {
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

    private void setCurrentThemeIndex(int index) {
        currentThemeIndex = index;

        if (currentThemeIndex >= themes.size()) {
            currentThemeIndex = 0;
        }

        if (currentThemeIndex < 0) {
            currentThemeIndex = themes.size() - 1;
        }

        currentMots = jeu.getWordsFileHandler().getWordsByTheme(themes.get(currentThemeIndex));
        updateCurrentThemeLabel();
    }

    @FXML
    public void nextCategory() {
        setCurrentThemeIndex(currentThemeIndex + 1);
        afficherCartes();
    }

    @FXML
    public void previousCategory() {
        setCurrentThemeIndex(currentThemeIndex - 1);
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

        if (result.isPresent()) {
            String mot = result.get().toLowerCase().trim();

            if (mot.length() > 13) {
                showAlert(AlertType.ERROR, "Erreur", "Mot trop long >:(", "Le mot doit contenir moins de 13 lettres.");
                handleAjouterMotAction();
            } else {
                Object[] res = wordsFileHandler.addWordToCategory(themes.get(currentThemeIndex), mot);
                boolean success = (boolean) res[0];
                String message = (String) res[1];
                if (success) {
                    currentMots.add(mot);
                    wordsFileHandler.writeJsonFile();
                    reagir();
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Mot existant", message);
                    handleAjouterMotAction();
                }
            }
        }
    }

    @FXML
    private void addNewTheme() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un thème");
        dialog.setHeaderText("Ajouter un thème de cartes");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField themeNameInput = new TextField();
        TextField firstCardInput = new TextField();

        grid.add(new Label("Nom du thème :"), 0, 0);
        grid.add(themeNameInput, 1, 0);
        grid.add(new Label("Première carte du thème :"), 0, 1);
        grid.add(firstCardInput, 1, 1);

        dialog.getDialogPane().setContent(grid);
        themeNameInput.requestFocus();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        themeNameInput.textProperty().addListener((obs, oldVal, newVal) ->
                okButton.setDisable(newVal.trim().isEmpty() || firstCardInput.getText().trim().isEmpty()));
        firstCardInput.textProperty().addListener((obs, oldVal, newVal) ->
                okButton.setDisable(newVal.trim().isEmpty() || themeNameInput.getText().trim().isEmpty()));

        Optional<String> result = dialog.showAndWait();
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();

        if (result.isPresent()) {
            String themeName = themeNameInput.getText().toLowerCase().trim();
            String firstCard = firstCardInput.getText().toLowerCase().trim();

            if (wordsFileHandler.themeExists(themeName)) {
                showAlert(AlertType.ERROR, "Erreur", "Thème existant", "Le thème \n" + themeName + "\n existe déjà.");
                addNewTheme();
            } else if (wordsFileHandler.getCategoryByWord(firstCard) != null) {
                showAlert(AlertType.ERROR, "Erreur", "Mot existant", "Le mot \"" + firstCard + "\"\n existe déjà.");
                addNewTheme();
            } else if (themeName.length() > 13) {
                showAlert(AlertType.ERROR, "Erreur", "Nom de thème trop long", "Le theme doit contenir moins de 13 lettres.");
                addNewTheme();
            } else if (firstCard.length() > 13) {
                showAlert(AlertType.ERROR, "Erreur", "Mot trop long", "Le mot doit contenir moins de 13 lettres.");
                addNewTheme();
            } else if (wordsFileHandler.addCategory(themeName)) {
                themes.add(themeName);
                wordsFileHandler.addWordToCategory(themeName, firstCard);
                setCurrentThemeIndex(themes.size() - 1);
                reagir();
                wordsFileHandler.writeJsonFile();
            }
        }
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Cartes")) {
            afficherCartes();
        }
    }
}
