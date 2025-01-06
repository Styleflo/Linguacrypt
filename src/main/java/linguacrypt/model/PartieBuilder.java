package linguacrypt.model;
import linguacrypt.utils.WordsFileHandler;

import java.util.ArrayList;

/**
 * Contient un objet Partie qui est initialisé par le constructueur
 * Contient la valeur du temps par Default
 * Contient la taille du plateau par default
 * Contient l'ensemble des themes par default
 */
public class PartieBuilder {
    private Partie partie;
    private final int TIMER = -1;
    private final int heightParameter = 5;
    private final int widthParameter = 5;
    private ArrayList<String> thematicListsSelected;

    /**
     * Le constructeur à appeler pour creer une Partie et la build.
     * C'est un objet PartieBuilder, il faut donc finir par .build pour construire la Partie
     */
    public PartieBuilder() {
        WordsFileHandler fileHandler = new WordsFileHandler("./cards.json");
        this.thematicListsSelected = fileHandler.getAllThemes();

        this.partie = new Partie();
        partie.setHeightParameter(heightParameter);
        partie.setWidthParameter(widthParameter);
        partie.setTimer(TIMER);
        partie.setThematicListsSelected(thematicListsSelected);
    }

    /**
     * Permet de choisir le temps de chaque tour.
     * Attention Il faut donc finir par .build à la fin de tout les parametres pour construire la Partie.
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
     * @param widthParameter
     * @return PartieBuilder
     */
    public PartieBuilder setWidthParameter(int widthParameter) {
        this.partie.setWidthParameter(widthParameter);
        return this;
    }

    /**
     * Permet de choisir les themes des cartes pour la partie
     * Attention Il faut finir par .build à la fin de tout les parametres pour construire la Partie.
     * @param thematicListsSelected
     * @return PartieBuilder
     */
    public PartieBuilder setThematicListsSelected(ArrayList<String> thematicListsSelected) {
        this.partie.setThematicListsSelected(thematicListsSelected);
        return this;
    }

    /**
     * Construit une instance de `Partie`.
     * @return Partie
     */
    public Partie build() {
        this.partie.newPlateau();
        return this.partie;
    }
}