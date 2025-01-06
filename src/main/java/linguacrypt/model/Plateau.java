package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;

public class Plateau {
    private final Carte[][] cards;
    private final Clef key;
    private final boolean isBlueTurn;
    private final int[] coveredCardsCounts;

    public Plateau(int width, int height, ArrayList<String> words_list) {
        cards = new Carte[height][width];
        key = new Clef(width, height);
        Collections.shuffle(words_list);

        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String word = words_list.get(index++);
                int type = key.getCardType(i, j);
                Carte card = new Carte(word, type);
                cards[i][j] = card;
            }
        }

        isBlueTurn = key.isBlueStarting();
        coveredCardsCounts = new int[4];
    }

    public Plateau(ArrayList<String> words_list) {
        this(5, 5, words_list);
    }

    public boolean isBlueTurn() {
        return isBlueTurn;
    }

    public boolean isRedTurn() {
        return !isBlueTurn;
    }

    public Carte getCard(int i, int j) {
        return cards[i][j];
    }

    public int[] getCoveredCardsCounts() {
        return coveredCardsCounts;
    }

    public int coverCard(int i, int j) {
        Carte card = cards[i][j];
        card.setCovered();
        return card.getType();
    }

    public Clef getKey() {
        return key;
    }

    public void prettyPrint() {
        for (Carte[] card : cards) {
            for (Carte carte : card) {
                System.out.print(carte.getWord() + "\t");
            }
            System.out.println();
        }
    }
}
