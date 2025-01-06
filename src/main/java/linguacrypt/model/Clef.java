package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Clef {
    private final boolean blueStarts;
    private final ArrayList<ArrayList<Integer>> grid = new ArrayList<>();

    public Clef(int width, int height) {
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

        List<Integer> types = new ArrayList<>();
        for (int i = 0; i < blue_count; i++) types.add(0);
        for (int i = 0; i < red_count; i++) types.add(1);
        for (int i = 0; i < black_count; i++) types.add(2);
        for (int i = 0; i < white_count; i++) types.add(3);

        Collections.shuffle(types);

        int index = 0;
        for (int j = 0; j < height; j++) {
            ArrayList<Integer> line = new ArrayList<>();

            for (int i = 0; i < height; i++) {
                line.add(types.get(index++));
            }

            grid.add(line);
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

    public ArrayList<ArrayList<Integer>> getGrid() {
        return grid;
    }
}
