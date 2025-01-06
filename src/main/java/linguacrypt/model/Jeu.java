package linguacrypt.model;

import java.util.ArrayList;
import linguacrypt.controller.*;

/**
 * Contient une liste d'observer qui peuvent etre notifiés lors d'un quelconque changement.
 * Contient une partie
 * Contient une string du nom de la vue courante à afficher.
 * Contient une liste de l'ensemble des mots de la base de donnée (les cartes dispos)
 */
public class Jeu {
    private ArrayList<Observer> observers = new ArrayList<>();
    private Partie partie;
    private String currentView;
    private ArrayList<String> wordList;

    public Jeu() {
        //A completer avec le nom de la vue initial à afficher
        currentView = "";
    }

    /**
     * Permet de recuperer la liste des observers.
     */
    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    /**
     * Ajoute un observer à la liste des observeur du model Jeu.
     * @param Observer
     */
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    /**
     * Supprime un observer à la liste des observeur du model Jeu.
     * @param Observer
     */
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    /**
     * permet de lier une partie à notre jeu
     * cela lance la partie
     */
    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    /**
     * Permet de recuperer la vue courrante.
     * @return String
     */
    public String getView() {
        return this.currentView;
    }

    /**
     * Permet de set une nouvelle vue courrante pour changer l'affichage.
     * @param currentView
     */
    public void setView(String currentView) {
        this.currentView = currentView;
    }

    /**
     * Cette fonction doit etre appelée apres chaque modification du jeu.
     * Elle permet de notifier à chaque controler de reagir à ce changement.
     * Le changement se propage ainsi aux views.
     */
    public void notifyObservers() {
        for (Observer o : observers) {
            o.reagir();
        }
    }

    /**
     * Permet de changer l'etat du jeu en gagné.
     * Une equipe a alors gagné.
     */
    public void win() {
        this.partie.setWon();
        // A completer avec le nom de la vue de win
        this.currentView = "";
    }

}