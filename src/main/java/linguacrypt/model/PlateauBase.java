package linguacrypt.model;

/**
 * Contient un une Clef (la feuille qui dit les mots à faire deviner
 * Contient un boolean si c'est le tour des bleu
 * Contient une liste des cartes couvertes
 * Contient le nombre de poitn bleu
 * Contient le nombre de poitn bleu
 */
public abstract class PlateauBase {
    protected final Clef key;
    protected final int[] coveredCardsCounts;
    protected boolean isBlueTurn;
    protected int pointBlue;
    protected int pointRed;

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
    public void updatePoint(int color) {
        if (color == 1) {
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
    public void updateTurn(int color) {
        switch (color) {
            case 0:
                if (this.isRedTurn()) {
                    this.changeTurn();
                }
                break;
            case 1:
                if (this.isBlueTurn()) {
                    this.changeTurn();
                }
                break;
            case 2:
                break;
            case 3:
                this.changeTurn();
                break;
        }
    }

}
