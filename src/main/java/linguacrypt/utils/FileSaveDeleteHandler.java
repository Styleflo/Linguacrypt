package linguacrypt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.Partie;

import java.io.File;
import java.io.IOException;

public class FileSaveDeleteHandler {
    ObjectMapper objectMapper;

    public FileSaveDeleteHandler() throws IOException {
        this.objectMapper = new ObjectMapper();
    }

    public void savePartie(Partie partie, String filePath) throws IOException {
        // Créez un fichier à partir du chemin spécifié
        File fichier = new File(filePath);
        // Si le fichier n'existe pas, il sera créé
        if (!fichier.exists()) {
            fichier.createNewFile();
        }
        // Sérialiser l'objet Partie en JSON et l'écrire dans le fichier
        this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(fichier, partie);
    }

    public Partie loadPartie(String filePath) throws IOException {
        Partie partie;
        partie = objectMapper.readValue(new File(filePath), Partie.class);
        return partie;
    }

}
