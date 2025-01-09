package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
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
                coveredWhite.setVisible(recouvert);
                break;
            case CardType.WHITE:
                coveredBlack.setVisible(recouvert);
                break;
        }
    }

    private void adjustFontSize(String mot) {
        int length = mot.length();
        double fontSize;

        if (length <= 5) {
            fontSize = 20.0;
        } else if (length <= 10) {
            fontSize = 15.0;
        } else {
            fontSize = 10.0;
        }

        labelMot.setFont(new Font(fontSize));
    }
}
