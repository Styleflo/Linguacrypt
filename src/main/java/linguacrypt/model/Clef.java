package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import linguacrypt.config.GameConfig;
import linguacrypt.utils.CardType;
import linguacrypt.utils.DataUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Clef {
    private final boolean blueStarts;
    private final CardType[][] grid;
    private final int[] cardsCounts;
    private final int width;
    private final int height;

    public Clef(int width, int height) {
        this.width = width;
        this.height = height;
        int total = width * height;
        grid = new CardType[height][width];
        int blue_and_red_count, red_count, blue_count, white_count, black_count;

        Random random = new Random();
        blueStarts = random.nextBoolean();

        black_count = (int) (GameConfig.BLACK_CARDS_PROPORTION * total);
        blue_and_red_count = (int) (GameConfig.BLUE_AND_RED_CARDS_PROPORTION * total);

        if (blueStarts) {
            red_count = blue_and_red_count;
            blue_count = blue_and_red_count + 1;
        } else {
            red_count = blue_and_red_count + 1;
            blue_count = blue_and_red_count;
        }

        white_count = width * height - (blue_and_red_count * 2 + 1) - black_count;

        cardsCounts = new int[4];
        cardsCounts[CardType.BLUE.ordinal()] = blue_count;
        cardsCounts[CardType.RED.ordinal()] = red_count;
        cardsCounts[CardType.BLACK.ordinal()] = black_count;
        cardsCounts[CardType.WHITE.ordinal()] = white_count;

        List<CardType> types = new ArrayList<>();
        for (int i = 0; i < blue_count; i++) types.add(CardType.BLUE);
        for (int i = 0; i < red_count; i++) types.add(CardType.RED);
        for (int i = 0; i < black_count; i++) types.add(CardType.BLACK);
        for (int i = 0; i < white_count; i++) types.add(CardType.WHITE);

        Collections.shuffle(types);

        int index = 0;

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                grid[i][j] = types.get(index++);
            }
        }

        try {
            this.write_qrcode();
        } catch (Exception e) {
            DataUtils.logException(e, "Erreur lors de la génération du QR code");
        }
    }

    @JsonCreator
    public Clef(@JsonProperty ("redStarting") boolean redStarts, @JsonProperty("blueStarting") boolean blueStarts, @JsonProperty ("grid") CardType[][] grid,
                @JsonProperty ("cardsCounts") int[] cardsCounts, @JsonProperty("width") int width,
                @JsonProperty ("height") int height) {
        this.blueStarts = blueStarts;
        this.grid = grid;
        this.cardsCounts = cardsCounts;
        this.width = width;
        this.height = height;
    }

    public Clef(int[] size) {
        this(size[0], size[1]);
    }

    public Clef() {
        this(GameConfig.DEFAULT_WIDTH, GameConfig.DEFAULT_HEIGHT);
    }

    public WritableImage bitMatrixToImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean pixel = bitMatrix.get(x, y); // Vérifie si le pixel fait partie du QR code
                pixelWriter.setColor(x, y, pixel ? Color.BLACK : Color.WHITE); // Noir pour les pixels actifs, blanc pour les autres
            }
        }

        return image;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isBlueStarting() {
        return blueStarts;
    }

    public boolean isRedStarting() {
        return !blueStarts;
    }

    public CardType[][] getGrid() {
        return grid;
    }

    public CardType getCardType(int i, int j) {
        /* Utilise les coordonnées matricielles */
        return grid[i][j];
    }

    public int[] getCardsCounts() {
        /* [bleu, rouge, noir, blanc] */
        return cardsCounts;
    }

    public void prettyPrint() {
        Map<CardType, String> int_to_square = new HashMap<>();
        int_to_square.put(CardType.BLUE, GameConfig.BLUE_TEXT_QRCODE);
        int_to_square.put(CardType.RED, GameConfig.RED_TEXT_QRCODE);
        int_to_square.put(CardType.BLACK, GameConfig.BLACK_TEXT_QRCODE);
        int_to_square.put(CardType.WHITE, GameConfig.WHITE_TEXT_QRCODE);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String square = int_to_square.get(grid[i][j]);
                System.out.print(square);
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        Map<CardType, String> intToSquare = Map.of(
                CardType.BLUE, GameConfig.BLUE_TEXT_QRCODE,
                CardType.RED, GameConfig.RED_TEXT_QRCODE,
                CardType.BLACK, GameConfig.BLACK_TEXT_QRCODE,
                CardType.WHITE, GameConfig.WHITE_TEXT_QRCODE
        );

        StringBuilder res = new StringBuilder(blueStarts ? GameConfig.BLUE_STARTS_TEXT_QRCODE : GameConfig.RED_STARTS_TEXT_QRCODE);
        res.append("\n");

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                res.append(intToSquare.get(grid[i][j]));
            }
            res.append("\n");
        }

        return res.toString();
    }

    public BitMatrix to_qrcode() throws WriterException, IOException {
        // C'est un peu le foutoir dans les coordonnées, mais ça marche
        JSONObject json = new JSONObject();
        json.put("height", this.width);
        json.put("width", this.height);
        json.put("blue_starts", this.blueStarts);

        JSONArray gridArray = new JSONArray();
        for (int i = 0; i < width; i++) {
            JSONArray rowArray = new JSONArray();
            for (int j = 0; j < height; j++) {
                rowArray.put(grid[j][i].name());
            }
            gridArray.put(rowArray);
        }
        json.put("grid", gridArray);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(json.toString(), BarcodeFormat.QR_CODE, 300, 300);
        return bitMatrix;
    }

    public void write_qrcode() throws WriterException, IOException {
        BitMatrix bitMatrix = this.to_qrcode();
        Path path = new File(GameConfig.QRCODE_PATH).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
