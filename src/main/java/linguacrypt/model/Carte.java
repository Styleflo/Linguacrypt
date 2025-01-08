package linguacrypt.model;

public class Carte extends CarteBase {
    private final String word;

    public Carte(String word, int type) {
        super(type);
        this.word = word;

    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public String getUrl() {
        return "Attention la carte est une carte image, elle a un url de phot et non un mot";
    }

}
