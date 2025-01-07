package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import linguacrypt.model.Jeu;

import java.io.IOException;

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

        int row = jeu.getPartie().getHeightParameter();
        int col = jeu.getPartie().getWidthParameter();

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                final int currentI = i;
                final int currentJ = j;
                AnchorPane carte = creerCarte(jeu.getPartie().getPlateau().getCard(i, j).getWord());

                carte.setOnMouseClicked(event -> {
                    handleCardClick(currentI, currentJ, carte);
                });

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
        if(jeu.getPartie().BlueWon()){
            System.out.println("Blue Won");
            showWinnerPopup("blue");
            //setWinnerPopup("Blue");
        }
        if(jeu.getPartie().RedWon()){
            System.out.println("Red Won");
            showWinnerPopup("red");
            //setWinnerPopup("Red");
        }
        // Marquer la carte comme révélée dans le modèle si nécessaire
        jeu.getPartie().getPlateau().getCard(x, y).setCovered();
        this.updateLabel();
    }

    private void showWinnerPopup(String winningTeam) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Partie terminée");
        alert.setHeaderText("Victoire !");
        alert.setContentText("L'équipe " + winningTeam + " a gagné !");
        alert.showAndWait();
    }

    public void setWinnerPopup(String winningTeam) {
        try {
            // Charger le fichier FXML du WinnerPopup
            FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("/view/WinnerPopup.fxml"));
            popupRoot = popupLoader.load();

            // Obtenir le contrôleur du pop-up
            WinnerPopupController winnerPopupController = popupLoader.getController();
            winnerPopupController.show(winningTeam);

            // Afficher le pop-up (tu peux remplacer AnchorPane si nécessaire)
            AnchorPane.setTopAnchor(popupRoot, 0.0);
            AnchorPane.setBottomAnchor(popupRoot, 0.0);
            AnchorPane.setLeftAnchor(popupRoot, 0.0);
            AnchorPane.setRightAnchor(popupRoot, 0.0);

            //content.getChildren().add(popupRoot);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void updateLabel() {
        if (this.jeu.getPartie().getPlateau().isBlueTurn()) {
            labelEquipe.setText("C'est le tour de Bleu");
        } else {
            labelEquipe.setText("C'est le tour de Rouge");
        }
        int nbpoint = jeu.getPartie().getPlateau().getKey().getWidth() * jeu.getPartie().getPlateau().getKey().getHeight() / 3;
        if (this.jeu.getPartie().getPlateau().getKey().isBlueStarting()) {
            lbbleu.setText(jeu.getPartie().getPlateau().getPointBlue() + "/" + (nbpoint + 1)  );
            lbred.setText(jeu.getPartie().getPlateau().getPointRed() + "/" + nbpoint);
        }else{
            lbbleu.setText(jeu.getPartie().getPlateau().getPointBlue() + "/" + nbpoint );
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
    private void handleTourSuivant(){
        jeu.getPartie().getPlateau().changeTurn();
        updateLabel();
    }

    @Override
    public void reagir() {
        if (jeu.getView() == "Plateau") {
            afficherCartes();
        }
    }
}
