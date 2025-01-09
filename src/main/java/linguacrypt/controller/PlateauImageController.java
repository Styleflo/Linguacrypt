package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import linguacrypt.config.GameConfig;
import linguacrypt.model.Carte;
import linguacrypt.model.CarteBase;
import linguacrypt.model.CarteImage;
import linguacrypt.model.Jeu;
import linguacrypt.utils.CardType;
import linguacrypt.utils.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlateauImageController implements Observer {
    private Jeu jeu;

    @FXML
    private GridPane gridPane;
    @FXML
    private Label labelEquipe;
    @FXML
    private Label lbbleu;
    @FXML
    private Label lbred;
    @FXML
    private Pane panneau_changer2;
    @FXML
    private AnchorPane panneau_changer;
    @FXML
    private ImageView imageview1;
    @FXML
    private ImageView imageview2;
    @FXML
    private ImageView filtre;
    @FXML
    private ImageView filtre2;
    @FXML
    private Pane confirmationOverlay;
    @FXML
    private Pane confirmationOverlayMenu;
    @FXML
    private Pane confirmationOverlayMenuSave;
    @FXML
    private Pane popupWin;
    @FXML
    private Label whoWon;
    @FXML
    private Button colorButton;
    @FXML
    private VBox borderWin;

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
        confirmationOverlayMenuSave.setVisible(false);
    }

    @FXML
    private void cancelSavePartie() {
        confirmationOverlayMenu.setVisible(false);
    }

    @FXML
    private void returnMenu() {
        confirmationOverlayMenu.setVisible(false);
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @FXML
    private void closeConfirmationMenu() {
        confirmationOverlayMenu.setVisible(false);
    }

    @FXML
    private void okWin() {
        popupWin.setVisible(false);
    }

    public void PlateauImageControlleur() {
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

    private AnchorPane findAnchorCard(String url) {
        List<AnchorPane> cartes = recupererCartes();
        for (AnchorPane carte : cartes) {
            ImageCardController controller = (ImageCardController) carte.getUserData();
            if (controller.getImageUrl() != null && controller.getImageUrl().equals(url)) {
                return carte;
            }
        }
        return null;
    }


    private void afficherCartes() {
        filtre.setMouseTransparent(true);
        filtre2.setMouseTransparent(true);
        if (jeu.getPartie().getPlateau().isBlueTurn()) {
            imageview1.setVisible(true);  // Si visible, devient inv
            imageview2.setVisible(false);  // Si visible, devient inv
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("blue_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_bleu");
            panneau_changer2.getStyleClass().add("logo_panneau");
        } else {
            imageview1.setVisible(false);  // Si visible, devient inv
            imageview2.setVisible(true);  // Si visible, devient inv
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("red_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_rouge");
            panneau_changer2.getStyleClass().add("logo_panneau");
        }

        DataUtils.assertNotNull(jeu, "Jeu non initialisé dans PlateauController.afficherCartes()");

        gridPane.getChildren().clear();

        int row = jeu.getPartie().getWidthParameter();
        int col = jeu.getPartie().getHeightParameter();

        // dépend de la taille des cartes TAILLLE DES CARTES 0 REGARDER ICI !!!!
        if (row < 7 && col < 6) {

            gridPane.setHgap(GameConfig.PLATEAU_HGAP);
            gridPane.setVgap(GameConfig.PLATEAU_VGAP);

            int adapth = 5 - col;
            int adaptl = 6 - row;
            int right = 60 + adapth * 30;
            int left = 20 + adapth * 30;
            int top = 70 + adaptl * 20;
            int bottom = 70 + adaptl * 20;

            gridPane.setPadding(new Insets(top, right, bottom, left));

            for (int i = 0; i < col; i++) {
                for (int j = 0; j < row; j++) {
                    final int currentI = i;
                    final int currentJ = j;

                    CarteImage carteImage = (CarteImage) jeu.getPartie().getPlateau().getCard(i, j);
                    System.out.println(carteImage.getUrl());
                    AnchorPane carte = creerCarte(carteImage.getUrl());

                    assert carte != null;
                    carte.setOnMouseClicked(event -> handleCardClick(currentI, currentJ, carte));

                    gridPane.add(carte, i, j);
                }
            }
        } else {
            gridPane.setHgap(4);
            gridPane.setVgap(8);
            int adapth = 9 - col;
            int adaptl = 9 - row;
            int right = 61 + adapth * (55 - adapth * 3);
            int left = adapth * (55 - adapth * 3);
            int top = 74 + adaptl * (36 - adaptl * 3);
            int bottom = 74 + adaptl * (36 - adaptl * 3);

            gridPane.setPadding(new Insets(top, right, bottom, left));
            for (int i = 0; i < col; i++) {
                for (int j = 0; j < row; j++) {
                    final int currentI = i;
                    final int currentJ = j;

                    CarteImage carteImage = (CarteImage) jeu.getPartie().getPlateau().getCard(i, j);
                    AnchorPane carte = creerCarte(carteImage.getUrl());

                    assert carte != null;
                    carte.setOnMouseClicked(event -> handleCardClick(currentI, currentJ, carte));

                    gridPane.add(carte, i, j);
                }
            }

        }

        this.updateLabel();
    }

    private void handleCardClick(int x, int y, AnchorPane carte) {
        CarteBase currentCard = jeu.getPartie().getPlateau().getCard(x, y);

        if (currentCard.isCovered()) {
            return;
        }

        if (jeu.getPartie().getwon() == -1) {
            jeu.getPartie().setPartieBegin();
        }

        CardType couleur = currentCard.getType();
        ImageCardController controller = (ImageCardController) carte.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé");

        controller.setRecouvert(couleur, true);
        String style;

        switch (couleur) {
            case CardType.RED:
                style = "-fx-background-color: " + GameConfig.RED_CARD_COLOR + ";";
                carte.setStyle(style);
                jeu.getPartie().getPlateau().updatePoint(CardType.RED);
                jeu.getPartie().getPlateau().updateTurn(CardType.RED);
                // Recouvrir la carte
                break;
            case CardType.BLUE:
                style = "-fx-background-color: " + GameConfig.BLUE_CARD_COLOR + ";";
                carte.setStyle(style);
                jeu.getPartie().getPlateau().updatePoint(CardType.BLUE);
                jeu.getPartie().getPlateau().updateTurn(CardType.BLUE);
                break;
            case CardType.BLACK:
                style = "-fx-background-color: " + GameConfig.BLACK_CARD_COLOR + ";";
                carte.setStyle(style);
                if (jeu.getPartie().getPlateau().isBlueTurn()) {
                    jeu.getPartie().setRedWon();
                } else {
                    jeu.getPartie().setBlueWon();
                }
                break;
            case CardType.WHITE:
                style = "-fx-background-color: " + GameConfig.WHITE_CARD_COLOR + ";";
                carte.setStyle(style);
                jeu.getPartie().getPlateau().updateTurn(CardType.WHITE);
                break;
        }

        jeu.getPartie().updateWin();

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
            for (CarteBase c : row) {
                CarteImage card = (CarteImage) c;
                AnchorPane carteVisu = findAnchorCard(card.getUrl());  // Modifié pour utiliser getUrl()
                if (carteVisu != null) {
                    card.setCovered();
                    ImageCardController controller = (ImageCardController) carteVisu.getUserData();
                    controller.setRecouvert(card.getType(), true);
                }
            }
        }
    }

    private void showWinnerPopup(String winningTeam) {
        if (winningTeam.equals("Rouge")) {
            whoWon.setText("L'équipe Rouge a gagné !");
            whoWon.setStyle("-fx-text-fill: #f70d1a;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("red_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-red");
        }
        else {
            whoWon.setText("L'équipe Bleue a gagné !");
            whoWon.setStyle("-fx-text-fill: #3399FF;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("blue_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-blue");
        }
        popupWin.setVisible(true);
    }

    private AnchorPane creerCarte(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Image_card.fxml"));
            AnchorPane card = loader.load();
            ImageCardController controller = loader.getController();
            card.setUserData(controller);
            System.out.println(url);
            controller.setMyImage(url);
            return card;
        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors de la création d'une carte image");
            return null;
        }
    }

    private AnchorPane creerPetiteCarte(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Image_card.fxml"));
            AnchorPane card = loader.load();
            ImageCardController controller = loader.getController();
            card.setUserData(controller);
            controller.setMyImage(url);
            return card;
        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors de la création d'une carte image");
            e.printStackTrace();
            return null;
        }
    }

    public void updateLabel() {
        if (this.jeu.getPartie().getPlateau().isBlueTurn()) {
            imageview1.setVisible(true);  // Si visible, devient inv
            imageview2.setVisible(false);  // Si visible, devient inv
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("blue_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_bleu");
            panneau_changer2.getStyleClass().add("logo_panneau");
            labelEquipe.setText(GameConfig.BLUE_TURN_TEXT);
            labelEquipe.setText("C'est le tour de Bleu");
        } else {
            imageview1.setVisible(false);  // Si visible, devient inv
            imageview2.setVisible(true);  // Si visible, devient inv
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("red_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_rouge");
            panneau_changer2.getStyleClass().add("logo_panneau");
            labelEquipe.setText(GameConfig.RED_TURN_TEXT);
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
        } else {
            confirmNouvellePartie();
        }
    }

    @FXML
    private void handleMenuPrincipal() {
        if (jeu.getPartie().getwon() != -1) {
            confirmationOverlayMenu.setVisible(true);
        }
        else {
            jeu.setView("MenuInitial");
            jeu.notifyObservers();
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
                System.out.println(fichier.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }
            jeu.setView("MenuInitial");
            jeu.notifyObservers();
        }
    }

    @FXML
    private void handleTourSuivant() {
        jeu.getPartie().getPlateau().changeTurn();
        updateLabel();

        boolean currentVisibility1 = imageview1.isVisible();
        imageview1.setVisible(!currentVisibility1);  // Si visible, devient inv
        boolean currentVisibility2 = imageview2.isVisible();
        imageview2.setVisible(!currentVisibility2);  // Si visible, devient inv
        if (panneau_changer.getStyleClass().get(1).equals("blue_main_panneau")) {
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("red_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_rouge");
            panneau_changer2.getStyleClass().add("logo_panneau");
        } else {
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("blue_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_bleu");
            panneau_changer2.getStyleClass().add("logo_panneau");
        }

    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("PlateauImage")) {
            System.out.println("on passe là");
            afficherCartes();
        }
    }
}
