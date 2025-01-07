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
                final int currentI = i;
                final int currentJ = j;
            AnchorPane carte = creerCarte(jeu.getPartie().getPlateau().getCard(i,j).getWord());

            carte.setOnMouseClicked(event -> {
            handleCardClick(currentI, currentJ, carte);
            });

            gridPane.add(carte, i, j);
        }
        }
    }

    private void handleCardClick(int x, int y, AnchorPane carte) {
        // Récupérer la couleur de la carte depuis le modèle
        int couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();

        // Appliquer le style CSS correspondant
        switch (couleur) {
            case 1:
                carte.setStyle("-fx-background-color: #ff6b6b;");
                break;
            case 0:
                carte.setStyle("-fx-background-color: #4dabf7;");
                break;
            case 3:
                carte.setStyle("-fx-background-color: #343a40;");
                break;
            case 2:
                carte.setStyle("-fx-background-color: #f8f9fa;");
                break;
        }

        // Marquer la carte comme révélée dans le modèle si nécessaire
        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
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

    @Override
    public void reagir() {
        afficherCartes();
    }

}
