package linguacrypt.controller;

import javafx.fxml.FXML;
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

    private Jeu jeu;

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

        if (Images.isSelected()) {
            partieBuilder.setTypePartie(TypePartie.IMAGES);
            jeu.setView("PlateauImages");
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.notifyObservers();
        } else {
            jeu.setView("Plateau");
            Partie partie = partieBuilder.getResult();
            jeu.setPartie(partie);
            jeu.notifyObservers();
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
    }

    @Override
    public void reagir() {
    }
}
