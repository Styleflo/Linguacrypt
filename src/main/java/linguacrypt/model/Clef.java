package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Clef {
    private final boolean blueStarts;
    private final int[][] grid;
    private int[] cardsCounts;

    public Clef(int width, int height) {
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
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                grid[i][j] = types.get(index++);
            }
        }
    }

    public Clef(int[] size) {
        this(size[0], size[1]);
    }

    public Clef() {
        this(5, 5);
    }

    public boolean isBlueStarting() { return blueStarts; }

    public boolean isRedStarting() { return !blueStarts; }

    public int[][] getGrid() {
        return grid;
    }

    public int getCardType(int i, int j) {
        /* Utilise les coordonnées matricielles */
        return grid[i][j];
    }

    public int[] getCardsCounts() { return cardsCounts; }
}
