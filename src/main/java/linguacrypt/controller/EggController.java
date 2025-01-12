package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;

import java.io.IOException;

public class EggController implements Observer {
    private Jeu jeu;
    @FXML
    private ImageView filtre;


    public EggController() {
    }

    public void setJeu(Jeu jeu) {
        filtre.setMouseTransparent(true);
        this.jeu = jeu;
    }


    @FXML
    private void handleQuitterButtonAction() throws IOException {
        System.out.println("passe");

        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @Override
    public void reagir() {
    }
}