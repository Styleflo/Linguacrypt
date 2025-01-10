package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;

import java.io.IOException;

public class MenuInitialController implements Observer {
    private Jeu jeu;
    @FXML
    private ImageView filtre;

    @FXML
    private Pane statOverlay;

    @FXML
    private Label victoiresBleues;

    @FXML
    private Label victoiresRouges;

    @FXML
    private Label whoWon;
    @FXML
    private Button colorButton;
    @FXML
    private VBox borderWin;

    public MenuInitialController() {
    }

    public void setJeu(Jeu jeu) {
        filtre.setMouseTransparent(true);
        this.jeu = jeu;
        mettreAJourVictoires();
    }

    private void mettreAJourVictoires() {
        if (jeu != null) {
            victoiresBleues.setText("Victoires bleues : " + jeu.getVictoireBleue());
            victoiresRouges.setText("Victoires rouges : " + jeu.getVictoireRouge());
        }
    }

    @FXML
    private void handleStatButton() {
        statOverlay.setVisible(true);
        if (jeu.getVictoireBleue()<jeu.getVictoireRouge()) {
            whoWon.setText("L'équipe Rouge gagne !");
            whoWon.setStyle("-fx-text-fill: #f70d1a;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("red_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-red");
        }
        else if (jeu.getVictoireBleue()==jeu.getVictoireRouge()) {
            whoWon.setText("Personne ne gagne !");
            whoWon.setStyle("-fx-text-fill: white;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("white"));
            colorButton.getStyleClass().add("white_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-white");
        }
        else {
            whoWon.setText("L'équipe Bleue gagne !");
            whoWon.setStyle("-fx-text-fill: #3399FF;");
            colorButton.getStyleClass().removeIf(classe -> classe.startsWith("blue"));
            colorButton.getStyleClass().add("blue_button");
            borderWin.getStyleClass().removeIf(classe -> classe.startsWith("win-box"));
            borderWin.getStyleClass().add("win-box-blue");
        }
        jeu.notifyObservers();

    }

    @FXML
    private void okStat() {
        statOverlay.setVisible(false);
    }

    @FXML
    private void handleJouerButtonAction() throws IOException {
        jeu.setView("Parametres");
        jeu.notifyObservers();
    }

    @FXML
    private void handleCartesButtonAction() {
        jeu.setView("Cartes");
        jeu.notifyObservers();
    }

    @FXML
    private void handleQuitterButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void reagir() {
        mettreAJourVictoires();
    }
}