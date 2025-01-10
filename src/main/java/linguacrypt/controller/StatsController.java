package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import linguacrypt.model.Jeu;
import linguacrypt.model.GameStatistics;

public class StatsController implements Observer {
    private Jeu jeu;

    @FXML
    private Label blueWinsLabel;

    @FXML
    private Label redWinsLabel;

    @FXML
    private VBox gamesContainer;

    @FXML
    private AnchorPane detailsPopup;

    @FXML
    private Label popupTitle;

    @FXML
    private VBox statsDetails;

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        updateStats();
    }

    private void updateStats() {
        // Mise à jour des compteurs de victoires
        blueWinsLabel.setText(String.valueOf(jeu.getVictoireBleue()));
        redWinsLabel.setText(String.valueOf(jeu.getVictoireRouge()));

        // Nettoyage du container
        gamesContainer.getChildren().clear();

        // Ajout de chaque partie
        for (GameStatistics stats : jeu.getGameStatistics()) {
            HBox gameRow = createGameRow(stats);
            gamesContainer.getChildren().add(gameRow);
        }
    }

    private HBox createGameRow(GameStatistics stats) {
        HBox row = new HBox(20);
        row.getStyleClass().add("game-row");

        // ID de la partie
        Label idLabel = new Label("Partie " + stats.getGameId());

        // Temps total restant
        String timeStr = formatTime(stats.getTotalTime());
        Label timeLabel = new Label("Temps restant: " + timeStr);

        // Vainqueur
        String winner = stats.isBlueTeamWon() ? "Équipe Bleue" : "Équipe Rouge";
        Label winnerLabel = new Label("Vainqueur: " + winner);
        winnerLabel.getStyleClass().add(stats.isBlueTeamWon() ? "blue-text" : "red-text");

        // Bouton Détails
        Button detailsButton = new Button("Détails");
        detailsButton.setOnAction(e -> showGameDetails(stats));

        row.getChildren().addAll(idLabel, timeLabel, winnerLabel, detailsButton);
        return row;
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void showGameDetails(GameStatistics stats) {
        popupTitle.setText("Détails de la partie " + stats.getGameId());

        statsDetails.getChildren().clear();

        // Ajout des statistiques détaillées
        addStatDetail("Précision équipe bleue", String.format("%.1f%%", stats.getBlueGuessAccuracy()));
        addStatDetail("Précision équipe rouge", String.format("%.1f%%", stats.getRedGuessAccuracy()));
        addStatDetail("Cartes Rouges données par équipe bleue", String.valueOf(stats.getRedCardsGivenToRed()));
        addStatDetail("Cartes Bleues données par équipe rouge", String.valueOf(stats.getBlueCardsGivenToBlue()));

        detailsPopup.setVisible(true);
    }

    private void addStatDetail(String label, String value) {
        HBox row = new HBox(20);
        row.getChildren().addAll(
                new Label(label + ":"),
                new Label(value)
        );
        statsDetails.getChildren().add(row);
    }

    @FXML
    private void closePopup() {
        detailsPopup.setVisible(false);
    }

    @FXML
    private void handleMenuInitial() {
        closePopup();
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("Stats")) {
            updateStats();
        }
    }
}