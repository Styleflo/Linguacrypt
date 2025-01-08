package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Jeu;
import linguacrypt.utils.DataUtils;

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

    private StackPane parametresRoot;

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
            DataUtils.logException(e, "Erreur lors du chargement du menu initial");
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
            DataUtils.logException(e, "Erreur lors du chargement des cartes");
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

    public void setParametre() {
        // Charger le fichier FXML du menu et obtenir le contrôleur Plateau
        try {
            FXMLLoader parametresLoader = new FXMLLoader(getClass().getResource("/view/parametres.fxml"));
            parametresRoot = parametresLoader.load();
            ParametreController parametres = parametresLoader.getController();
            parametres.setJeu(jeu);
            jeu.addObserver(parametres);

            // faire le builder pour la partie
            parametres.setPartieBuilder();

            AnchorPane.setTopAnchor(parametresRoot, 0.0);
            AnchorPane.setBottomAnchor(parametresRoot, 0.0);
            AnchorPane.setLeftAnchor(parametresRoot, 0.0);
            AnchorPane.setRightAnchor(parametresRoot, 0.0);

        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors du chargement du plateau");
        }
    }

    public void update() {
        content.getChildren().clear();
        switch (this.jeu.getView()) {
            case "MenuInitial" -> content.getChildren().add(menuInitialRoot);
            case "Cartes" -> content.getChildren().add(cartesRoot);
            case "Plateau" -> content.getChildren().add(plateauRoot);
            case "Parametres" -> content.getChildren().add(parametresRoot);
            case null, default -> System.out.println(jeu.getView());
        }
    }

    public void reagir() {
        update();
    }
}


