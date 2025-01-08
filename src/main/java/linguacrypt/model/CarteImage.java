package linguacrypt.model;

public class CarteImage extends CarteBase {
    private final String url;


    public CarteImage(String url, int carteType) {
        super(carteType);
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getWord() {
        return "Attention la carte est une carte image, elle a un url de phot et non un mot";
    }

}
