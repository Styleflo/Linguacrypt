package linguacrypt.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import linguacrypt.config.GameConfig;
import linguacrypt.model.Jeu;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.StringUtils;
import linguacrypt.utils.CardsDataManager;

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
    @FXML
    private ImageView filtre;


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
        CardsDataManager cardsDataManager = jeu.getWordsFileHandler();

        themes = cardsDataManager.getAllThemes();
        currentThemeIndex = 0;
        updateCurrentThemeLabel();
        currentMots = cardsDataManager.getWordsByTheme(themes.get(currentThemeIndex));
    }

    private void updateCurrentThemeLabel() {
        String name = themes.get(currentThemeIndex);
        themeLabel.setText(StringUtils.capitalizeFirstLetter(name));
    }

    private void afficherCartes() {
        DataUtils.assertNotNull(jeu, "Jeu non initialisé dans CartesController.afficherCartes()");
        filtre.setMouseTransparent(true);
        gridPane.getChildren().clear();
        gridPane.setHgap(GameConfig.CARTES_THEMES_HGAP);
        gridPane.setVgap(GameConfig.CARTES_THEMES_VGAP);
        gridPane.setPadding(new Insets(GameConfig.CARTES_THEMES_PADDING));

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
                CardsDataManager cardsDataManager = jeu.getWordsFileHandler();
                cardsDataManager.removeWordFromCategory(theme, word);
                currentMots.remove(word);

                if (lastWord) {
                    themes.remove(theme);
                    setCurrentThemeIndex(currentThemeIndex - 1);
                }

                cardsDataManager.saveUserConfig();
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
            DataUtils.logException(e, "Erreur lors de la création d'une carte");
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
        CardsDataManager cardsDataManager = jeu.getWordsFileHandler();

        if (result.isPresent()) {
            String mot = result.get().toLowerCase().trim();

            if (mot.length() > GameConfig.MAX_WORD_SIZE) {
                // Afficher une boîte de dialogue d'erreur
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Mot trop long >:(");
                alert.setContentText("Le mot doit contenir moins de " + GameConfig.MAX_WORD_SIZE + " lettres.");
                alert.showAndWait();
            } else {
                Object[] res = cardsDataManager.addWordToCategory(themes.get(currentThemeIndex), mot);
                boolean success = (boolean) res[0];
                String message = (String) res[1];
                if (success) {
                    // Ajoute le mot à la catégorie actuelle
                    currentMots.add(mot);
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
        CardsDataManager cardsDataManager = jeu.getWordsFileHandler();

        if (result.isPresent()) {
            String themeName = themeNameInput.getText().toLowerCase().trim();
            String firstCard = firstCardInput.getText().toLowerCase().trim();

            if (cardsDataManager.themeExists(themeName)) {
                showAlert(AlertType.ERROR, "Erreur", "Thème existant", "Le thème \n" + themeName + "\n existe déjà.");
                addNewTheme();
            } else if (cardsDataManager.getCategoryByWord(firstCard) != null) {
                showAlert(AlertType.ERROR, "Erreur", "Mot existant", "Le mot \"" + firstCard + "\"\n existe déjà.");
                addNewTheme();
            } else if (themeName.length() > GameConfig.MAX_WORD_SIZE) {
                showAlert(AlertType.ERROR, "Erreur", "Nom de thème trop long", "Le theme doit contenir moins de " + GameConfig.MAX_WORD_SIZE + " lettres.");
                addNewTheme();
            } else if (firstCard.length() > GameConfig.MAX_WORD_SIZE) {
                showAlert(AlertType.ERROR, "Erreur", "Mot trop long", "Le mot doit contenir moins de " + GameConfig.MAX_WORD_SIZE + " lettres.");
                addNewTheme();
            } else if (cardsDataManager.addCategory(themeName)) {
                themes.add(themeName);
                cardsDataManager.addWordToCategory(themeName, firstCard);
                setCurrentThemeIndex(themes.size() - 1);
                reagir();
                cardsDataManager.saveUserConfig();
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
