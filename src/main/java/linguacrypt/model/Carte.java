package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.utils.CardType;

public class Carte extends CarteBase {
    private final String word;

    public Carte(String word, CardType type) {
        super(type);
        this.word = word;
    }

    public Carte(String word, CardType type, boolean covered) {
        super(type, covered);
        this.word = word;
    }

    @JsonCreator
    public Carte(@JsonProperty("type") CardType type, @JsonProperty ("covered") boolean covered,
                 @JsonProperty ("word") String word) {
        super(type, covered);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
