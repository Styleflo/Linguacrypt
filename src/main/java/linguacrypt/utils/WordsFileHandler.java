package linguacrypt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WordsFileHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;
    private final WordsCategories wordsCategories;

    public WordsFileHandler(String filePath) throws IOException {
        URL resource = getClass().getClassLoader().getResource(filePath);

        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        file = new File(resource.getFile());

        wordsCategories = objectMapper.readValue(file, WordsCategories.class);
    }

    public void writeJsonFile() throws IOException {
        objectMapper.writeValue(file, wordsCategories);
    }

    public ArrayList<String> getWordsByTheme(String theme) {
        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            if (category.getName().equals(theme)) {
                return new ArrayList<>(category.getWords());
            }
        }

        return null;
    }

    public ArrayList<String> getWordsByThemes(ArrayList<String> themes) {
        ArrayList<String> words = new ArrayList<>();

        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            if (themes.contains(category.getName())) {
                words.addAll(category.getWords());
            }
        }

        return words;
    }

    public ArrayList<String> getAllThemes() {
        ArrayList<String> themes = new ArrayList<>();

        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            themes.add(category.getName());
        }

        return themes;
    }

    public void addCategory(String category) {
        category = category.trim().toLowerCase();
        wordsCategories.getCategories().add(new WordsCategory(category, new ArrayList<>()));
    }

    public void addWordToCategory(String category, String word) {
        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory cat : categories) {
            if (cat.getName().equals(category)) {
                cat.getWords().add(word);
                return;
            }
        }
    }
}
