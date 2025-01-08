package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import linguacrypt.config.GameConfig;

public class WinnerPopupController implements Observer {
    @FXML
    private Label winnerLabel;

    @FXML
    private StackPane root;

    @FXML
    private Button okButton;

    public void show(String winningTeam) {
        winnerLabel.setText("L'équipe " + winningTeam + " a gagné !");

        // Définir les couleurs en fonction de l'équipe gagnante
        if (winningTeam.equalsIgnoreCase("Bleue")) {
            winnerLabel.setStyle("-fx-text-fill: " + GameConfig.BLUE_CARD_COLOR + ";");
            okButton.getStyleClass().remove("red_button");
            okButton.getStyleClass().add("blue_button");
        } else {
            winnerLabel.setStyle("-fx-text-fill: " + GameConfig.RED_CARD_COLOR + ";");
            okButton.getStyleClass().remove("blue_button");
            okButton.getStyleClass().add("red_button");
        }

        root.setStyle("-fx-background-color: " + GameConfig.WINNER_POPUP_BG_COLOR + ";"); // Gris semi-transparent
        root.setVisible(true);
    }

    @FXML
    private void handleOkButton() {
        root.setStyle("-fx-background-color: transparent;");
        root.setVisible(false);
    }

    public StackPane getRoot() {
        return root;
    }

    public void reagir () {}
}
