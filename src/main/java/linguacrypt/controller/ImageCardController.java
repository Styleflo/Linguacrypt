package linguacrypt.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import linguacrypt.model.Jeu;
import linguacrypt.utils.CardType;

public class ImageCardController {
    private Jeu jeu;
    @FXML
    private ImageView cardImage;
    @FXML
    private ImageView coveredImage;
    @FXML
    private ImageView semicoveredImage;
    @FXML
    private ImageView coveredImagepetit;
    @FXML
    private ImageView semicoveredImagepetit;

    private String currentUrl;

    @FXML
    public void initialize() {

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void setMyImage(String url) {
        if (url != null && !url.isEmpty()) {
            try {
                Image image = new Image(url, false); // true pour le chargement en arrière-plan
                cardImage.setImage(image);
                cardImage.setPreserveRatio(true);

                currentUrl = url;
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }

    public void setRecouvert(CardType type, boolean recouvert) {
        if (jeu.getPartie().getPlateau().getKey().getHeight() < 7 && jeu.getPartie().getPlateau().getKey().getWidth() < 6) {

            if (coveredImage != null) {
                String coverImageUrl = switch (type) {
                    case RED -> "/assets/hippo.png";
                    case BLUE -> "/assets/florian.png";
                    case BLACK -> "/assets/lancelot.png";
                    case WHITE -> "/assets/moi.png";
                };

                try {
                    Image coverImage = new Image(getClass().getResourceAsStream(coverImageUrl));
                    coveredImage.setImage(coverImage);
                    coveredImage.setVisible(recouvert);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image de couverture: " + e.getMessage());
                }
            }
        }else{
            if (coveredImagepetit != null) {
                String coverImageUrl = switch (type) {
                    case RED -> "/assets/ely_petit.png";
                    case BLUE -> "/assets/mai_petit.png";
                    case BLACK -> "/assets/garance_petit.png";
                    case WHITE -> "/assets/gael_petit.png";
                };

                try {
                    Image coverImage = new Image(getClass().getResourceAsStream(coverImageUrl));
                    coveredImagepetit.setImage(coverImage);
                    coveredImagepetit.setVisible(recouvert);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image de couverture: " + e.getMessage());
                }
            }
        }

    }

    public void setsemiRecouvert(CardType type, boolean recouvert) {
        if (jeu.getPartie().getPlateau().getKey().getHeight() < 7 && jeu.getPartie().getPlateau().getKey().getWidth() < 6) {

            if (coveredImage != null) {
                String coverImageUrl = switch (type) {
                    case RED -> "/assets/hippo.png";
                    case BLUE -> "/assets/florian.png";
                    case BLACK -> "/assets/lancelot.png";
                    case WHITE -> "/assets/moi.png";
                };
                String semicoverImageUrl = switch (type) {
                    case RED -> "/assets/hippo_compresse.png";
                    case BLUE -> "/assets/florian_compresse.png";
                    case BLACK -> "/assets/lancelot_compresse.png";
                    case WHITE -> "/assets/moi_compresse.png";
                };

                try {
                    Image coverImage = new Image(getClass().getResourceAsStream(coverImageUrl));
                    Image semicoverImage = new Image(getClass().getResourceAsStream(semicoverImageUrl));
                    semicoveredImage.setImage(semicoverImage);
                    coveredImage.setImage(coverImage);
                    coveredImage.setVisible(!recouvert);
                    semicoveredImage.setVisible(recouvert);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image de couverture: " + e.getMessage());
                }
            }
        }else {
        if (coveredImagepetit != null) {
            String coverImageUrl = switch (type) {
                case RED -> "/assets/ely_petit.png";
                case BLUE -> "/assets/mai_petit.png";
                case BLACK -> "/assets/garance_petit.png";
                case WHITE -> "/assets/gael_petit.png";
            };
            String semicoverImageUrl = switch (type) {
                case RED -> "/assets/ely_petit_compresse.png";
                case BLUE -> "/assets/mai_petit_compresse.png";
                case BLACK -> "/assets/garance_petit_compresse.png";
                case WHITE -> "/assets/gael_petit_compresse.png";
            };


                try {
                    Image coverImage = new Image(getClass().getResourceAsStream(coverImageUrl));
                    Image semicoverImage = new Image(getClass().getResourceAsStream(semicoverImageUrl));
                    semicoveredImagepetit.setImage(semicoverImage);
                    coveredImagepetit.setImage(coverImage);
                    coveredImagepetit.setVisible(!recouvert);
                    semicoveredImagepetit.setVisible(recouvert);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image de couverture: " + e.getMessage());
                }
            }
        }
    }


    public String getImageUrl() {
        return currentUrl;
    }
}
