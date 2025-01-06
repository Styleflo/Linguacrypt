package linguacrypt.controller;

import javafx.fxml.FXML;
import linguacrypt.model.Jeu;


public class MenuInitialController implements Observer {

    private Jeu jeu;

    public MenuInitialController() {

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    @FXML
    private void handleJouerButtonAction() {
        System.out.println("Bouton 'Jouer' cliqué !");
    }

    @FXML
    private void handleCartesButtonAction() {
        jeu.setView("Cartes");
        jeu.notifierObserver();
    }


    @Override
    public void reagir() {

    }
}
