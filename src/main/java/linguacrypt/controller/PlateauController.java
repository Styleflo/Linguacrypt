package linguacrypt.controller;

import com.google.zxing.common.BitMatrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import linguacrypt.config.GameConfig;
import linguacrypt.model.*;
import linguacrypt.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ImageView turnQR;
    @FXML
    private ImageView qrCode;
    @FXML
    private ImageView lingualogo;
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
    private Label blueTimer;
    @FXML
    private Label redTimer;

    private Timeline timeline;
    private int blueTimeLeft;
    private int redTimeLeft;
    private boolean isTimerRunning = false;

    private void initializeTimer() {
        if (jeu.getPartie().getTimer() != -1) {
            // Si c'est une nouvelle partie, initialise les temps
            if (jeu.getPartie().getwon() == -1) {
                blueTimeLeft = jeu.getPartie().getTimer() / 2;
                redTimeLeft = jeu.getPartie().getTimer() / 2;
            }
            updateTimerLabels();

            if (timeline == null) {
                timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    if (jeu.getPartie().getPlateau().isBlueTurn()) {
                        blueTimeLeft--;
                    } else {
                        redTimeLeft--;
                    }
                    updateTimerLabels();
                    checkTimeOut();
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
            }
        }
    }

    private void updateTimerLabels() {
        int blueMinutes = blueTimeLeft / 60;
        int blueSeconds = blueTimeLeft % 60;
        int redMinutes = redTimeLeft / 60;
        int redSeconds = redTimeLeft % 60;

        blueTimer.setText(String.format("%02d:%02d", blueMinutes, blueSeconds));
        redTimer.setText(String.format("%02d:%02d", redMinutes, redSeconds));
    }

    private void checkTimeOut() {
        if (blueTimeLeft <= 0) {
            stopTimer();
            jeu.getPartie().setRedWon();
            revealCard();
            showWinnerPopup("Rouge");
        } else if (redTimeLeft <= 0) {
            stopTimer();
            jeu.getPartie().setBlueWon();
            revealCard();
            showWinnerPopup("Bleue");
        }
    }

    private void startTimer() {
        if (jeu.getPartie().getTimer() != -1 && !isTimerRunning) {
            timeline.play();
            isTimerRunning = true;
        }
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.pause();
            isTimerRunning = false;
        }
    }

    @FXML
    private void confirmNouvellePartie() {
        stopTimer();
        confirmationOverlay.setVisible(false);
        jeu.getPartie().newPlateau();
        qrCode.setVisible(false); // Rendre l'ImageView visible si nécessaire
        lingualogo.setVisible(true);
        jeu.getPartie().getPlateau().setqrcodeaffiche(false);
        jeu.getPartie().newPlateau();
        jeu.notifyObservers();

    }

    @FXML
    private void cancelNouvellePartie() {
        if (jeu.getPartie().getwon() == 2) {
            startTimer();
        }
        confirmationOverlay.setVisible(false);
    }

    @FXML
    private void confirmSavePartie() {
        qrCode.setVisible(false); // Rendre l'ImageView visible si nécessaire
        lingualogo.setVisible(true);
        jeu.getPartie().getPlateau().setqrcodeaffiche(false);
        stopTimer();
        confirmationOverlayMenu.setVisible(false);
        confirmationOverlayMenuSave.setVisible(true);
        savePartie();
        confirmationOverlayMenuSave.setVisible(false);
    }

    @FXML
    private void cancelSavePartie() {
        if (jeu.getPartie().getwon() == 2) {
            startTimer();
        }
        confirmationOverlayMenu.setVisible(false);
    }

    @FXML
    private void returnMenu() {
        stopTimer();
        qrCode.setVisible(false); // Rendre l'ImageView visible si nécessaire
        lingualogo.setVisible(true);
        jeu.getPartie().getPlateau().setqrcodeaffiche(false);
        confirmationOverlayMenu.setVisible(false);
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @FXML
    private void closeConfirmationMenu() {
        if (jeu.getPartie().getwon() == 2) {
            startTimer();
        }
        confirmationOverlayMenu.setVisible(false);
    }

    @FXML
    private void okWin() {
        if (jeu.getPartie().getwon() == 2) {
            startTimer();
        }
        popupWin.setVisible(false);
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
        turnQR.setMouseTransparent(true);
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

                    Carte carte = (Carte) jeu.getPartie().getPlateau().getCard(i, j);
                    AnchorPane carteAnchor = creerCarte(carte.getWord());

                    DataUtils.assertNotNull(carteAnchor, "CarteAnchor non initialisé dans PlateauController.afficherCartes()");
                    carteAnchor.setOnMouseClicked(event -> handleCardClick(currentI, currentJ, carteAnchor));
                    carteAnchor.setOnMouseEntered(event -> handleMouseEnter(currentI, currentJ, carteAnchor));
                    carteAnchor.setOnMouseExited(event -> handleMouseExit(currentI, currentJ, carteAnchor));
                    gridPane.add(carteAnchor, i, j);
                    handleMouseEnter(i, j, carteAnchor);

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
                    Carte carte = (Carte) jeu.getPartie().getPlateau().getCard(i, j);
                    AnchorPane carteAnchor = creerPetiteCarte(carte.getWord());

                    DataUtils.assertNotNull(carteAnchor, "CarteAnchor non initialisé dans PlateauController.afficherCartes()");
                    carteAnchor.setOnMouseClicked(event -> handleCardClick(currentI, currentJ, carteAnchor));

                    gridPane.add(carteAnchor, i, j);
                }
            }

        }

        this.updateLabel();
        initializeTimer();
    }

    private void handleMouseEnter(int x, int y, AnchorPane carte) {
        if ((!jeu.getPartie().getPlateau().getCard(x, y).isCovered()) || jeu.getPartie().isWon()) {
            return;
        }
        // Récupérer la couleur de la carte depuis le modèle
        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        NeutralCardController controller = (NeutralCardController) carte.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé dans PlateauController.handleCardClick()");
        controller.setSemiCovered(couleur, true);
        String style;

        this.updateLabel();
    }

    private void handleMouseExit(int x, int y, AnchorPane carte) {
        if ((!jeu.getPartie().getPlateau().getCard(x, y).isCovered()) || jeu.getPartie().isWon()) {
            return;
        }
        // Récupérer la couleur de la carte depuis le modèle
        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        NeutralCardController controller = (NeutralCardController) carte.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé dans PlateauController.handleCardClick()");
        controller.setSemiCovered(couleur, false);
        String style;

        this.updateLabel();
    }


    private void handleCardClick(int x, int y, AnchorPane carte) {
        if (jeu.getPartie().getPlateau().getCard(x, y).isCovered()) {
            return;
        }


        if (jeu.getPartie().getwon() == -1) {
            jeu.getPartie().setPartieBegin();
        }
        // Récupérer la couleur de la carte depuis le modèle
        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        NeutralCardController controller = (NeutralCardController) carte.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé dans PlateauController.handleCardClick()");
        controller.setRecouvert(couleur, true);
        String style;

        switch (couleur) {
            case CardType.RED:
                style = "-fx-background-color: " + GameConfig.RED_CARD_COLOR + ";";
                carte.setStyle(style);
                jeu.getPartie().getPlateau().updatePoint(CardType.RED);
                jeu.getPartie().getPlateau().updateTurn(CardType.RED);
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
                Carte card = (Carte) c;
                AnchorPane carteVisu = findAnchorCard(card.getWord());
                if (carteVisu != null) {
                    card.setCovered();
                    String style = switch (card.getType()) {
                        case CardType.RED -> "-fx-background-color: " + GameConfig.RED_CARD_COLOR + ";";
                        case CardType.BLUE -> "-fx-background-color: " + GameConfig.BLUE_CARD_COLOR + ";";
                        case CardType.BLACK -> "-fx-background-color: " + GameConfig.BLACK_CARD_COLOR + ";";
                        case CardType.WHITE -> "-fx-background-color: " + GameConfig.WHITE_CARD_COLOR + ";";
                    };
                    carteVisu.setStyle(style);
                }
            }
        }
    }

    private void showWinnerPopup(String winningTeam) {
        stopTimer();
        if (winningTeam.equals("Rouge")) {
            whoWon.setText("L'équipe Rouge a gagné !");
            whoWon.setStyle("-fx-text-fill: #f70d1a;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("red_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-red");
        } else {
            whoWon.setText("L'équipe Bleue a gagné !");
            whoWon.setStyle("-fx-text-fill: #3399FF;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("blue_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-blue");
        }
        popupWin.setVisible(true);
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

    private AnchorPane creerPetiteCarte(String mot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Neutral_card_vp.fxml"));
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
        // todo : mettre qui gagne en cas de victoire
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
        stopTimer();
        if (jeu.getPartie().getwon() == 2) {
            confirmationOverlay.setVisible(true);
        } else {
            confirmNouvellePartie();
            qrCode.setVisible(false); // Rendre l'ImageView visible si nécessaire
            lingualogo.setVisible(true);
            jeu.getPartie().getPlateau().setqrcodeaffiche(false);
        }
    }

    @FXML
    private void handleMenuPrincipal() {
        if (jeu.getPartie().getwon() == 2 ) {
            confirmationOverlayMenu.setVisible(true);
        } else {
            qrCode.setVisible(false); // Rendre l'ImageView visible si nécessaire
            lingualogo.setVisible(true);
            jeu.getPartie().getPlateau().setqrcodeaffiche(false);
            jeu.setView("MenuInitial");
            jeu.notifyObservers();
        }
    }

    /**
     * La fonction permet de sauvegarder une partie non fini q
     * Elle lit l'etat de la partie actuelle et sauvegarde le tout en format Json
     */
    private void savePartie() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la partie en cours");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Partie", "*.json"));
        File fichier = fileChooser.showSaveDialog(new Stage());
        if (fichier != null) {
            try {
                FileSaveDeleteHandler filesavehandler = new FileSaveDeleteHandler();
                filesavehandler.savePartie(jeu.getPartie(), fichier.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }
            jeu.setView("MenuInitial");
            jeu.notifyObservers();
        }
    }

    @FXML
    private void handleTourSuivant() {
        if (isTimerRunning) {
            stopTimer();
            startTimer();
        }
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

    @FXML
    public void afficheQRcode() {
        try {
            BitMatrix qrcode = jeu.getPartie().getPlateau().getKey().to_qrcode(); // Génération du QR code
            WritableImage qrImage = jeu.getPartie().getPlateau().getKey().bitMatrixToImage(qrcode); // Convertit le BitMatrix en WritableImage
            qrCode.setImage(qrImage); // Affiche l'image dans l'ImageView
            qrCode.setVisible(true); // Rendre l'ImageView visible si nécessaire
            lingualogo.setVisible(false);
        } catch (Exception e) {
            DataUtils.logException(e, "Erreur lors de la génération du QR code");
        }
        qrCode.setVisible(!jeu.getPartie().getPlateau().isqrcodeaffiche()); // Rendre l'ImageView visible si nécessaire
        lingualogo.setVisible(jeu.getPartie().getPlateau().isqrcodeaffiche());
        jeu.getPartie().getPlateau().setqrcodeaffiche(!jeu.getPartie().getPlateau().isqrcodeaffiche());
    }


    @Override
    public void reagir() {
        if (jeu.getView().equals("Plateau")) {
            afficherCartes();
            // Si c'est une nouvelle partie (pas de carte révélée)
            if (jeu.getPartie().getwon() == -1) {
                initializeTimer();
                startTimer(); // Démarre le timer immédiatement
            }
        }
    }
}
