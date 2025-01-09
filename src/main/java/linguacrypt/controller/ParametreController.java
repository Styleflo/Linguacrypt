package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;
import linguacrypt.model.TypePartie;
import linguacrypt.utils.WordsFileHandler;

import java.io.IOException;
import java.util.ArrayList;

public class ParametreController implements Observer {

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
    private Label labelTimer;
    private final int MIN_TIME = 30; // 30 secondes
    private final int MAX_TIME = 1800; // 30 minutes
    private int currentTime = -1; // -1 signifie infini

    @FXML
    private Button choisirThemeButton;
    public void handleFlecheGaucheTimer() {
        if (currentTime == -1) {
            currentTime = MAX_TIME;
        } else if (currentTime > MIN_TIME) {
            // Réduit de 30 secondes
            currentTime = Math.max(MIN_TIME, currentTime - 30);
        }
        else {
            currentTime = -1;
        }
        updateTimerLabel();
    }

    @FXML
    private Button themesAleatoiresButton;
    public void handleFlecheDroiteTimer() {
        if (currentTime == -1) {
            currentTime = MIN_TIME;
        } else if (currentTime < MAX_TIME) {
            // Augmente de 30 secondes
            currentTime = Math.min(MAX_TIME, currentTime + 30);
        }
        else {
            currentTime = -1;
        }
        updateTimerLabel();
    }

    private Jeu jeu;
    private void updateTimerLabel() {
        if (currentTime == -1) {
            labelTimer.setText("∞");
        } else {
            int minutes = currentTime / 60;
            int seconds = currentTime % 60;
            labelTimer.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }

    private PartieBuilder partieBuilder;

    private ArrayList<String> themes;

    public ParametreController() {
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void setPartieBuilder() throws IOException {
        this.partieBuilder = new PartieBuilder(jeu);
        filtre.setMouseTransparent(true);
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
        }
    }

    @FXML
    public void handleFlecheDroitelb1() {
        int valeurActuelle = Integer.parseInt(label1.getText());
        if (valeurActuelle < 8) {
            label1.setText(String.valueOf(valeurActuelle + 1));
            partieBuilder.setHeightParameter(valeurActuelle + 1);
        }
    }

    @FXML
    public void handleFlecheGauchelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle > 2) {
            label2.setText(String.valueOf(valeurActuelle - 1));
            partieBuilder.setWidthParameter(valeurActuelle - 1);
        }
    }

    @FXML
    public void handleFlecheDroitelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle < 8) {
            label2.setText(String.valueOf(valeurActuelle + 1));
            partieBuilder.setWidthParameter(valeurActuelle + 1);
        }
    }

    @FXML
    public void handleThemes() {
        lesthemes.setVisible(true);
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();
        themes = wordsFileHandler.getAllThemes();
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

            // Empêcher la propagation du clic de la checkbox à l'élément parent
            checkBox.setOnMouseClicked(event -> {
                event.consume();
            });

            themeItem.getChildren().addAll(checkBox, label);
            themeBox.getChildren().add(themeItem);
        }
        choisirThemeButton.getStyleClass().add("button-selected");
        themesAleatoiresButton.getStyleClass().remove("button-selected");
    }

    @FXML
    private void handleValider() {
        ArrayList<String> selectedThemes = new ArrayList<>();
        for (javafx.scene.Node node : themeBox.getChildren()) {
            if (node instanceof HBox hbox) {
                for (javafx.scene.Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox checkBox) {
                        if (checkBox.isSelected()) {
                            Label label = (Label) hbox.getChildren().get(1);
                            selectedThemes.add(label.getText());
                        }
                    }
                }
            }
        }
        partieBuilder.setUsedThemes(selectedThemes);
        lesthemes.setVisible(false);
    }

    @FXML
    private void handleValiderTout() throws IOException {

        if (Images.isSelected() && !Mots.isSelected()) {
            partieBuilder.setTimer(currentTime);
            partieBuilder.resetTypePartie();
            partieBuilder.setTypePartie(TypePartie.IMAGES);
            jeu.setView("PlateauImage");
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.notifyObservers();
        } else if(Mots.isSelected() && !Images.isSelected()) {
            partieBuilder.setTimer(currentTime);
            partieBuilder.resetTypePartie();
            jeu.setView("Plateau");
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.notifyObservers();
        } else {
            // eventuellement pop up à mettre un jour
        }
    }

    @FXML
    private void handleMenu(){
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @FXML
    private void handleAnnuler() {
        lesthemes.setVisible(false);
        choisirThemeButton.getStyleClass().remove("button-selected");
    }

    @Override
    public void reagir() {
    }
}
