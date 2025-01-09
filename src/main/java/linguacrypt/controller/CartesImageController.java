package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import linguacrypt.config.GameConfig;
import linguacrypt.model.Jeu;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.ImagesFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class CartesImageController implements Observer {
    private Jeu jeu;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label themeLabel;

    @FXML
    private ImageView filtre;

    @FXML
    private int currentpage;


    private List<String> currentImages;

    public CartesImageController() {
        currentImages = new ArrayList<>();
        currentpage = 1;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;

        ImagesFileHandler imagesFileHandler = jeu.getImagesFileHandler();

        currentImages = imagesFileHandler.getImagesByThemes(imagesFileHandler.getAllThemes());
        //System.out.println(currentImages);

    }

    private void updateCurrentThemeLabel() {

    }


    private void afficherCartesImages() {
        DataUtils.assertNotNull(jeu, "Jeu non initialisé dans CartesController.afficherCartes()");
        filtre.setMouseTransparent(true);
        gridPane.getChildren().clear();
        gridPane.setHgap(GameConfig.CARTES_THEMES_HGAP);
        gridPane.setVgap(GameConfig.CARTES_THEMES_VGAP);
        gridPane.setPadding(new Insets(GameConfig.CARTES_THEMES_PADDING));

        int row = 0;
        int col = 0;
        int maxCols = 5;

        int ind_fin = min(50 * (currentpage), currentImages.size());

        for (int i = 50 * (currentpage - 1); i < ind_fin; i++) {
            AnchorPane carte = creerCarteImage(currentImages.get(i));

            assert carte != null;
            //create_transition(carte);
            int finalI = i;
            carte.setOnMouseClicked(event -> handleCardClick(currentImages.get(finalI), event.getScreenX(), event.getScreenY()));

            gridPane.add(carte, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }


    private Alert showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
    }

    private void handleCardClick(String word, double x, double y) {

        ContextMenu contextMenu = new ContextMenu();
        String deleteButtonLabel, alertTitle, alertContent;


        deleteButtonLabel = "Supprimer cette image ?";
        alertTitle = "Suppression de l'image";
        alertContent = "Voulez vous supprimer cette image ?";


        MenuItem deleteButton = new MenuItem(deleteButtonLabel);

        deleteButton.setOnAction(event -> {
            Alert alert = showAlert(AlertType.WARNING, alertTitle, null, alertContent);

            if (alert.showAndWait().isPresent()) {
                currentImages.remove(word);
                reagir();
            }
        });

        contextMenu.getItems().add(deleteButton);
        contextMenu.getItems().add(new MenuItem("Annuler"));
        contextMenu.show(gridPane.getScene().getWindow(), x, y);
    }

    /*private AnchorPane creerCarte(String mot) {

    }
    */


    private AnchorPane creerCarteImage(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Image_card.fxml"));
            AnchorPane card = loader.load();
            ImageCardController controller = loader.getController();
            card.setUserData(controller);
            controller.setMyImage(url);
            return card;
        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors de la création d'une carte image");
            return null;
        }
    }


    @FXML
    private void handleRevenirMenuAction() {
        jeu.setView("MenuInitial");
        jeu.notifyObservers();
    }

    private void setCurrentPage() {
        themeLabel.setText("Page " + currentpage);
    }

    @FXML
    public void nextCategory() {
        if (currentpage < 6) {
            currentpage++;
            setCurrentPage();
            afficherCartesImages();
        }
    }

    @FXML
    public void previousCategory() {
        if (currentpage > 1) {
            currentpage--;
            setCurrentPage();
            afficherCartesImages();
        }
    }

    @FXML
    private void addNewImage() {

    }

    @FXML
    private void CategoryImage() {
        jeu.setView("Cartes");
        jeu.notifyObservers();
    }

    @Override
    public void reagir() {
        if (jeu.getView().equals("CartesImages")) {
            afficherCartesImages();
        }
    }
}
