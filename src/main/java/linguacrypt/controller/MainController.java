package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Jeu;

import java.io.IOException;


public class MainController implements Observer {

    private Jeu jeu;

    @FXML
    private VBox menuContainer;

    @FXML
    private AnchorPane content;

    private StackPane menuInitialRoot;

    private StackPane plateauRoot;


    private StackPane cartesRoot;

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

            AnchorPane.setTopAnchor(menuInitialRoot, 0.0);
            AnchorPane.setBottomAnchor(menuInitialRoot, 0.0);
            AnchorPane.setLeftAnchor(menuInitialRoot, 0.0);
            AnchorPane.setRightAnchor(menuInitialRoot, 0.0);

            content.getChildren().clear();
            content.getChildren().add(menuInitialRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCartes() {
        // Charger le fichier FXML du menu et obtenir le contrôleur Cartes
        try {
            FXMLLoader menuInitialLoader = new FXMLLoader(getClass().getResource("/view/cartes.fxml"));
            cartesRoot = menuInitialLoader.load();
            CartesController cartes = menuInitialLoader.getController();
            cartes.setJeu(jeu);
            jeu.addObserver(cartes);

            AnchorPane.setTopAnchor(cartesRoot, 0.0);
            AnchorPane.setBottomAnchor(cartesRoot, 0.0);
            AnchorPane.setLeftAnchor(cartesRoot, 0.0);
            AnchorPane.setRightAnchor(cartesRoot, 0.0);

            content.getChildren().clear();
            content.getChildren().add(menuInitialRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlateau() {
        // Charger le fichier FXML du menu et obtenir le contrôleur Plateau
        try {
            FXMLLoader plateauLoader = new FXMLLoader(getClass().getResource("/view/plateau.fxml"));
            plateauRoot = plateauLoader.load();
            PlateauController plateau = plateauLoader.getController();
            plateau.setJeu(jeu);
            jeu.addObserver(plateau);

            AnchorPane.setTopAnchor(plateauRoot, 0.0);
            AnchorPane.setBottomAnchor(plateauRoot, 0.0);
            AnchorPane.setLeftAnchor(plateauRoot, 0.0);
            AnchorPane.setRightAnchor(plateauRoot, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        content.getChildren().clear();
        if (this.jeu.getView() == "MenuInitial") {
            content.getChildren().add(menuInitialRoot);
        } else if (this.jeu.getView() == "Cartes") {
            content.getChildren().add(cartesRoot);
        } else if (this.jeu.getView() == "Plateau") {
            content.getChildren().add(plateauRoot);
        } else {
            System.out.println(jeu.getView());
        }
    }

    public void reagir() {
        update();
    }
}


