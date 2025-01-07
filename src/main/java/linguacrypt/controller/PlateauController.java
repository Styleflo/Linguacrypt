package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Jeu;

import java.io.IOException;

public class PlateauController implements Observer {

    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    public void PlateauControlleur() {
        // Constructeur par défaut requis pour le contrôleur FXML
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void updateTeam(){

    }

    private void afficherCartes() {
        if (jeu == null) return;

        gridPane.getChildren().clear();
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        gridPane.setPadding(new Insets(25));

        int row = jeu.getPartie().getHeightParameter();
        int col = jeu.getPartie().getWidthParameter();

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {

            AnchorPane carte = creerCarte(jeu.getPartie().getPlateau().getCard(i,j).getWord());
            gridPane.add(carte, i, j);
        }
        }
    }

    private AnchorPane creerCarte(String mot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Neutral_card.fxml"));
            AnchorPane card = loader.load();
            NeutralCardController controller = loader.getController();
            controller.setMot(mot);
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void handleNouvellePartie() throws IOException {
        jeu.getPartie().newPlateau();
        jeu.notifyObservers();
    }

    @FXML
    private void handleMenuPrincipal(){
        this.jeu.setView("MenuInitial");
        this.jeu.notifyObservers();
    }

    @Override
    public void reagir() {
        afficherCartes();
    }

}
