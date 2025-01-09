package linguacrypt.model;

import linguacrypt.config.GameConfig;
import linguacrypt.controller.Observer;
import linguacrypt.utils.CardsDataManager;
import linguacrypt.utils.DataUtils;
import linguacrypt.utils.ImagesFileHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contient une liste d'observers qui peuvent être notifiés lors d'un quelconque changement.
 * Contient une wordfilHandler
 * Contient une string du nom de la vue courante à afficher
 */
public class Jeu {
    private final ArrayList<Observer> observers = new ArrayList<>();
    private final CardsDataManager cardsDataManager;
    private final ImagesFileHandler imagesFileHandler;
    private Partie partie;
    private String currentView;
    private int victoireBleue;
    private int victoireRouge;

    public int getVictoireBleue() {
        return victoireBleue;
    }

    public void victoireBleue() {
        this.victoireBleue = victoireBleue +1;
    }

    public int getVictoireRouge() {
        return victoireRouge;
    }

    public void victoireRouge() {
        this.victoireRouge = victoireRouge +1;
    }

    public Jeu() throws IOException {
        // Peut etre des trucs à faire mais pour l'instant ça va !
        currentView = "MenuInitial";
        cardsDataManager = new CardsDataManager(GameConfig.CARDS_FILE);
        imagesFileHandler = new ImagesFileHandler(GameConfig.CARDS_IMAGES_FILE);
        victoireBleue = 0;
        victoireRouge = 0;
    }

    /**
     * Permet de recuperer la liste des observers.
     *
     * @return ArrayList<Observer>
     */
    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    /**
     * Permet de recuperer le FileHandler pour les images
     *
     * @return ImagesFileHandler
     */
    public ImagesFileHandler getImagesFileHandler() {
        return this.imagesFileHandler;
    }

    /**
     * Ajoute un observer à la liste des observers.
     *
     * @param observer
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Supprime un observer de la liste des observers.
     *
     * @param observer
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Permet de recuperer la partie en cours
     *
     * @return Partie
     */
    public Partie getPartie() {
        DataUtils.assertNotNull(this.partie, "La partie n'a pas été initialisée dans Jeu.getPartie()");

        return this.partie;
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
     *
     * @return String
     */
    public String getView() {
        return this.currentView;
    }

    /**
     * Permet de set une nouvelle vue courrante pour changer l'affichage.
     *
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

    public CardsDataManager getWordsFileHandler() {
        return cardsDataManager;
    }
}
