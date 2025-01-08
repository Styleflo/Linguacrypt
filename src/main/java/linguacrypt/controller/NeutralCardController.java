package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class NeutralCardController {

    @FXML
    private Label labelMot;


    public void setMot(String mot) {
        labelMot.setText(mot);
        labelMot.setFont(new Font(5));
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