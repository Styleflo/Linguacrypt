package linguacrypt.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;
import linguacrypt.model.TypePartie;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.FileSaveDeleteHandler;
import linguacrypt.utils.GameDataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParametreController implements Observer {

    private final int MIN_TIME = 30; // 30 secondes
    private final int MAX_TIME = 1800; // 30 minutes
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private ImageView filtre;
    @FXML
    private Pane lesthemes;
    @FXML
    private VBox themeBox;
    @FXML
    private CheckBox Images;
    @FXML
    private CheckBox Mots;
    @FXML
    private Label labelTimer;
    private int currentTime = -1; // -1 signifie infini

    @FXML
    private Button choisirThemeButton;
    @FXML
    private Button themesAleatoiresButton;

    //pop up
    @FXML
    private AnchorPane popup;

    private Jeu jeu;
    private PartieBuilder partieBuilder;

    private Map<String, Integer> themeWordCounts = new HashMap<>();

    private int nbr_carte_selectionne;

    private int width;

    private int height;


    public ParametreController() {
        width = 5;
        height = 5;
    }

    public void resetParametre() {
        partieBuilder.resetall();
    }

    public void handleFlecheGaucheTimer() {
        if (currentTime == -1) {
            currentTime = MAX_TIME;
        } else if (currentTime > MIN_TIME) {
            // Réduit de 30 secondes
            currentTime = Math.max(MIN_TIME, currentTime - 30);
        } else {
            currentTime = -1;
        }
        updateTimerLabel();
    }

    public void handleFlecheDroiteTimer() {
        if (currentTime == -1) {
            currentTime = MIN_TIME;
        } else if (currentTime < MAX_TIME) {
            // Augmente de 30 secondes
            currentTime = Math.min(MAX_TIME, currentTime + 30);
        } else {
            currentTime = -1;
        }
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        if (currentTime == -1) {
            labelTimer.setText("∞");
        } else {
            int minutes = currentTime / 60;
            int seconds = currentTime % 60;
            labelTimer.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void setPartieBuilder() throws IOException {
        this.partieBuilder = new PartieBuilder(jeu);
        filtre.setMouseTransparent(true);
        handleCartesAleatoire();
        handleModeMots();
    }

    public void handleCartesAleatoire() {
        themesAleatoiresButton.getStyleClass().add("button-selected");
        choisirThemeButton.getStyleClass().remove("button-selected");
        partieBuilder.resetWordsUsed();
    }

    @FXML
    public void handleFlecheGauchelb1() {
        int valeurActuelle = Integer.parseInt(label1.getText());
        if (valeurActuelle > 2) {
            label1.setText(String.valueOf(valeurActuelle - 1));
            partieBuilder.setHeightParameter(valeurActuelle - 1);
            height = valeurActuelle - 1;
        }
    }

    @FXML
    public void handleFlecheDroitelb1() {
        int valeurActuelle = Integer.parseInt(label1.getText());
        if (valeurActuelle < 8) {
            label1.setText(String.valueOf(valeurActuelle + 1));
            partieBuilder.setHeightParameter(valeurActuelle + 1);
            height = valeurActuelle + 1;
        }
    }

    @FXML
    public void handleFlecheGauchelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle > 2) {
            label2.setText(String.valueOf(valeurActuelle - 1));
            partieBuilder.setWidthParameter(valeurActuelle - 1);
            width = valeurActuelle - 1;
        }
    }

    @FXML
    public void handleFlecheDroitelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle < 8) {
            label2.setText(String.valueOf(valeurActuelle + 1));
            partieBuilder.setWidthParameter(valeurActuelle + 1);
            width = valeurActuelle + 1;
        }
    }

    @FXML
    public void handleThemes() {
        lesthemes.setVisible(true);
        GameDataManager gameDataManager = jeu.getGameDataManager();
        ArrayList<String> themes = gameDataManager.getAllThemes();
        themeBox.getChildren().clear();

        for (String theme : themes) {
            HBox themeItem = new HBox(10);
            themeItem.getStyleClass().add("theme-item");

            CheckBox checkBox = new CheckBox();
            checkBox.setStyle("-fx-text-fill: white;");

            Label label = new Label(theme);
            label.setStyle("-fx-text-fill: white;");

            // Rendre tout l'élément cliquable
            themeItem.setOnMouseClicked(event -> {
                checkBox.setSelected(!checkBox.isSelected());
            });

            int wordCount = gameDataManager.getWordsByTheme(theme).size();
            themeWordCounts.put(theme, wordCount);

            // Empêcher la propagation du clic de la checkbox à l'élément parent
            checkBox.setOnMouseClicked(Event::consume);

            themeItem.getChildren().addAll(checkBox, label);
            themeBox.getChildren().add(themeItem);
        }
    }

    @FXML
    private void handleValider() {
        ArrayList<String> selectedThemes = new ArrayList<>();
        nbr_carte_selectionne = 0; // Réinitialiser le compteur de cartes sélectionnées

        for (javafx.scene.Node node : themeBox.getChildren()) {
            if (node instanceof HBox hbox) {
                for (javafx.scene.Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox checkBox) {
                        if (checkBox.isSelected()) {
                            // Vérifier que l'index est valide avant d'accéder à l'élément
                            if (hbox.getChildren().size() > 1 && hbox.getChildren().get(1) instanceof Label label) {
                                String theme = label.getText();
                                selectedThemes.add(theme);

                                // Ajouter la taille du thème au nombre de cartes sélectionnées
                                if (themeWordCounts.containsKey(theme)) {
                                    nbr_carte_selectionne += themeWordCounts.get(theme);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (nbr_carte_selectionne < width * height) {

            popup.setVisible(true);
        } else {
            partieBuilder.setUsedThemes(selectedThemes);
            lesthemes.setVisible(false);
            themesAleatoiresButton.getStyleClass().remove("button-selected");
            choisirThemeButton.getStyleClass().add("button-selected");
        }
    }

    @FXML
    private void handleModeImage() {
        if (Images.isSelected()) {
            Mots.setSelected(false);
            choisirThemeButton.setVisible(false);
            themesAleatoiresButton.setVisible(false);
        } else {
            choisirThemeButton.setVisible(true);
            themesAleatoiresButton.setVisible(true);
        }
    }

    @FXML
    private void handleModeMots() {
        if (Mots.isSelected()) {
            Images.setSelected(false);
            choisirThemeButton.setVisible(true);
            themesAleatoiresButton.setVisible(true);
        } else {
            choisirThemeButton.setVisible(false);
            themesAleatoiresButton.setVisible(false);
        }
    }

    @FXML
    private void handleValiderTout() throws IOException {

        if (Images.isSelected() && !Mots.isSelected()) {
            partieBuilder.setTimer(currentTime);
            partieBuilder.setTypePartie(TypePartie.IMAGES);
            jeu.setView("PlateauImage");
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.notifyObservers();
        } else if (Mots.isSelected() && !Images.isSelected()) {
            partieBuilder.setTimer(currentTime);
            partieBuilder.setTypePartie(TypePartie.WORDS);
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.setView("Plateau");
            jeu.notifyObservers();
        } else {
            // eventuellement pop up à mettre un jour
        }
    }

    @FXML
    private void handleMenu() {
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @FXML
    /**
     * La fonction permet de load une partie non fini qui fut sauvegardé dans le passé
     * Elle lit un fichier Json et remet à jour l'état des classes
     */
    private void loadPartie() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger une partie déjà existante");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
        File fichier = fileChooser.showOpenDialog(new Stage());
        if (fichier != null) {
            try {
                FileSaveDeleteHandler filesavehandler = new FileSaveDeleteHandler();
                Partie partieload = filesavehandler.loadPartie(fichier.getAbsolutePath());
                jeu.setPartie(partieload);
                if (partieload.getTypePartie() == TypePartie.IMAGES) {
                    jeu.setView("PlateauImage");
                } else {
                    jeu.setView("Plateau");
                }
                jeu.notifyObservers();
            } catch (IOException e) {
                DataUtils.logException(e, "Erreur lors du chargement de la partie");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        lesthemes.setVisible(false);
        choisirThemeButton.getStyleClass().remove("button-selected");
    }

    // Popup Fonctions
    @FXML
    private void handleOKPopup() {
        popup.setVisible(false);
        System.out.println("on clique mais ça marche pas");
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("MenuInitial")) {
            resetParametre();
        }
    }
}
