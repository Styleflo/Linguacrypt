package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import linguacrypt.model.Jeu;

import java.io.IOException;

public class PlateauController implements Observer {
    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelEquipe;

    @FXML
    private Label lbbleu;

    @FXML
    private Label lbred;

    public void PlateauControlleur() {
        // Constructeur par défaut requis pour le contrôleur FXML
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }


    private void afficherCartes() {
        if (jeu == null) return;

        gridPane.getChildren().clear();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(80));

        int row = jeu.getPartie().getWidthParameter();
        int col = jeu.getPartie().getHeightParameter();

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                final int currentI = i;
                final int currentJ = j;
                AnchorPane carte = creerCarte(jeu.getPartie().getPlateau().getCard(i, j).getWord());

                assert carte != null;
                carte.setOnMouseClicked(event -> handleCardClick(currentI, currentJ, carte));

                gridPane.add(carte, i, j);
            }
        }
        this.updateLabel();
    }

    private void handleCardClick(int x, int y, AnchorPane carte) {
        // Récupérer la couleur de la carte depuis le modèle
        int couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();

        // Appliquer le style CSS correspondant et change les points.
        switch (couleur) {
            case 1:
                carte.setStyle("-fx-background-color: #ff6b6b;");
                jeu.getPartie().getPlateau().updatePoint(1);
                jeu.getPartie().getPlateau().updateTurn(1);
                jeu.getPartie().updateWin();

                break;
            case 0:
                carte.setStyle("-fx-background-color: #4dabf7;");
                jeu.getPartie().getPlateau().updatePoint(0);
                jeu.getPartie().getPlateau().updateTurn(0);
                jeu.getPartie().updateWin();
                break;
            case 2:
                carte.setStyle("-fx-background-color: #343a40;");
                jeu.getPartie().getPlateau().updateTurn(2);
                jeu.getPartie().updateWin(2);
                break;
            case 3:
                carte.setStyle("-fx-background-color: #f8f9fa;");
                jeu.getPartie().getPlateau().updateTurn(3);
                break;
        }
        if (jeu.getPartie().BlueWon()) {
            System.out.println("Blue Won");
        }
        if (jeu.getPartie().RedWon()) {
            System.out.println("Red Won");
        }
        // Marquer la carte comme révélée dans le modèle si nécessaire
        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
        this.updateLabel();
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

    public void updateLabel() {
        if (this.jeu.getPartie().getPlateau().isBlueTurn()) {
            labelEquipe.setText("C'est le tour de Bleu");
        } else {
            labelEquipe.setText("C'est le tour de Rouge");
        }
        int nbpoint = jeu.getPartie().getPlateau().getKey().getWidth() * jeu.getPartie().getPlateau().getKey().getHeight() / 3;
        if (this.jeu.getPartie().getPlateau().getKey().isBlueStarting()) {
            lbbleu.setText(jeu.getPartie().getPlateau().getPointBlue() + "/" + (nbpoint + 1));
            lbred.setText(jeu.getPartie().getPlateau().getPointRed() + "/" + nbpoint);
        } else {
            lbbleu.setText(jeu.getPartie().getPlateau().getPointBlue() + "/" + nbpoint);
            lbred.setText(jeu.getPartie().getPlateau().getPointRed() + "/" + (nbpoint + 1));
        }
    }

    @FXML
    private void handleNouvellePartie() {
        jeu.getPartie().newPlateau();
        jeu.notifyObservers();
    }

    @FXML
    private void handleMenuPrincipal() {
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @FXML
    private void handleTourSuivant() {
        jeu.getPartie().getPlateau().changeTurn();
        updateLabel();
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Plateau")) {
            afficherCartes();
        }
    }
}
