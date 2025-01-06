package linguacrypt.model;

public class Carte {
    private String word;
    private int type;  // 0: bleu ; 1: rouge ; 2: blanc ; 3: noir

    public Carte(String word, int type) {
        this.word = word;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
