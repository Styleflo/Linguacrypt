package linguacrypt.model;

/**
 * Contient un Plateau de cartes de mots ou d'images.
 * Contient un boolean pour connaitre l'etat du jeu entre gagné ou en cours. Gagné étant true
 */
public class Partie {
    private Plateau plateau;
    private boolean win = false;


    public Partie(Plateau plateau) {
        this.plateau = plateau;
    }

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
    public boolean isWin() {
        return this.win;
    }

    /**
     * Permet de relancer une partie avec de nouvelles cartes.
     */
    public void newPartie() {
        this.plateau = new Plateau();
        this.win = false;
    }

    /**
     * Permet de changer l'etat du jeu en gagné
     */
    public void setWin() {
        this.win = true;
    }
}
