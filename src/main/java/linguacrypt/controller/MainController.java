package linguacrypt.controller;

import javafx.fxml.FXMLLoader;
import linguacrypt.model.Jeu;


import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class MainController implements Observer {

    private Jeu jeu;

    @FXML
    private VBox menuContainer;

    @FXML
    private AnchorPane content;

    private AnchorPane menuInitialRoot;

    public void MainControlleur() {
        // Constructeur par défaut requis pour le contrôleur FXML
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }


    public void setMenuInitial() {
        // Charger le fichier FXML du menu et obtenir le contrôleur MenuInitial
        try {
            FXMLLoader menuInitialLoader = new FXMLLoader(getClass().getResource("/view/menuInitial.fxml"));
            menuInitialRoot = menuInitialLoader.load();
            MenuInitialController menuinitial = menuInitialLoader.getController();
            menuinitial.setJeu(jeu);
            jeu.addObserver(menuinitial);
            content.getChildren().clear();
            content.getChildren().add(menuInitialRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        content.getChildren().clear();
        if (this.jeu.getView() == "MenuInitial") {
            content.getChildren().add(menuInitialRoot);
        } else {
            System.out.println(jeu.getView());
        }
    }

    public void reagir() {
        update();
    }
}


