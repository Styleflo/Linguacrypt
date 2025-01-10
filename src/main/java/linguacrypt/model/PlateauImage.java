package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.utils.CardType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * herite de la classe PlateauBase
 * Contient les carteImages sous le type CarteBase
 */
public class PlateauImage extends PlateauBase implements Serializable {
    private final CarteBase[][] cardImages;

    public PlateauImage(int width, int height, ArrayList<String> words_list) {
        super(width, height);
        cardImages = new CarteImage[height][width];
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String word = words_list.get(index++);
                CardType type = key.getCardType(i, j);
                CarteImage card = new CarteImage(word, type);
                cardImages[i][j] = card;
            }
        }
        isBlueTurn = key.isBlueStarting();
    }

    @JsonCreator
    public PlateauImage(@JsonProperty("key") Clef key, @JsonProperty("coveredCardsCounts") int[] coveredCardsCounts,
                        @JsonProperty("blueTurn") boolean isBlueTurn, @JsonProperty("redTurn") boolean isRedTurn, @JsonProperty("pointBlue") int pointBlue,
                        @JsonProperty("pointRed") int pointRed, @JsonProperty("qrcodeaffiche") boolean qrcodeaffiche,
                        @JsonProperty("cardsImages") CarteBase[][] cardImages) {
        super(key, coveredCardsCounts, isBlueTurn, isRedTurn, pointBlue, pointRed, qrcodeaffiche);
        this.cardImages = cardImages;
    }

    public PlateauImage(ArrayList<String> words_list) {
        this(5, 5, words_list);
    }

    @Override
    public CarteBase getCard(int i, int j) {
        return this.cardImages[i][j];
    }

    public CardType coverCard(int i, int j) {
        // Coordonnées matrcielles
        CarteBase card = cardImages[i][j];
        card.setCovered();
        return card.getType();
    }

    public CarteBase[][] getCards() {
        return cardImages;
    }
}
