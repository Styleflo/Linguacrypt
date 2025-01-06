package linguacrypt.model;

public class Jeu {
    private Plateau plateau;

}

public Jeu(Plateau plateau) {
    this.plateau = plateau;
}

public Plateau getPlateau() {
    return this.plateau;
}

public void setPlateau(Plateau plateau) {
    this.plateau = plateau;
}
