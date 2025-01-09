package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.utils.CardType;

public class CarteImage extends CarteBase {
    private final String url;


    public CarteImage(String url, CardType carteType) {
        super(carteType);
        this.url = url;
    }

    @JsonCreator
    public CarteImage(@JsonProperty("type") CardType type, @JsonProperty ("covered") boolean covered,
                      @JsonProperty ("url") String url) {
        super(type, covered);
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
