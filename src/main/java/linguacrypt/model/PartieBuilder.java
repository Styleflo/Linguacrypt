package linguacrypt.model;

import linguacrypt.utils.WordsFileHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contient un objet Partie qui est initialisé par le constructueur
 * Contient la valeur du temps par Default
 * Contient la taille du plateau par default
 * Contient l'ensemble des themes par default
 */
public class PartieBuilder {
    private final Partie partie;

    /**
     * Le constructeur à appeler pour creer une Partie et la build.
     * C'est un objet PartieBuilder, il faut donc finir par .getResult pour obtenir la partie construite
     */
    public PartieBuilder(Jeu jeu) throws IOException {
        this.partie = new Partie();
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();
        partie.setWords(wordsFileHandler.getWordsByThemes(wordsFileHandler.getAllThemes()));
        partie.setTimer(-1);
        partie.setWidthParameter(5);
        partie.setHeightParameter(5);
    }

    /**
     * Permet de choisir le temps de chaque tour.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @param timer
     * @return PartieBuilder
     */
    public PartieBuilder setTimer(int timer) {
        this.partie.setTimer(timer);
        return this;
    }

    /**
     * Permet de choisir la hauteur de la grille de jeu.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @param heightParameter
     * @return PartieBuilder
     */
    public PartieBuilder setHeightParameter(int heightParameter) {
        this.partie.setHeightParameter(heightParameter);
        return this;
    }

    /**
     * Permet de choisir la largeur de la grille de jeu.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @param widthParameter
     * @return PartieBuilder
     */
    public PartieBuilder setWidthParameter(int widthParameter) {
        this.partie.setWidthParameter(widthParameter);
        return this;
    }

    /**
     * Permet de choisir les mots utilisées sans la partie
     *
     * @param words
     * @return PartieBuilder
     */
    public PartieBuilder setWordsUsed(ArrayList<String> words) {
        this.partie.setWords(words);
        return this;
    }

    public PartieBuilder createPlateau() throws IOException {
        this.partie.newPlateau();
        return this;
    }

    /**
     * Construit une instance de `Partie`.
     *
     * @return Partie
     */
    public Partie getResult() throws IOException {
        return this.partie;
    }
}
