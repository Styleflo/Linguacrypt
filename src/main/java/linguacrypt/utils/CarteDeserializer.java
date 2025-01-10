package linguacrypt.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import linguacrypt.model.Carte;
import linguacrypt.model.CarteBase;
import linguacrypt.model.CarteImage;

import java.io.IOException;

public class CarteDeserializer extends JsonDeserializer<CarteBase> {

    @Override
    public CarteBase deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = p.getCodec().readTree(p);

        // Lire les propriétés communes
        CardType type = CardType.valueOf(node.get("type").asText());
        boolean covered = node.get("covered").asBoolean();

        // Vérifier si c'est une Carte ou une CarteImage en fonction des champs
        if (node.has("word")) {
            String word = node.get("word").asText();
            return new Carte(word, type, covered); // Carte a un champ "word"
        } else if (node.has("url")) {
            String url = node.get("url").asText();
            return new CarteImage(url, type, covered); // CarteImage a un champ "url"
        } else {
            throw new IOException("Invalid JSON: Missing 'word' or 'url' field");
        }
    }
}
