package linguacrypt.model;

import linguacrypt.controller.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

class JeuTest {
    private Jeu jeu;

    @BeforeEach
    void setUp() throws IOException {
        jeu = new Jeu();
    }

    @Test
    void initializeNewGameStatistics_createsNewGameStatistics() {
        jeu.initializeNewGameStatistics();
        assertEquals(1, jeu.getGameStatistics().size());
        assertNotNull(jeu.getCurrentGameStats());
    }

    @Test
    void victoireBleue_incrementsVictoireBleue() {
        int initialVictoireBleue = jeu.getVictoireBleue();
        jeu.victoireBleue();
        assertEquals(initialVictoireBleue + 1, jeu.getVictoireBleue());
    }

    @Test
    void victoireRouge_incrementsVictoireRouge() {
        int initialVictoireRouge = jeu.getVictoireRouge();
        jeu.victoireRouge();
        assertEquals(initialVictoireRouge + 1, jeu.getVictoireRouge());
    }

    @Test
    void addObserver_addsObserverToList() {
        Observer observer = new Observer() {
            @Override
            public void reagir() {
                // Do nothing
            }

            public boolean isNotified() {
                return false;
            }
        };
        jeu.addObserver(observer);
        assertTrue(jeu.getObservers().contains(observer));
    }

    @Test
    void removeObserver_removesObserverFromList() {
        Observer observer = new Observer() {
            @Override
            public void reagir() {
                // Do nothing
            }

            public boolean isNotified() {
                return false;
            }
        };
        jeu.addObserver(observer);
        jeu.removeObserver(observer);
        assertFalse(jeu.getObservers().contains(observer));
    }

    @Test
    void setPartie_initializesNewGameStatistics() {
        Partie partie = new Partie();
        jeu.setPartie(partie);
        assertNotNull(jeu.getPartie());
        assertEquals(partie, jeu.getPartie());
        assertEquals(1, jeu.getGameStatistics().size());
    }

    @Test
    void setView_updatesCurrentView() {
        String newView = "NewView";
        jeu.setView(newView);
        assertEquals(newView, jeu.getView());
    }

    @Test
    void notifyObservers_notifiesAllObservers() {
        Observer observer = new Observer() {
            boolean notified = false;

            @Override
            public void reagir() {
                notified = true;
            }

            public boolean isNotified() {
                return notified;
            }
        };
        jeu.addObserver(observer);
        jeu.notifyObservers();
        assertTrue(((Observer) observer).isNotified());
    }
}
