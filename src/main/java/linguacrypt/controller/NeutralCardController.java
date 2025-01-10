package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import linguacrypt.model.Jeu;
import linguacrypt.utils.CardType;

public class NeutralCardController {
    private Jeu jeu;
    @FXML
    private Label labelMot;

    @FXML
    private ImageView coveredBlue;

    @FXML
    private ImageView coveredRed;

    @FXML
    private ImageView coveredWhite;

    @FXML
    private ImageView coveredBlack;

    @FXML
    private ImageView coveredpetitBlue;

    @FXML
    private ImageView coveredpetitRed;

    @FXML
    private ImageView coveredpetitWhite;

    @FXML
    private ImageView coveredpetitBlack;

    @FXML
    private ImageView coveredsemiBlue;

    @FXML
    private ImageView coveredsemiRed;

    @FXML
    private ImageView coveredsemiWhite;

    @FXML
    private ImageView coveredsemiBlack;

    @FXML
    private ImageView coveredsemipetitBlue;

    @FXML
    private ImageView coveredsemipetitRed;

    @FXML
    private ImageView coveredsemipetitWhite;

    @FXML
    private ImageView coveredsemipetitBlack;


    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }
    public void setMot(String mot) {
        labelMot.setText(mot);
    }

    public Label getLabelMot() {
        return labelMot;
    }

    public void setRecouvert(CardType color, boolean recouvert) {
        if (jeu.getPartie().getPlateau().getKey().getHeight() < 7 && jeu.getPartie().getPlateau().getKey().getWidth() < 6) {
            switch (color) {
                case CardType.BLUE:
                    coveredBlue.setVisible(recouvert);
                    break;
                case CardType.RED:
                    coveredRed.setVisible(recouvert);
                    break;
                case CardType.BLACK:
                    coveredBlack.setVisible(recouvert);
                    break;
                case CardType.WHITE:
                    coveredWhite.setVisible(recouvert);
                    break;
            }
        }else{
            switch (color) {
                case CardType.BLUE:
                    coveredpetitBlue.setVisible(recouvert);
                    break;
                case CardType.RED:
                    coveredpetitRed.setVisible(recouvert);
                    break;
                case CardType.BLACK:
                    coveredpetitBlack.setVisible(recouvert);
                    break;
                case CardType.WHITE:
                    coveredpetitWhite.setVisible(recouvert);
                    break;
            }
        }
    }

    public void setSemiCovered(CardType color, boolean semicovered) {
        if (jeu.getPartie().getPlateau().getKey().getHeight() < 7 && jeu.getPartie().getPlateau().getKey().getWidth() < 6) {

            switch (color) {
                case CardType.BLUE:
                    coveredBlue.setVisible(!semicovered);
                    coveredsemiBlue.setVisible(semicovered);
                    break;
                case CardType.RED:
                    coveredRed.setVisible(!semicovered);
                    coveredsemiRed.setVisible(semicovered);
                    break;
                case CardType.WHITE:
                    coveredWhite.setVisible(!semicovered);
                    coveredsemiWhite.setVisible(semicovered);
                    break;
                case CardType.BLACK:
                    coveredBlack.setVisible(!semicovered);
                    coveredsemiBlack.setVisible(semicovered);
                    break;
            }
        }else {
            switch (color) {
                case CardType.BLUE:
                    coveredpetitBlue.setVisible(!semicovered);
                    coveredsemipetitBlue.setVisible(semicovered);
                    break;
                case CardType.RED:
                    coveredpetitRed.setVisible(!semicovered);
                    coveredsemipetitRed.setVisible(semicovered);
                    break;
                case CardType.WHITE:
                    coveredpetitWhite.setVisible(!semicovered);
                    coveredsemipetitWhite.setVisible(semicovered);
                    break;
                case CardType.BLACK:
                    coveredpetitBlack.setVisible(!semicovered);
                    coveredsemipetitBlack.setVisible(semicovered);
                    break;
            }
        }
    }

}
