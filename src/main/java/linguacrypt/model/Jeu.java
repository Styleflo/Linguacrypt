package linguacrypt.model;

import java.util.ArrayList;
import linguacrypt.controller.*;

/**
 * Contient une liste d'observer qui peuvent etre notifiés lors d'un quelconque changement
 * Contient un Plateau de cartes de mots ou d'images
 * Contient une string du nom de la vue courante à afficher
 **/
public class Jeu {
    private ArrayList<Observer> observers = new ArrayList<>();
    private Plateau plateau;
    private boolean win = false;
    private String currentView;


    public Jeu(Plateau plateau) {
        this.plateau = plateau;
        //A completer avec le nom de la vue initial à afficher
        currentView = "";
    }

    /**
     * Explicite
     **/
    public Plateau getPlateau() {
        return this.plateau;
    }

    /**
     * Permet de set un plateau specifique
     * @param plateau
     **/
    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    /**
     * Permet de recuperer la liste des observers
     **/
    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    /**
     * Ajoute un observer à la liste des observeur du model Jeu
     * @param Observer
     **/
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    /**
     * Supprime un observer à la liste des observeur du model Jeu
     * @param Observer
     **/
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    /**
     * Permet de recuperer la vue courrante
     **/
    public void getView() {
        return this.currentView;
    }

    /**
     * Permet de set une nouvelle vue courrante pour changer l'affichage
     * @param currentView
     **/
    public void setView(String currentView) {
        this.currentView = currentView;
    }

    /**
     * Permet de savoir si une équipe a déjà gagné
     * @return boolean
     **/
    public boolean isWin() {
        return this.win;
    }

    /**
     * Cette fonction doit etre appelée apres chaque modification du jeu
     * Elle permet de notifier à chaque controler de reagir à ce changement
     * Le changement se propage ainsi aux views
     **/
    public void notifyObservers() {
        for (Observer o : observers) {
            o.reagir();
        }
    }

    /**
     * Permet de relancer une partie avec de nouvelles cartes
     **/
    public void newGame() {
        this.plateau = new Plateau();
        //faire la logique que recommence une partie
    }

    /**
     * Permet de changer l'etat du jeu en gagné
     * une equipe à alors gagné
     **/
    public void win() {
        this.win = true;
        // A completer avec le nom de la vue de win
        this.currentView = "";
    }

}