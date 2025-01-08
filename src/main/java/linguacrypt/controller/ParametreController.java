package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;
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

    public void handleCartesAleatoire() throws IOException {
        jeu.setView("Plateau");
        Partie partie = partieBuilder.getResult();
        jeu.setPartie(partie);
        jeu.notifyObservers();
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
            Label label = new Label(theme);
            label.setStyle("-fx-text-fill: white;");
            CheckBox checkBox = new CheckBox();
            HBox themeItem = new HBox(checkBox, label);
            themeItem.setSpacing(10);
            themeBox.getChildren().add(themeItem);
        }

        Button validerButton = new Button("Valider");
        validerButton.setOnAction(event -> {
            try {
                handleValider();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button annulerButton = new Button("Annuler");
        annulerButton.setOnAction(event -> handleAnnuler());

        HBox buttonBox = new HBox(10, validerButton, annulerButton);
        buttonBox.setSpacing(10);
        themeBox.getChildren().add(buttonBox);
    }

    @FXML
    private void handleValider() throws IOException {
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
        jeu.setView("Plateau");
        Partie partie = partieBuilder.getResult();
        jeu.setPartie(partie);
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
