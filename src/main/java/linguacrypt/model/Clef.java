package linguacrypt.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import linguacrypt.utils.DataVerification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Clef {
    private final boolean blueStarts;
    private final int[][] grid;
    private final int[] cardsCounts;
    private final int width;
    private final int height;

    public Clef(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new int[height][width];
        int blue_and_red_count, red_count, blue_count, white_count, black_count;

        Random random = new Random();
        blueStarts = random.nextBoolean();

        black_count = 1;
        blue_and_red_count = width * height / 3;

        if (blueStarts) {
            red_count = blue_and_red_count;
            blue_count = blue_and_red_count + 1;
        } else {
            red_count = blue_and_red_count + 1;
            blue_count = blue_and_red_count;
        }

        white_count = width * height - (blue_and_red_count * 2 + 1) - black_count;

        cardsCounts = new int[4];
        cardsCounts[0] = blue_count;
        cardsCounts[1] = red_count;
        cardsCounts[2] = black_count;
        cardsCounts[3] = white_count;

        List<Integer> types = new ArrayList<>();
        for (int i = 0; i < blue_count; i++) types.add(0);
        for (int i = 0; i < red_count; i++) types.add(1);
        for (int i = 0; i < black_count; i++) types.add(2);
        for (int i = 0; i < white_count; i++) types.add(3);

        Collections.shuffle(types);

        int index = 0;

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                grid[i][j] = types.get(index++);
            }
        }

        try {
            this.to_qrcode();
        } catch (Exception e) {
            DataVerification.logException(e, "Erreur lors de la génération du QR code");
        }
    }

    public Clef(int[] size) {
        this(size[0], size[1]);
    }

    public Clef() {
        this(5, 5);
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

    public int[][] getGrid() {
        return grid;
    }

    public int getCardType(int i, int j) {
        /* Utilise les coordonnées matricielles */
        return grid[i][j];
    }

    public int[] getCardsCounts() {
        /* [bleu, rouge, noir, blanc] */
        return cardsCounts;
    }

    public void prettyPrint() {
        Map<Integer, String> int_to_square = new HashMap<>();
        int_to_square.put(0, "Blue  ");
        int_to_square.put(1, "Red   ");
        int_to_square.put(2, "Black ");
        int_to_square.put(3, "White ");

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String square = int_to_square.get(grid[i][j]);
                System.out.print(square);
            }
            System.out.println();
        }
    }

    public String toString() {
        Map<Integer, String> int_to_square = new HashMap<>();
        int_to_square.put(0, "Blue  ");
        int_to_square.put(1, "Red   ");
        int_to_square.put(2, "Black ");
        int_to_square.put(3, "White ");
        String res;

        if (blueStarts) {
            res = "bleu commence \n";
        } else {
            res = "rouge commence \n";
        }
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                res = res.concat(int_to_square.get(grid[i][j]));

            }
            res = res.concat("\n");
        }

        return res;
    }

    public void to_qrcode() throws WriterException, IOException {
        String text = this.toString();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        Path path = new File("src/main/resources/assets/clef.png").toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
