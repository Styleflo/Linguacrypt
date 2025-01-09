package linguacrypt.config;

public class GameConfig {
    public static int DEFAULT_WIDTH = 5;
    public static int DEFAULT_HEIGHT = 5;
    public static int DEFAULT_TIMER = -1;

    public static String CARDS_FILE = "cards.json";
    public static String CARDS_IMAGES_FILE = "cards_images.json";
    public static String QRCODE_PATH = "src/main/resources/assets/clef.png";
    public static String APP_DIR = ".linguacryptConfig";
    public static String USER_CONFIG_FILE = "userConfig.json";

    public static double BLACK_CARDS_PROPORTION = 0.04;  // = 1/25
    public static double BLUE_AND_RED_CARDS_PROPORTION = 0.33;  // =~ 1/3

    public static String BLUE_TEXT_QRCODE = "Bleu    ";
    public static String RED_TEXT_QRCODE = "Rouge   ";
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

    public static String RED_CARD_COLOR = "#ff6b6b";
    public static String BLUE_CARD_COLOR = "#4dabf7";
    public static String BLACK_CARD_COLOR = "#343a40";
    public static String WHITE_CARD_COLOR = "#f8f9fa";
    public static String WINNER_POPUP_BG_COLOR = "rgba(81, 81, 81, 0.9)";
}
