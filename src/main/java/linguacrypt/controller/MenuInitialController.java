package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;
import linguacrypt.model.Partie;
import linguacrypt.model.PartieBuilder;

import java.io.IOException;


public class MenuInitialController implements Observer {

    private Jeu jeu;

    public MenuInitialController() {
    }

    public void setJeu(Jeu jeu) {
        filtre.setMouseTransparent(true);
        this.jeu = jeu;
    }

    @FXML
    private ImageView filtre;

    @FXML
    private void handleJouerButtonAction() throws IOException {
        jeu.setView("Plateau");

        PartieBuilder partieBuilder = new PartieBuilder(jeu);
        Partie partie = partieBuilder.getResult();
        jeu.setPartie(partie);
        jeu.notifyObservers();

    }

    @FXML
    private void handleCartesButtonAction() {
        jeu.setView("Cartes");
        jeu.notifyObservers();

    }

    @FXML
    private void handleQuitterButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @Override
    public void reagir() {

    }
}
