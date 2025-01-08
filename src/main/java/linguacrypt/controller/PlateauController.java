package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import linguacrypt.model.Carte;
import linguacrypt.model.CarteBase;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateauController implements Observer {
    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    private StackPane popupRoot;

    @FXML
    private Label labelEquipe;

    @FXML
    private Label lbbleu;

    @FXML
    private Label lbred;

    private WinnerPopupController winnerPopupController;
    private StackPane popupContainer;

    @FXML
    private Pane confirmationOverlay;

    @FXML
    private Pane confirmationOverlayMenu;

    @FXML
    private Pane confirmationOverlayMenuSave;

    @FXML
    private void confirmNouvellePartie() {
        confirmationOverlay.setVisible(false);
        jeu.getPartie().newPlateau();
        jeu.notifyObservers();
    }

    @FXML
    private void cancelNouvellePartie() {
        confirmationOverlay.setVisible(false);
    }

    @FXML
    private void confirmSavePartie() {
        confirmationOverlayMenu.setVisible(false);
        confirmationOverlayMenuSave.setVisible(true);
        savePartie();
    }

    @FXML
    private void cancelSavePartie() {
        confirmationOverlayMenu.setVisible(false);
    }



    public void PlateauControlleur() {
        // Constructeur par défaut requis pour le contrôleur FXML
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    private List<AnchorPane> recupererCartes() {
        List<AnchorPane> cartes = new ArrayList<>();

        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (node instanceof AnchorPane) {
                cartes.add((AnchorPane) node);
            }
        }

        return cartes;
    }
    private AnchorPane findAnchorCard(String mot) {
        List<AnchorPane> cartes = recupererCartes();
        for (AnchorPane carte : cartes) {
            NeutralCardController controller = (NeutralCardController) carte.getUserData(); // Récupérer le contrôleur associé à la carte
            if (controller.getLabelMot() != null && controller.getLabelMot().getText().equals(mot)) {
                return carte;
            }
        }
        return null; // Si aucune carte ne correspond au mot, on retourne null
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
        if (jeu.getPartie().getPlateau().getCard(x,y).isCovered()) {
            return;
        }
        if (jeu.getPartie().getwon() == -1) {
            jeu.getPartie().setPartieBegin();
        }
        // Récupérer la couleur de la carte depuis le modèle
        int couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();

        // Appliquer le style CSS correspondant et change les points.
        switch (couleur) {
            case 1: //couleur de la carte est rouge
                carte.setStyle("-fx-background-color: #ff6b6b;");
                jeu.getPartie().getPlateau().updatePoint(1);
                jeu.getPartie().getPlateau().updateTurn(1);
                jeu.getPartie().updateWin();

                break;
            case 0: //couleur de la carte est bleue
                carte.setStyle("-fx-background-color: #4dabf7;");
                jeu.getPartie().getPlateau().updatePoint(0);
                jeu.getPartie().getPlateau().updateTurn(0);
                jeu.getPartie().updateWin();
                break;
            case 2: //couleur de la carte est noire (celui qui l'a retourné a perdu)
                carte.setStyle("-fx-background-color: #343a40;");
                jeu.getPartie().getPlateau().updateTurn(2);
                jeu.getPartie().updateWin(2);
                break;
            case 3: //couleur de la carte est neutre
                carte.setStyle("-fx-background-color: #f8f9fa;");
                jeu.getPartie().getPlateau().updateTurn(3);
                break;
        }
        if (jeu.getPartie().BlueWon()) {
            revealCard();
            showWinnerPopup("Bleue");
        }
        if (jeu.getPartie().RedWon()) {
            revealCard();
            showWinnerPopup("Rouge");
        }
        // Marquer la carte comme révélée dans le modèle si nécessaire
        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
        this.updateLabel();
    }

    private void revealCard() {
        CarteBase[][] listCard = jeu.getPartie().getPlateau().getCards();
        for (CarteBase[] row : listCard) {
            for (CarteBase card : row) {
                AnchorPane carteVisu = findAnchorCard(card.getWord());
                if (carteVisu != null) {  //vérification
                    card.setCovered();
                    int color = card.getType();

                    switch (color) {
                        case 1: //couleur de la carte est rouge
                            carteVisu.setStyle("-fx-background-color: #ff6b6b;");
                            break;
                        case 0: //couleur de la carte est bleue
                            carteVisu.setStyle("-fx-background-color: #4dabf7;");
                            break;
                        case 2: //couleur de la carte est noire
                            carteVisu.setStyle("-fx-background-color: #343a40;");
                            break;
                        case 3: //couleur de la carte est neutre
                            carteVisu.setStyle("-fx-background-color: #f8f9fa;");
                            break;
                    }
                }
            }
        }
    }


    private void showWinnerPopup(String winningTeam) {
        try {
            if (popupContainer == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/winner-popup.fxml"));
                popupContainer = loader.load();
                winnerPopupController = loader.getController();

                StackPane parent = (StackPane) gridPane.getParent();

                // On ajoute le popup au StackPane
                parent.getChildren().add(popupContainer);
                popupContainer.toFront(); // Met le popup au premier plan

            }

            winnerPopupController.show(winningTeam);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du popup: " + e.getMessage());
        }
    }

    private AnchorPane creerCarte(String mot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Neutral_card.fxml"));
            AnchorPane card = loader.load();
            NeutralCardController controller = loader.getController();
            card.setUserData(controller);
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
        if (jeu.getPartie().getwon() == 2) {
            confirmationOverlay.setVisible(true);
        }
        else {
            confirmNouvellePartie();
        }
    }

    @FXML
    private void handleMenuPrincipal() {
        if (jeu.getPartie().getwon() == 2) {
            confirmationOverlayMenu.setVisible(true);
        }
        else {
            confirmNouvellePartie();
        }
    }

    private void savePartie() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la partie en cours");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Jeu", "*.alb"));

        File fichier = fileChooser.showSaveDialog(new Stage());
        if (fichier != null) {
            try {
                jeu.getPartie().savePartie(fichier.getAbsolutePath());
                System.out.println("Partie sauvegardé dans : " + fichier.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }
        }
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
