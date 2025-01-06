package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Jeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartesController implements Observer {

    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    private List<String> mots;

    public CartesController() {
        mots = new ArrayList<>();
        mots.add("Avion");
        mots.add("Souris");
        mots.add("Carnaval");
        mots.add("Tourniquet");
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    private void afficherCartes() {
        if (jeu == null) return;

        gridPane.getChildren().clear();
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        gridPane.setPadding(new Insets(25));

        int row = 0;
        int col = 0;
        int maxCols = 2;

        // Calculer la taille des cartes
        double cardWidth = (900 - (2 * 25) - ((maxCols - 1) * 50)) / maxCols;
        double cardHeight = cardWidth * 0.75;  // ratio 4:3

        for (int i = 0; i < mots.size(); i++) {
            VBox carte = creerCarte(mots.get(i));
            carte.setPrefSize(cardWidth, cardHeight);

            gridPane.add(carte, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    private VBox creerCarte(String mot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/neutral_card.fxml"));
            VBox card = loader.load();
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
