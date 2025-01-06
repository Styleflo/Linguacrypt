package linguacrypt.controller;

import linguacrypt.model.Jeu;

public class MenuInitialController implements Observer {

    private Jeu jeu;

    public MenuInitialController() {

    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }


    @Override
    public void reagir() {

    }
}
