package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;

public class Plateau {
    private final Carte[][] cards;
    private boolean isBlueTurn;
    private final Clef key;
    private int[] coveredCardsCounts;

    public Plateau(int width, int height, ArrayList<String> word_list) {
        cards = new Carte[height][width];
        key = new Clef(width, height);
        Collections.shuffle(word_list);

        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String word = word_list.get(index++);
                int type = key.getCardType(i, j);
                Carte card = new Carte(word, type);
                cards[i][j] = card;
            }
        }

        isBlueTurn = key.isBlueStarting();
        coveredCardsCounts = new int[4];
    }

    public boolean isBlueTurn() { return isBlueTurn; }

    public boolean isRedTurn() { return !isBlueTurn; }

    public Carte getCard(int i, int j) { return cards[i][j]; }

    public int[] getCoveredCardsCounts() { return coveredCardsCounts; }
}
