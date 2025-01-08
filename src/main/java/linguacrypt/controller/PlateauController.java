package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import linguacrypt.config.GameConfig;
import linguacrypt.model.CarteBase;
import linguacrypt.model.Jeu;
import linguacrypt.utils.CardType;
import linguacrypt.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        DataUtils.assertNotNull(jeu, "Jeu non initialisé dans PlateauController.afficherCartes()");

        gridPane.getChildren().clear();
        gridPane.setHgap(GameConfig.PLATEAU_HGAP);
        gridPane.setVgap(GameConfig.PLATEAU_VGAP);
        gridPane.setPadding(new Insets(GameConfig.PLATEAU_PADDING));

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
        if (jeu.getPartie().getPlateau().getCard(x, y).isCovered()) {
            return;
        }

        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        String style = "";
        int points = -1;
        CardType nextTurn = couleur;

        switch (couleur) {
            case CardType.RED:
                style = "-fx-background-color: #ff6b6b;";
                points = 1;
                break;
            case CardType.BLUE:
                style = "-fx-background-color: #4dabf7;";
                points = 0;
                break;
            case CardType.BLACK:
                style = "-fx-background-color: #343a40;";
                nextTurn = CardType.BLACK;
                break;
            case CardType.WHITE:
                style = "-fx-background-color: #f8f9fa;";
                nextTurn = CardType.RED;
                break;
        }

        carte.setStyle(style);

        if (points != -1) {
            jeu.getPartie().getPlateau().updatePoint(points);
        }

        jeu.getPartie().getPlateau().updateTurn(nextTurn);
        jeu.getPartie().updateWin();

        if (jeu.getPartie().BlueWon()) {
            revealCard();
            showWinnerPopup("Bleue");
        } else if (jeu.getPartie().RedWon()) {
            revealCard();
            showWinnerPopup("Rouge");
        }

        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
        this.updateLabel();
    }

    private void revealCard() {
        CarteBase[][] listCard = jeu.getPartie().getPlateau().getCards();
        for (CarteBase[] row : listCard) {
            for (CarteBase card : row) {
                AnchorPane carteVisu = findAnchorCard(card.getWord());
                if (carteVisu != null) {
                    card.setCovered();
                    String style = switch (card.getType()) {
                        case CardType.RED -> "-fx-background-color: #ff6b6b;";
                        case CardType.BLUE -> "-fx-background-color: #4dabf7;";
                        case CardType.BLACK -> "-fx-background-color: #343a40;";
                        case CardType.WHITE -> "-fx-background-color: #f8f9fa;";
                    };
                    carteVisu.setStyle(style);
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
            DataUtils.logException(e, "Erreur lors de l'affichage du popup de fin de partie");
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
            DataUtils.logException(e, "Erreur lors de la création d'une carte");
            return null;
        }
    }

    public void updateLabel() {
        if (this.jeu.getPartie().getPlateau().isBlueTurn()) {
            labelEquipe.setText(GameConfig.BLUE_TURN_TEXT);
        } else {
            labelEquipe.setText(GameConfig.RED_TURN_TEXT);
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
