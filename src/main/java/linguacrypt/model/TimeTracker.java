package linguacrypt.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeTracker {

    // Variables pour enregistrer l'heure de début et de fin
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Méthode pour démarrer le chronométrage
    public void start() {
        startTime = LocalDateTime.now();
        System.out.println("Start time recorded: " + startTime);
    }

    // Méthode pour arrêter le chronométrage
    public void stop() {
        endTime = LocalDateTime.now();
        System.out.println("End time recorded: " + endTime);
    }

    // Méthode pour calculer la durée entre startTime et endTime
    public Duration getDuration() {
        if (startTime == null || endTime == null) {
            throw new IllegalStateException("Start time or end time is not set");
        }
        return Duration.between(startTime, endTime);
    }

    // Méthode pour obtenir la durée sous forme lisible
    public String getFormattedDuration() {
        Duration duration = getDuration();
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

