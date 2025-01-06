package linguacrypt.model;

import java.util.ArrayList;

/**
 * Contient un Plateau de cartes de mots ou d'images.
 * Contient un boolean pour connaitre l'etat du jeu entre gagné ou en cours. Gagné étant true.
 * Contient un entier pour le temps de jeu de chaque équipe.
 * Contient une liste des themes selectionnés pour la partie
 * Contient la largeur du plateau
 * Contient la hauteur du plateau
 */
public class Partie {
    private Plateau plateau;
    private boolean won = false;
    private int timer;
    private ArrayList<String> thematicListsSelected;
    private int heightParameter;
    private int widthParameter;

    /**
     * Explicite.
     */
    public Plateau getPlateau() {
        return this.plateau;
    }

    /**
     * Permet de set un plateau specifique.
     * @param plateau
     */
    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    /**
     * Permet de savoir si une équipe a déjà gagné.
     * @return boolean
     */
    public boolean isWon() {
        return this.won;
    }

    /**
     * Permet de relancer une partie avec de nouvelles cartes sans changer les parametres.
     */
    public void newPlateau() {
        this.plateau = new Plateau(this.widthParameter, this.heightParameter, //fonction d'hippo à appliquer sur ArrayList<String> thematicListsSelected);
        this.won = false;
    }

    /**
     * Permet de changer l'etat du jeu en gagné
     */
    public void setWon() {
        this.won = true;
    }

    /**
     * Permet de recuperer les thematiques selectionnées
     * @return ArrayList<String>
     */
    public ArrayList<String> getThematicListsSelected() {
        return this.thematicListsSelected;
    }

    /**
     * Permet de set les thematiques selectionnées
     * @param thematicListsSelected
     */
    public void setThematicListsSelected(ArrayList<String> thematicListsSelected) {
        this.thematicListsSelected = thematicListsSelected;
    }

    /**
     * Explicite.
     */
    public int getTimer() {
        return timer;
    }

    /**
     * Explicite.
     */
    public void setTimer(int timer) {
        this.timer = timer;
    }

    /**
     * Explicite.
     */
    public int getHeightParameter() {
        return heightParameter;
    }

    /**
     * Explicite.
     */
    public void setHeightParameter(int heightParameter) {
        this.heightParameter = heightParameter;
    }

    /**
     * Explicite.
     */
    public int getWidthParameter() {
        return widthParameter;
    }

    /**
     * Explicite.
     */
    public void setWidthParameter(int widthParameter) {
        this.widthParameter = widthParameter;
    }
}
