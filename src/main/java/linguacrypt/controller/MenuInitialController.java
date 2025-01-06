package linguacrypt.controller;

import javafx.fxml.FXML;
import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;


public class MenuInitialController implements Observer {

    private Jeu jeu;

    public MenuInitialController() {

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    @FXML
    private void handleJouerButtonAction() {
        jeu.setView("Plateau");
        System.out.println("avant");
        PartieBuilder partieBuilder = new PartieBuilder();
        Partie partie = partieBuilder.build();
        jeu.setPartie(partie);
        System.out.println("passe");
        jeu.notifyObservers();
    }

    @FXML
    private void handleCartesButtonAction() {
        jeu.setView("Cartes");
        jeu.notifyObservers();
    }


    @Override
    public void reagir() {

    }
}
