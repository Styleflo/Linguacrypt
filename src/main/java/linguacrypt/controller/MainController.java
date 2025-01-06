package linguacrypt.controller;

import linguacrypt.model.Jeu;


import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class MainController implements Observer {

    private Jeu jeu;

    @FXML
    private VBox menuContainer;

    @FXML
    private AnchorPane content;

    public void MainControlleur() {
        // Constructeur par défaut requis pour le contrôleur FXML
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }


    @Override
    public void reagir() {

    }
}


