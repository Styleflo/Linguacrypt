package linguacrypt.model;

import linguacrypt.config.GameConfig;

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
    private PlateauBase plateau;
    private int won; // 0= bleu a gagné; 1=rouge a gagné; 2 = personne a gagné
    private int timer;
    private ArrayList<String> words;
    private int heightParameter;
    private int widthParameter;
    private TypePartie typePartie;

    /**
     * Explicite.
     */
    public PlateauBase getPlateau() {
        return this.plateau;
    }

    /**
     * Permet de set un plateau specifique.
     *
     * @param plateau
     */
    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    /**
     * @return TypeJeu
     */
    public TypePartie getTypePartie() {
        return this.typePartie;
    }

    /**
     * Permet de set le type de jeu.
     * Attention il faut passer un TypeJeu.
     * Soit TypeJeu.WORDS ou soit TypeJeu.IMAGES
     *
     * @param typeJeu
     */
    public void setTypePartie(TypePartie typeJeu) {
        this.typePartie = typeJeu;
    }

    /**
     * Permet de relancer une partie avec de nouvelles cartes sans changer les parametres.
     */
    public void newPlateau() {
        this.plateau = new Plateau(this.widthParameter, this.heightParameter, words);
        this.won = 2;
    }

    /**
     * Permet de changer l'etat du jeu en gagné pour bleu
     *
     * @return TypeJeu
     */
    public void setBlueWon() {
        this.won = 0;
    }

    /**
     * Permet de relancer une partie avec de nouvelles cartes sans changer les parametres.
     */
    public void setRedWon() {
        this.won = 1;
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

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public boolean RedWon() {
        return (this.won == 1);
    }

    public boolean BlueWon() {
        return (this.won == 0);
    }

    public void updateWin() {
        int nbpoint = (int) (this.getPlateau().getKey().getWidth() * this.getPlateau().getKey().getHeight() * GameConfig.BLUE_AND_RED_CARDS_PROPORTION);

        if (this.getPlateau().getKey().isBlueStarting()) {
            if (this.getPlateau().getPointBlue() == nbpoint + 1) {
                setBlueWon();
            } else if (this.getPlateau().getPointRed() == nbpoint) {
                setRedWon();
            }
        } else {
            if (this.getPlateau().getPointRed() == nbpoint + 1) {
                setRedWon();
            } else if (this.getPlateau().getPointBlue() == nbpoint) {
                setBlueWon();
            }
        }
    }

    public void updateWin(int color) {
        if (color == 2) {
            if (this.getPlateau().isBlueTurn()) {
                setRedWon();
            } else {
                setBlueWon();
            }
        }
    }

}
