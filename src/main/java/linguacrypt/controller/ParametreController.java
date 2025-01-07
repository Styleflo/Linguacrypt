package linguacrypt.controller;

import linguacrypt.model.Jeu;

public class ParametreController implements Observer {

    private Jeu jeu;

    public ParametreController(){}

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void reagir() {

    }
}
