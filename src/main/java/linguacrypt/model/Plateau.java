package linguacrypt.model;

import linguacrypt.config.GameConfig;
import linguacrypt.utils.CardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.max;

public class Plateau extends PlateauBase implements Serializable {
    private final CarteBase[][] cards;

    public Plateau(int width, int height, ArrayList<String> words_list) {
        super(width, height);
        cards = new Carte[height][width];
        Collections.shuffle(words_list);
        int index = 0;
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                String word = words_list.get(index++);
                CardType type = key.getCardType(i, j);
                Carte card = new Carte(word, type);
                cards[i][j] = card;
            }
        }
    }

    public Plateau(ArrayList<String> words_list) {
        this(GameConfig.DEFAULT_WIDTH, GameConfig.DEFAULT_HEIGHT, words_list);
    }

    @Override
    public CarteBase getCard(int i, int j) {
        return this.cards[i][j];
    }

    public CardType coverCard(int i, int j) {
        // Coordonnées matrcielles
        CarteBase card = cards[i][j];
        card.setCovered();
        return card.getType();
    }

    public void prettyPrint() {
        for (CarteBase[] card : cards) {
            for (CarteBase carte : card) {
                System.out.print(carte.getWord());
                System.out.print(" ".repeat(max(15 - carte.getWord().length(), 0)));
            }

            System.out.println();
        }
    }

    public CarteBase[][] getCards() {
        return cards;
    }
}
