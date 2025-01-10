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
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import linguacrypt.config.GameConfig;
import linguacrypt.model.Jeu;
import linguacrypt.utils.GameDataManager;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.StringUtils;

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

    //popup nv Carte
    @FXML
    private StackPane panneauNvCarte;

    @FXML
    private TextField themeNameInput;

    @FXML
    private TextField firstCardInput;

    @FXML
    private Button okButton;

    private String themeName;

    private String firstCard;


    public CartesController() {
        currentMots = new ArrayList<>();
        themes = new ArrayList<>();
        currentThemeIndex = 0;

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        GameDataManager gameDataManager = jeu.getGameDataManager();

        themes = gameDataManager.getAllThemes();
        currentThemeIndex = 0;
        updateCurrentThemeLabel();
        currentMots = gameDataManager.getWordsByTheme(themes.get(currentThemeIndex));
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
        boolean lastWord = jeu.getGameDataManager().getWordsByTheme(theme).size() == 1;

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

            if (alert.getResult() == ButtonType.OK) {
                GameDataManager gameDataManager = jeu.getGameDataManager();
                gameDataManager.removeWordFromCategory(theme, word);
                currentMots.remove(word);

                if (lastWord) {
                    themes.remove(theme);
                    setCurrentThemeIndex(currentThemeIndex - 1);
                }

                gameDataManager.saveUserConfig();
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

        currentMots = jeu.getGameDataManager().getWordsByTheme(themes.get(currentThemeIndex));
        updateCurrentThemeLabel();
    }

    @FXML
    public void nextCategory() {
        currentThemeIndex++;
        if (currentThemeIndex >= themes.size()) {
            currentThemeIndex = 0;
        }
        currentMots = jeu.getGameDataManager().getWordsByTheme(themes.get(currentThemeIndex));
        updateCurrentThemeLabel();
        afficherCartes();
    }

    @FXML
    public void previousCategory() {
        currentThemeIndex--;
        if (currentThemeIndex < 0) {
            currentThemeIndex = themes.size() - 1;
        }
        currentMots = jeu.getGameDataManager().getWordsByTheme(themes.get(currentThemeIndex));
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
        GameDataManager gameDataManager = jeu.getGameDataManager();

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
                Object[] res = gameDataManager.addWordToCategory(themes.get(currentThemeIndex), mot);
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
        themeName = null;
        firstCard = null;
        themeNameInput.clear();
        firstCardInput.clear();

        panneauNvCarte.setVisible(true);

        themeNameInput.requestFocus();
        okButton.setDisable(true);

        themeNameInput.textProperty().addListener((obs, oldVal, newVal) ->
                okButton.setDisable(newVal.trim().isEmpty() || firstCardInput.getText().trim().isEmpty()));
        firstCardInput.textProperty().addListener((obs, oldVal, newVal) ->
                okButton.setDisable(newVal.trim().isEmpty() || themeNameInput.getText().trim().isEmpty()));

    }

    private void addNewThemeSuite(){
        GameDataManager gameDataManager = jeu.getGameDataManager();

        if (themeName != null && firstCard != null) {

            if (gameDataManager.themeExists(themeName)) {
                showAlert(AlertType.ERROR, "Erreur", "Thème existant", "Le thème \n" + themeName + "\n existe déjà.");
                addNewTheme();
            } else if (gameDataManager.getCategoryByWord(firstCard) != null) {
                showAlert(AlertType.ERROR, "Erreur", "Mot existant", "Le mot \"" + firstCard + "\"\n existe déjà.");
                addNewTheme();
            } else if (themeName.length() > GameConfig.MAX_WORD_SIZE) {
                showAlert(AlertType.ERROR, "Erreur", "Nom de thème trop long", "Le theme doit contenir moins de " + GameConfig.MAX_WORD_SIZE + " lettres.");
                addNewTheme();
            } else if (firstCard.length() > GameConfig.MAX_WORD_SIZE) {
                showAlert(AlertType.ERROR, "Erreur", "Mot trop long", "Le mot doit contenir moins de " + GameConfig.MAX_WORD_SIZE + " lettres.");
                addNewTheme();
            } else if (gameDataManager.addCategory(themeName)) {
                themes.add(themeName);
                gameDataManager.addWordToCategory(themeName, firstCard);
                setCurrentThemeIndex(themes.size() - 1);
                reagir();
                gameDataManager.saveUserConfig();
            }
        }
    }

    @FXML
    private void CategoryImage() {
        jeu.setView("CartesImages");
        jeu.notifyObservers();
    }

    //PopUp Nouvelle Carte

    @FXML
    private void handleAnnulerpopup(){
        panneauNvCarte.setVisible(false);
    }

    @FXML
    private void handleValiderPopup(){
        if (!themeNameInput.getText().trim().isEmpty() && !firstCardInput.getText().trim().isEmpty()) {
            themeName = themeNameInput.getText().toLowerCase().trim();
            firstCard = firstCardInput.getText().toLowerCase().trim();
            panneauNvCarte.setVisible(false);
            addNewThemeSuite();
        }
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Cartes")) {
            afficherCartes();
        }
    }
}
