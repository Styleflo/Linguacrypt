package linguacrypt.model;

import linguacrypt.config.GameConfig;
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
    private final WordsFileHandler wordsFileHandler;

    /**
     * Le constructeur à appeler pour creer une Partie et la build.
     * C'est un objet PartieBuilder, il faut donc finir par .getResult pour obtenir la partie construite
     */
    public PartieBuilder(Jeu jeu) {
        this.partie = new Partie();
        this.wordsFileHandler = jeu.getWordsFileHandler();
        WordsFileHandler wordsFileHandler = jeu.getWordsFileHandler();
        partie.setWords(this.wordsFileHandler.getWordsByThemes(wordsFileHandler.getAllThemes()));
        partie.setTimer(GameConfig.DEFAULT_TIMER);
        partie.setWidthParameter(GameConfig.DEFAULT_WIDTH);
        partie.setHeightParameter(GameConfig.DEFAULT_HEIGHT);
        partie.setTypePartie(TypePartie.WORDS);
    }

    /**
     * Permet de reset le temps de chaque tour.
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
     * Permet de reset le temps de chaque tour.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetTimer() {
        this.partie.setTimer(GameConfig.DEFAULT_TIMER);
        return this;
    }

    /**
     * Permet de reset la hauteur de la grille de jeu.
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
     * Permet de reset la hauteur de la grille de jeu.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetHeightParameter() {
        this.partie.setHeightParameter(GameConfig.DEFAULT_HEIGHT);
        return this;
    }

    /**
     * Permet de reset la largeur de la grille de jeu.
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
     * Permet de reset la largeur de la grille de jeu.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetWidthParameter() {
        this.partie.setWidthParameter(GameConfig.DEFAULT_WIDTH);
        return this;
    }

    /**
     * Permet de reset la largeur les themes du jeu
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @param themes
     * @return PartieBuilder
     */
    public PartieBuilder setUsedThemes(ArrayList<String> themes) {
        this.partie.setWords(wordsFileHandler.getWordsByThemes(themes));
        return this;
    }

    /**
     * Permet de reset la largeur les themes du jeu
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetUsedThemes() {
        this.partie.setWords(this.wordsFileHandler.getWordsByThemes(wordsFileHandler.getAllThemes()));
        return this;
    }

    /**
     * Permet de reset les mots utilisées sans la partie
     *
     * @param words
     * @return PartieBuilder
     */
    public PartieBuilder setWordsUsed(ArrayList<String> words) {
        this.partie.setWords(words);
        return this;
    }

    /**
     * Permet de reset les mots utilisées sans la partie
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetWordsUsed() {
        this.partie.setWords(this.wordsFileHandler.getWordsByThemes(wordsFileHandler.getAllThemes()));
        return this;
    }


    public PartieBuilder createPlateau() {
        this.partie.newPlateau();
        return this;
    }

    /**
     * Permet de reset le type de partie entre image ou mot.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @param typePartie
     * @return PartieBuilder
     */
    public PartieBuilder setTypePartie(TypePartie typePartie) {
        this.partie.setTypePartie(typePartie);
        return this;
    }

    /**
     * Permet de reset le type de partie entre image ou mot.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetTypePartie() {
        this.partie.setTypePartie(TypePartie.WORDS);
        return this;
    }

    /**
     * Permet de reset l'ensemble des parametres
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
     *
     * @return PartieBuilder
     */
    public PartieBuilder resetall() {
        PartieBuilder partieBuilder = this.resetTypePartie().
                resetWordsUsed().
                resetHeightParameter().
                resetWidthParameter().
                resetTimer();
        return this;
    }

    /**
     * Construit une instance de Partie.
     *
     * @return Partie
     */
    public Partie getResult() throws IOException {
        this.createPlateau();
        return this.partie;
    }
}
