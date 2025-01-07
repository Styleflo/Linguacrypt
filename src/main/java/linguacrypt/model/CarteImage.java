package linguacrypt.model;

public class CarteImage {
    private final String url;
    private final int carteType;  // 0: bleu ; 1: rouge ; 2: noir ; 3: blanc
    private boolean covered;

    public CarteImage(String url, int carteType) {
        this.url = url;
        this.carteType = carteType;
        covered = false;
    }

    public int getType() {
        return carteType;
    }

    public String getWord() {
        return url;
    }

    public void setCovered() {
        covered = true;
    }

    public boolean isCovered() {
        return covered;
    }
}
