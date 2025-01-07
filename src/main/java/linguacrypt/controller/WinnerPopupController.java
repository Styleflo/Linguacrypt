package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class WinnerPopupController {

    @FXML
    private Label winnerLabel; // Assure-toi que ce Label est bien défini dans ton fichier FXML.

    @FXML
    private Pane popupBackground; // Une zone de fond pour styliser le pop-up, si nécessaire.

    public void show(String winningTeam) {
        // Définir le texte du label avec le nom de l'équipe gagnante
        winnerLabel.setText("Félicitations ! L'équipe " + winningTeam + " a gagné !");

        // Appliquer un style ou une couleur spécifique selon l'équipe gagnante
        if ("Blue".equalsIgnoreCase(winningTeam)) {
            popupBackground.setStyle("-fx-background-color: #4dabf7;"); // Bleu
        } else if ("Red".equalsIgnoreCase(winningTeam)) {
            popupBackground.setStyle("-fx-background-color: #ff6b6b;"); // Rouge
        } else {
            popupBackground.setStyle("-fx-background-color: #f8f9fa;"); // Neutre ou par défaut
        }
    }
}

