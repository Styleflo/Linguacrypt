package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import linguacrypt.utils.CardType;

public class ImageCardController {
    @FXML
    private ImageView cardImage;
    @FXML
    private ImageView coveredImage;

    private String currentUrl;

    @FXML
    public void initialize() {

    }

    public void setImage(String url) {
        if (url != null && !url.isEmpty()) {
            try {
                Image image = new Image(url, true); // true pour le chargement en arrière-plan
                cardImage.setImage(image);
                currentUrl = url;
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                setPlaceholderImage();
            }
        }
    }

    private void setPlaceholderImage() {
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/assets/placeholder.png"));
            cardImage.setImage(placeholder);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image par défaut: " + e.getMessage());
        }
    }

    public void setRecouvert(CardType type, boolean recouvert) {
        if (coveredImage != null) {
            String coverImageUrl = switch (type) {
                case RED -> "/assets/red_cover.png";
                case BLUE -> "/assets/blue_cover.png";
                case BLACK -> "/assets/black_cover.png";
                case WHITE -> "/assets/white_cover.png";
            };

            try {
                Image coverImage = new Image(getClass().getResourceAsStream(coverImageUrl));
                coveredImage.setImage(coverImage);
                coveredImage.setVisible(recouvert);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image de couverture: " + e.getMessage());
            }
        }
    }

    public String getImageUrl() {
        return currentUrl;
    }
}