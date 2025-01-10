package linguacrypt.controller;

import com.google.zxing.common.BitMatrix;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import linguacrypt.config.GameConfig;
import linguacrypt.model.Carte;
import linguacrypt.model.CarteBase;
import linguacrypt.model.CarteImage;
import linguacrypt.model.GameStatistics;
import linguacrypt.model.Jeu;
import linguacrypt.utils.CardType;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.FileSaveDeleteHandler;

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

    private boolean isTimerRunning = false;

    private void initializeTimer() {
        if (jeu.getPartie().getTimer() != -1) {
            // Si c'est une nouvelle partie, initialise les temps
            if (jeu.getPartie().getwon() == -1) {
                jeu.getPartie().setBlueTimeLeft ((jeu.getPartie().getTimer()/2 ));
                jeu.getPartie().setRedTimeLeft ((jeu.getPartie().getTimer() /2));
            }
            updateTimerLabels();

            if (timeline == null) {
                timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    if (jeu.getPartie().getPlateau().isBlueTurn()) {
                        jeu.getPartie().setBlueTimeLeft(jeu.getPartie().getBlueTimeLeft()-1);
                    } else {
                        jeu.getPartie().setRedTimeLeft(jeu.getPartie().getRedTimeLeft()-1);
                    }
                    updateTimerLabels();
                    checkTimeOut();
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
            }
        } else {

            blueTimer.setVisible(false);
            redTimer.setVisible(false);

            jeu.getPartie().setBlueTimeLeft (0);
            jeu.getPartie().setRedTimeLeft (0);

        }
    }

    private void updateTimerLabels() {
        int blueMinutes = jeu.getPartie().getBlueTimeLeft()/ 60;
        int blueSeconds = jeu.getPartie().getBlueTimeLeft() % 60;
        int redMinutes = jeu.getPartie().getRedTimeLeft() / 60;
        int redSeconds = jeu.getPartie().getRedTimeLeft() % 60;

        blueTimer.setText(String.format("%02d:%02d", blueMinutes, blueSeconds));
        redTimer.setText(String.format("%02d:%02d", redMinutes, redSeconds));
    }

    private void checkTimeOut() {
        if (jeu.getPartie().getBlueTimeLeft() <= 0) {
            stopTimer();
            jeu.getPartie().setRedWon();
            revealCard();
            jeu.victoireRouge();
            labelEquipe.setText("Victoire des rouges !");
            GameStatistics stats = jeu.getCurrentGameStats();
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();

                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }

            showWinnerPopup("Rouge");
        } else if (jeu.getPartie().getRedTimeLeft() <= 0) {
            stopTimer();
            jeu.getPartie().setBlueWon();
            revealCard();
            jeu.victoireBleue();
            labelEquipe.setText("Victoire des bleus !");
            GameStatistics stats = jeu.getCurrentGameStats();
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();

                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }

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
        jeu.initializeNewGameStatistics();
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
                    carteAnchor.setOnMouseEntered(event -> handleMouseEnter(currentI, currentJ, carteAnchor));
                    carteAnchor.setOnMouseExited(event -> handleMouseExit(currentI, currentJ, carteAnchor));
                    gridPane.add(carteAnchor, i, j);
                    afficheCardClicked(i, j, carteAnchor, carte);
                }
            }

        }

        this.updateLabel();
        initializeTimer();
    }

    public void afficheCardClicked(int x, int y, AnchorPane carteAnchor, Carte carte) {
        if (!carte.isCovered()) {
            return;
        }

        CarteBase currentCard = jeu.getPartie().getPlateau().getCard(x, y);


        if (jeu.getPartie().getwon() == -1) {
            jeu.getPartie().setPartieBegin();
        }

        CardType couleur = currentCard.getType();
        NeutralCardController controller = (NeutralCardController) carteAnchor.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé");

        controller.setRecouvert(couleur, true);
        String style;

        switch (couleur) {
            case CardType.RED:
                style = "-fx-background-color: " + GameConfig.RED_CARD_COLOR + ";";
                carteAnchor.setStyle(style);
                jeu.getPartie().getPlateau().updatePoint(CardType.RED);
                jeu.getPartie().getPlateau().updateTurn(CardType.RED);
                // Recouvrir la carte
                break;
            case CardType.BLUE:
                style = "-fx-background-color: " + GameConfig.BLUE_CARD_COLOR + ";";
                carteAnchor.setStyle(style);
                jeu.getPartie().getPlateau().updatePoint(CardType.BLUE);
                jeu.getPartie().getPlateau().updateTurn(CardType.BLUE);
                break;
            case CardType.BLACK:
                style = "-fx-background-color: " + GameConfig.BLACK_CARD_COLOR + ";";
                carteAnchor.setStyle(style);
                if (jeu.getPartie().getPlateau().isBlueTurn()) {
                    jeu.getPartie().setRedWon();
                } else {
                    jeu.getPartie().setBlueWon();
                }
                break;
            case CardType.WHITE:
                style = "-fx-background-color: " + GameConfig.WHITE_CARD_COLOR + ";";
                carteAnchor.setStyle(style);
                jeu.getPartie().getPlateau().updateTurn(CardType.WHITE);
                break;
        }

        jeu.getPartie().updateWin();

        if (jeu.getPartie().BlueWon()) {
            revealCard();
            jeu.victoireBleue();
            GameStatistics stats = jeu.getCurrentGameStats();
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();

                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }
            showWinnerPopup("Bleue");
        }
        if (jeu.getPartie().RedWon()) {
            revealCard();
            jeu.victoireRouge();
            GameStatistics stats = jeu.getCurrentGameStats();
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();

                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }
            showWinnerPopup("Rouge");
        }
        this.updateLabel();
    }

    private void handleMouseEnter(int x, int y, AnchorPane carte) {
        if ((!jeu.getPartie().getPlateau().getCard(x, y).isCovered())){
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
        if ((!jeu.getPartie().getPlateau().getCard(x, y).isCovered())) {
            return;
        }
        // Récupérer la couleur de la carte depuis le modèle
        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        NeutralCardController controller = (NeutralCardController) carte.getUserData();
        DataUtils.assertNotNull(controller, "Contrôleur de carte non initialisé dans PlateauController.handleCardClick()");
        controller.setSemiCovered(couleur, false);

        this.updateLabel();
    }



    private void handleCardClick(int x, int y, AnchorPane carte) {
        if (jeu.getPartie().getPlateau().getCard(x, y).isCovered()|| jeu.getPartie().isWon()) {
            return;
        }

        if (jeu.getPartie().getwon() == -1) {
            jeu.getPartie().setPartieBegin();
        }

        // Récupérer la couleur de la carte depuis le modèle
        String audioFile = getClass().getResource("/soundtrack/carte2.mp3").toExternalForm();

        Media media = new Media(audioFile);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        CardType couleur = jeu.getPartie().getPlateau().getCard(x, y).getType();
        boolean isBlueTurn = jeu.getPartie().getPlateau().isBlueTurn();

        // Mettre à jour les statistiques
        GameStatistics stats = jeu.getCurrentGameStats();
        if (stats != null && !jeu.getPartie().getPlateau().getCard(x, y).isCovered()) {
            if (isBlueTurn) {
                stats.addBlueGuess(couleur == CardType.BLUE);
                if (couleur == CardType.RED) {
                    stats.cardGivenToOpponent(true);
                }
            } else {
                stats.addRedGuess(couleur == CardType.RED);
                if (couleur == CardType.BLUE) {
                    stats.cardGivenToOpponent(false);
                }
            }
        }
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
        updateLabel();
        jeu.getPartie().updateWin();

        if (jeu.getPartie().BlueWon()) {
            jeu.victoireBleue();
            labelEquipe.setText("Victoire des bleus !");
            revealCard();
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();


                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }
            showWinnerPopup("Bleue");
        }
        if (jeu.getPartie().RedWon()) {
            jeu.victoireRouge();
            labelEquipe.setText("Victoire des rouges !");
            revealCard();
            showWinnerPopup("Rouge");
            if (jeu.getPartie().BlueWon() || jeu.getPartie().RedWon()) {
                if (stats != null) {
                    stats.setBlueTeamWon(jeu.getPartie().BlueWon());
                    jeu.getTimeTracker().stop();


                    if (jeu.getPartie().getTimer() != -1) {
                        stats.setTotalTime(jeu.getPartie().getTimer() - jeu.getPartie().getBlueTimeLeft() - jeu.getPartie().getRedTimeLeft());
                    }else {
                        stats.setTotalTime((int) jeu.getTimeTracker().getDuration().getSeconds());
                    }
                }
            }

        }

        // Marquer la carte comme révélée dans le modèle si nécessaire
        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
    }

    private void revealCard() {
        CarteBase[][] listCard = jeu.getPartie().getPlateau().getCards();
        for (CarteBase[] row : listCard) {
            for (CarteBase c : row) {
                Carte card = (Carte) c;
                AnchorPane carteVisu = findAnchorCard(card.getWord());
                if (carteVisu != null) {
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
        String audioFile = getClass().getResource("/soundtrack/Applaudissements.mp3").toExternalForm();

        Media media = new Media(audioFile);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        if (winningTeam.equals("Rouge")) {
            whoWon.setText("L'équipe rouge a gagné !");
            whoWon.setStyle("-fx-text-fill: #f70d1a;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("red_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-red");
        } else {
            whoWon.setText("L'équipe bleue a gagné !");
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
            controller.setJeu((jeu));
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
            controller.setJeu((jeu));
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
            imageview1.setVisible(true);  // Si visible, devient inv
            imageview2.setVisible(false);  // Si visible, devient inv
            panneau_changer.getStyleClass().clear(); // Supprimer toutes les classes existantes
            panneau_changer.getStyleClass().add("main_panneau");
            panneau_changer.getStyleClass().add("blue_main_panneau");
            panneau_changer2.getStyleClass().clear();
            panneau_changer2.getStyleClass().add("logo_panneau_bleu");
            panneau_changer2.getStyleClass().add("logo_panneau");
            labelEquipe.setText(GameConfig.BLUE_TURN_TEXT);
            labelEquipe.setText("C'est au tour des bleus");
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
            labelEquipe.setText("C'est au tour des rouges");
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
        stopTimer();
        if (jeu.getPartie().getwon() == 2) {
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
        this.updateLabel();


    }

    @FXML
    public void afficheQRcode() {
        try {
            // Génération du QR code de base
            BitMatrix qrcode = jeu.getPartie().getPlateau().getKey().to_qrcode();

            // Marges réduites
            int padding = 15;
            int width = qrcode.getWidth() + (2 * padding);
            int height = qrcode.getHeight() + (2 * padding);

            WritableImage finalImage = new WritableImage(width, height);
            PixelWriter pixelWriter = finalImage.getPixelWriter();

            // Couleurs simples mais contrastées
            Color bgColor = Color.WHITE;
            Color qrColor = Color.rgb(0, 0, 150);  // Bleu foncé pour un bon contraste

            // Remplissage du fond
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    pixelWriter.setColor(x, y, bgColor);
                }
            }

            // Dessin du QR code
            for (int x = 0; x < qrcode.getWidth(); x++) {
                for (int y = 0; y < qrcode.getHeight(); y++) {
                    if (qrcode.get(x, y)) {
                        pixelWriter.setColor(x + padding, y + padding, qrColor);
                    }
                }
            }

            // Application simple des effets
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.1);
            colorAdjust.setContrast(0.1);

            // Application de l'image et de l'effet
            qrCode.setImage(finalImage);
            qrCode.setEffect(colorAdjust);
            qrCode.setCache(true);
            qrCode.setCacheHint(CacheHint.QUALITY);

            // Animation simple
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), qrCode);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            qrCode.setVisible(true);
            lingualogo.setVisible(false);
            fadeIn.play();

        } catch (Exception e) {
            DataUtils.logException(e, "Erreur lors de la génération du QR code");
        }
        qrCode.setVisible(!jeu.getPartie().getPlateau().isqrcodeaffiche());
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
            }

            startTimer();
        }
    }
}
