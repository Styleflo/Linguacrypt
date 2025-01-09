package linguacrypt.model;

import linguacrypt.utils.CardType;

public class Carte extends CarteBase {
    private final String word;

    public Carte(String word, CardType type) {
        super(type);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
