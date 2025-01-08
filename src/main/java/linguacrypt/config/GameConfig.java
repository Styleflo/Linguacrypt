package linguacrypt.config;

public class GameConfig {
    public static int DEFAULT_WIDTH = 5;
    public static int DEFAULT_HEIGHT = 5;
    public static int DEFAULT_TIMER = -1;

    public static String CARDS_FILE = "cards.json";
    public static String QRCODE_PATH = "src/main/resources/assets/clef.png";

    public static double BLACK_CARDS_PROPORTION = 0.04;  // = 1/25
    public static double BLUE_AND_RED_CARDS_PROPORTION = 0.33;  // =~ 1/3

    public static String BLUE_TEXT_QRCODE =  "Bleu    ";
    public static String RED_TEXT_QRCODE =   "Rouge   ";
    public static String BLACK_TEXT_QRCODE = "Noir    ";
    public static String WHITE_TEXT_QRCODE = "Blanc   ";
    public static String BLUE_STARTS_TEXT_QRCODE = "Bleu commence";
    public static String RED_STARTS_TEXT_QRCODE = "Rouge commence";
    public static String BLUE_TURN_TEXT = "C'est le tour de l'équipe bleue";
    public static String RED_TURN_TEXT = "C'est le tour de l'équipe rouge";

    public static int CARTES_THEMES_HGAP = 15;
    public static int CARTES_THEMES_VGAP = 15;
    public static int CARTES_THEMES_PADDING = 33;
    public static int PLATEAU_HGAP = 25;
    public static int PLATEAU_VGAP = 25;
    public static int PLATEAU_PADDING = 80;

    public static int MAX_WORD_SIZE = 14;
}
