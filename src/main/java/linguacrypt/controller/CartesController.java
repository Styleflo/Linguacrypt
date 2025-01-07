package linguacrypt.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import linguacrypt.model.Jeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartesController implements Observer {

    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    private final List<String> mots;

    public CartesController() {
        mots = new ArrayList<>();
        mots.add("Avion");
        mots.add("Souris");
        mots.add("Carnaval");
        mots.add("Tourniquet");
        mots.add("Avion");
        mots.add("Souris");
        mots.add("Carnaval");
        mots.add("Tourniquet");
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
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(25));

        int row = 0;
        int col = 0;
        int maxCols = 4;




        // Calculer la taille des cartes

        for (int i = 0; i < mots.size(); i++) {
            AnchorPane carte = creerCarte(mots.get(i));

            create_transition(carte);

            gridPane.add(carte, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
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

    public void create_transition(AnchorPane carte){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setToX(10);
        transition.setToY(5);

        carte.setOnMouseEntered(event -> {
            transition.setNode(carte);
            transition.play();
        });

        carte.setOnMouseExited(event -> {
            carte.setTranslateX(0);
            carte.setTranslateY(0);
        });

    }

    @Override
    public void reagir() {
        afficherCartes();
    }
}
