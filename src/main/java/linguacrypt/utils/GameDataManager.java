package linguacrypt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.config.GameConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameDataManager {
    private final UserConfig userConfig;
    private final Path appDir;

    public GameDataManager(String gameDataPath) throws IOException {
        InputStream fileStream = getClass().getClassLoader().getResourceAsStream(gameDataPath);

        DataUtils.assertNotNull(fileStream, "Le fichier " + gameDataPath + " n'existe pas.");

        String userHome = System.getProperty("user.home");
        appDir = Paths.get(userHome, GameConfig.APP_DIR);

        if (!appDir.toFile().exists()) {
            appDir.toFile().mkdir();
        }

        Path userConfigPath = appDir.resolve(GameConfig.USER_CONFIG_FILE);

        if (!userConfigPath.toFile().exists()) {
            Files.copy(fileStream, userConfigPath);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        userConfig = objectMapper.readValue(userConfigPath.toFile(), UserConfig.class);
    }

    public void saveUserConfig() {
        try {
            Path userConfigPath = appDir.resolve(GameConfig.USER_CONFIG_FILE);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(userConfigPath.toFile(), userConfig);
        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors de la sauvegarde de UserConfig.");
        }

    }

    public ArrayList<String> getWordsByTheme(String theme) {
        List<WordsCategory> categories = userConfig.getCategories();

        for (WordsCategory category : categories) {
            if (category.getName().equals(theme)) {
                return new ArrayList<>(category.getWords());
            }
        }

        return null;
    }

    public ArrayList<String> getWordsByThemes(ArrayList<String> themes) {
        ArrayList<String> words = new ArrayList<>();

        List<WordsCategory> categories = userConfig.getCategories();

        for (WordsCategory category : categories) {
            if (themes.contains(category.getName())) {
                words.addAll(category.getWords());
            }
        }

        return words;
    }

    public ArrayList<String> getAllThemes() {
        ArrayList<String> themes = new ArrayList<>();

        List<WordsCategory> categories = userConfig.getCategories();

        for (WordsCategory category : categories) {
            themes.add(category.getName());
        }

        return themes;
    }

    public boolean addCategory(String categoryName) {
        if (themeExists(categoryName)) return false;

        userConfig.getCategories().add(new WordsCategory(categoryName, new ArrayList<>()));

        return true;
    }

    public boolean themeExists(String category) {
        for (WordsCategory cat : userConfig.getCategories()) {
            if (cat.getName().equals(category)) {
                return true;
            }
        }
        return false;
    }

    public Object[] addWordToCategory(String category, String word) {
        WordsCategory previousCategory = getCategoryByWord(word);
        if (previousCategory != null) {
            return new Object[]{false, "Le mot " + word + " existe déjà dans le thème " + previousCategory.getName() + "."};
        }

        for (WordsCategory cat : userConfig.getCategories()) {
            if (cat.getName().equals(category)) {
                cat.getWords().add(word);
                return new Object[]{true, ""};
            }
        }

        return new Object[]{false, "La catégorie " + category + " n'existe pas."};
    }

    public WordsCategory getCategoryByWord(String word) {
        for (WordsCategory cat : userConfig.getCategories()) {
            if (cat.getWords().contains(word)) {
                return cat;
            }
        }

        return null;
    }

    public void removeWordFromCategory(String category, String word) {
        List<WordsCategory> categories = userConfig.getCategories();

        for (WordsCategory cat : categories) {
            if (cat.getName().equals(category)) {
                if (cat.getWords().contains(word)) {
                    cat.getWords().remove(word);

                    if (cat.getWords().isEmpty()) {
                        categories.remove(cat);
                    }

                    return;
                }
            }
        }
    }

    public void addImage(File image) {
        try {
            Files.copy(image.toPath(), appDir.resolve(image.getName()));
        } catch (IOException e) {
            DataUtils.logException(e, "Erreur lors de la copie de l'image.");
        }

        userConfig.getAddedImages().add(image.getName());
    }

    public void removeImage(String image) {
        if (Files.exists(appDir.resolve(image))) {
            try {
                Files.delete(appDir.resolve(image));
            } catch (IOException e) {
                DataUtils.logException(e, "Erreur lors de la suppression de l'image.");
            }

            userConfig.getAddedImages().remove(image);
        } else {
            userConfig.getUsedImages().remove(image);
        }
    }

    public ArrayList<String> getImages() {
        ArrayList<String> images = new ArrayList<>(userConfig.getUsedImages());

        for (String name: userConfig.getAddedImages()) {
            images.add(appDir.resolve(name).toString());
        }

        return images;
    }
}
