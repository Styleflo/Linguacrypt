package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import linguacrypt.utils.CardType;

public class NeutralCardController {
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
    private ImageView coveredsemiBlue;

    @FXML
    private ImageView coveredsemiRed;

    @FXML
    private ImageView coveredsemiWhite;

    @FXML
    private ImageView coveredsemiBlack;

    public void setMot(String mot) {
        labelMot.setText(mot);
    }

    public Label getLabelMot() {
        return labelMot;
    }

    public void setRecouvert(CardType color, boolean recouvert) {
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
    }

    public void setSemiCovered(CardType color, boolean semicovered) {
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
    }

}
