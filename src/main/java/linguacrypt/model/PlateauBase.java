package linguacrypt.model;

import java.util.Collections;

public abstract class PlateauBase {
    protected final Clef key;
    protected boolean isBlueTurn;
    protected final int[] coveredCardsCounts;
    protected int pointBlue;
    protected int pointRed;

    public PlateauBase(int width, int height) {
        key = new Clef(width, height);
        pointBlue = 0;
        pointRed = 0;
        isBlueTurn = key.isBlueStarting();
        coveredCardsCounts = new int[4];
    }

    public abstract CarteBase getCard(int i, int j);

    public boolean isBlueTurn() {
        return isBlueTurn;
    }

    public boolean isRedTurn() {
        return !isBlueTurn;
    }

    public int[] getCoveredCardsCounts() {
        return coveredCardsCounts;
    }

    public Clef getKey() {
        return key;
    }

    public void addBluePoint(){
        this.pointBlue ++ ;
    }

    public void addRedPoint(){
        this.pointRed ++ ;
    }

    public void updatePoint(int color){
        if (color == 1){
            this.addRedPoint();
        }else {
            this.addBluePoint();
        }
    }

    public void changeTurn(){
        this.isBlueTurn = !this.isBlueTurn;
    }

    public void updateTurn(int color){
        switch (color){
            case 0:
                if(this.isRedTurn()){
                    this.changeTurn();
                }
                break;
            case 1:
                if(this.isBlueTurn()){
                    this.changeTurn();
                }
                break;
            case 2:;
                break;
            case 3:
                this.changeTurn();
                break;
        }
    }

}
