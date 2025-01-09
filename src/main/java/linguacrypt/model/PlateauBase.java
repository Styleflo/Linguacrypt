package linguacrypt.model;

import linguacrypt.utils.CardType;

import java.io.Serializable;

/**
 * Contient une Clef (la feuille qui dit les mots à faire deviner
 * Contient un boolean si c'est le tour des bleu
 * Contient une liste des cartes couvertes
 * Contient le nombre de poitn bleu
 * Contient le nombre de poitn bleu
 */
public abstract class PlateauBase implements Serializable {
    protected final Clef key;
    protected final int[] coveredCardsCounts;
    protected boolean isBlueTurn;
    protected int pointBlue;
    protected int pointRed;
    protected boolean qrcodeaffiche;
    /**
     * Constructeur
     *
     * @param width
     * @param height
     */
    public PlateauBase(int width, int height) {
        key = new Clef(width, height);
        pointBlue = 0;
        pointRed = 0;
        isBlueTurn = key.isBlueStarting();
        coveredCardsCounts = new int[4];
    }

    /**
     * donne une carte
     *
     * @param i
     * @param j
     * @return CarteBase
     */
    public abstract CarteBase getCard(int i, int j);

    public boolean isBlueTurn() {
        return isBlueTurn;
    }

    /**
     * Explicite
     *
     * @return boolean
     */
    public boolean isRedTurn() {
        return !isBlueTurn;
    }

    /**
     * donne les cartes couvertes
     *
     * @return int[]
     */
    public int[] getCoveredCardsCounts() {
        return coveredCardsCounts;
    }

    /**
     * Explicite
     *
     * @return Clef
     */
    public Clef getKey() {
        return key;
    }

    /**
     * Donne un point à l'équipe bleue
     */
    public void addBluePoint() {
        this.pointBlue++;
    }

    /**
     * Donne un point à l'équipe rouge
     */
    public void addRedPoint() {
        this.pointRed++;
    }

    /**
     * Donne un point à l'équipe qui joue
     */
    public void updatePoint(CardType color) {
        if (color == CardType.RED) {
            this.addRedPoint();
        } else {
            this.addBluePoint();
        }
    }

    public int getPointBlue() {
        return this.pointBlue;
    }

    public int getPointRed() {
        return this.pointRed;
    }


    /**
     * Explicite
     */
    public void changeTurn() {
        this.isBlueTurn = !this.isBlueTurn;
    }

    /**
     * Une grosse fonction qui change le tour de la personne si c'est une mauvaise carte ou game over
     */
    public void updateTurn(CardType color) {
        if ((color == CardType.BLUE && isRedTurn()) ||
                (color == CardType.RED && isBlueTurn()) ||
                color == CardType.WHITE) {
            changeTurn();
        }
    }
    public boolean isqrcodeaffiche(){return qrcodeaffiche;    }
    public void setqrcodeaffiche(boolean b){qrcodeaffiche =  b;   }
    public abstract CarteBase[][] getCards();
}
