package linguacrypt.model;

import java.io.Serializable;

public class GameStatistics implements Serializable {
    private int gameId;
    private int redCorrectGuesses = 0;
    private int redTotalGuesses = 0;
    private int blueCorrectGuesses = 0;
    private int blueTotalGuesses = 0;
    private int redCardsGivenToRed = 0;
    private int blueCardsGivenToBlue = 0;
    private boolean blueTeamWon;
    private int TotalTime;

    public GameStatistics(int gameId) {
        this.gameId = gameId;
    }

    public void printAll() {
        System.out.println("Game ID: " + gameId);
        System.out.println("Red correct guesses: " + redCorrectGuesses);
        System.out.println("Red total guesses: " + redTotalGuesses);
        System.out.println("Blue correct guesses: " + blueCorrectGuesses);
        System.out.println("Blue total guesses: " + blueTotalGuesses);
        System.out.println("Red cards given to Red: " + redCardsGivenToRed);
        System.out.println("Blue cards given to Blue: " + blueCardsGivenToBlue);
        System.out.println("Blue team won: " + blueTeamWon);
        System.out.println("Total time: " + TotalTime);
    }

    public void addRedGuess(boolean correct) {
        redTotalGuesses++;
        if (correct) redCorrectGuesses++;
    }

    public void addBlueGuess(boolean correct) {
        blueTotalGuesses++;
        if (correct) blueCorrectGuesses++;
    }

    public double getRedGuessAccuracy() {
        return redTotalGuesses == 0 ? 0 : (redCorrectGuesses * 100.0 / redTotalGuesses);
    }

    public double getBlueGuessAccuracy() {
        return blueTotalGuesses == 0 ? 0 : (blueCorrectGuesses * 100.0 / blueTotalGuesses);
    }

    // Getters existants
    public int getGameId() { return gameId; }
    public int getRedCardsGivenToRed() { return redCardsGivenToRed; }
    public int getBlueCardsGivenToBlue() { return blueCardsGivenToBlue; }
    public boolean isBlueTeamWon() { return blueTeamWon; }
    public int getTotalTime() { return TotalTime; }

    // Tracking des cartes données à l'adversaire
    public void cardGivenToOpponent(boolean isBlueTeam) {
        if (isBlueTeam) {
            redCardsGivenToRed++;
        } else {
            blueCardsGivenToBlue++;
        }
    }

    // Setters
    public void setBlueTeamWon(boolean blueTeamWon) { this.blueTeamWon = blueTeamWon; }
    public void setTotalTime(int remainingTotalTime) { this.TotalTime = remainingTotalTime; }
}