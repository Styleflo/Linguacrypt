package linguacrypt.model;

import linguacrypt.utils.CardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.max;

public class Plateau extends PlateauBase implements Serializable {
    private final CarteBase[][] cards;

    public Plateau(int width, int height, ArrayList<String> words_list, TypePartie typePartie) {
        super(width, height);

        if (typePartie == TypePartie.WORDS) {
            cards = new Carte[height][width];
        } else {
            cards = new CarteImage[height][width];
        }

        Collections.shuffle(words_list);
        int index = 0;
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                String word = words_list.get(index++);
                CardType type = key.getCardType(i, j);
                if (typePartie == TypePartie.WORDS) {
                    Carte card = new Carte(word, type);
                    cards[i][j] = card;
                } else {
                    CarteImage cardImage = new CarteImage(word, type);
                    cards[i][j] = cardImage;
                }
            }
        }
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
            for (CarteBase c : card) {
                Carte carte = (Carte) c;
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
