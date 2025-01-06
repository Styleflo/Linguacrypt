package linguacrypt.model;

public class Carte {
    private final String word;
    private final int type;  // 0: bleu ; 1: rouge ; 2: blanc ; 3: noir
    private boolean covered;

    public Carte(String word, int type) {
        this.word = word;
        this.type = type;
        covered = false;
    }

    public int getType() {
        return type;
    }

    public String getWord() {
        return word;
    }

    public void setCovered() {
        covered = true;
    }

    public boolean isCovered() {
        return covered;
    }
}
