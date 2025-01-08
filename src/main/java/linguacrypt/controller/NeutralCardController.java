package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class NeutralCardController {

    @FXML
    private Label labelMot;

    public void setMot(String mot) {
        labelMot.setText(mot);
    }

}