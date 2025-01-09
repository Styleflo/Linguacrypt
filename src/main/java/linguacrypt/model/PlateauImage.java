package linguacrypt.model;

import linguacrypt.utils.CardType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * herite de la classe PlateauBase
 * Contient les carteImages sous le type CarteBase
 */
public class PlateauImage extends PlateauBase implements Serializable {
    private final CarteBase[][] cardsImages;

    public PlateauImage(int width, int height, ArrayList<String> words_list) {
        super(width, height);
        cardsImages = new CarteImage[height][width];
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String word = words_list.get(index++);
                CardType type = key.getCardType(i, j);
                CarteImage card = new CarteImage(word, type);
                cardsImages[i][j] = card;
            }
        }
        isBlueTurn = key.isBlueStarting();
    }

    public PlateauImage(ArrayList<String> words_list) {
        this(5, 5, words_list);
    }

    @Override
    public CarteBase getCard(int i, int j) {
        return this.cardsImages[i][j];
    }

    public CardType coverCard(int i, int j) {
        // Coordonnées matrcielles
        CarteBase card = cardsImages[i][j];
        card.setCovered();
        return card.getType();
    }

    public CarteBase[][] getCards() {
        return cardsImages;
    }
}
