package linguacrypt.controller;

import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.io.IOException;

public class ParametreController implements Observer {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    private Jeu jeu;

    private PartieBuilder partieBuilder;

    public ParametreController(){}

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void setPartieBuilder() throws IOException {
        this.partieBuilder = new PartieBuilder(jeu);
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
        }
        partieBuilder.setHeightParameter(valeurActuelle - 1);
    }

    @FXML
    public void handleFlecheDroitelb1() {
        int valeurActuelle = Integer.parseInt(label1.getText());
        if (valeurActuelle < 8) {
            label1.setText(String.valueOf(valeurActuelle + 1));
        }
        partieBuilder.setHeightParameter(valeurActuelle +1);
    }

    @FXML
    public void handleFlecheGauchelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle > 2) {
            label2.setText(String.valueOf(valeurActuelle - 1));
        }
    }

    @FXML
    public void handleFlecheDroitelb2() {
        int valeurActuelle = Integer.parseInt(label2.getText());
        if (valeurActuelle < 8) {
            label2.setText(String.valueOf(valeurActuelle + 1));
        }
    }

    @Override
    public void reagir() {
    }
}