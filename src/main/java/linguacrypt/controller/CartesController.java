package linguacrypt.controller;

import linguacrypt.model.Jeu;

public class CartesController implements Observer {

    private Jeu jeu;

    public CartesController() {

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }


    @Override
    public void reagir() {

    }
}
