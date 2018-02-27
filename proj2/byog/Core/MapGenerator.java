package byog.Core;

import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class MapGenerator {
    private static Integer[] world_parameters;
    private static final int HEIGHT = 30;
    private static final int WIDTH = 80;
    static TETile[][] TETile_world = new TETile[WIDTH][HEIGHT];
    private static long SEED;
    private static Random r = new Random(SEED);
    private Integer[][] floor_tiles = new Integer[100][2];
    private int ft_array_index = 0;
    private static final int x_start = RandomUtils.uniform(r, 30, 50);
    private static final int y_start = RandomUtils.uniform(r, 12, 18);
    private static int x_current = x_start;
    private static int y_current = y_start;


    public MapGenerator(String input) {
        SEED = numerical_seed(input);
    }

    private static void fillTileBackground(TETile[][] tile_array) {
        for (int i = 0; i < tile_array.length; i += 1) {
            for (int j = 0; j < tile_array[0].length; j += 1) {
                tile_array[i][j] = Tileset.NOTHING;
            }
        }
    }

    private TETile[][] create_tile_world() {

        fillTileBackground(TETile_world);
        addFloors();
        // make walking snake from same position 4 ways , prob 150 steps each
        // add Rooms // extend random points on our snake
        // make walls by checking 8 tiles around and that they're not equal to floor
        return TETile_world;
    }

    public TETile[][] Generate() {
        TETile[][] world = create_tile_world();
        return world;
    }

    private long numerical_seed(String input) {
        input = input.replace("N", "");
        input = input.replace("n", "");
        input = input.replace("S", "");
        input = input.replace("n", "");
        Long final_seed = Long.parseLong(input);
        return final_seed;
    }

    private boolean isValid(int direction) {
        if (direction == 1) {
            return (x_current != 78);
        }

        if (direction == 2) {
            return (y_current != 28);
        }

        if (direction == 3) {
            return (x_current != 1);
        }

        else{

            return (y_current != 1);
        }


    }

    public void moveRight () {
        x_current += 1;
        TETile_world[x_current][y_current] = Tileset.FLOWER;
        floor_tiles[ft_array_index] = new Integer[]{x_current, y_current};
        ft_array_index += 1;
    }

    public void moveLeft() {
        x_current -= 1;
        TETile_world[x_current][y_current] = Tileset.FLOWER;
        floor_tiles[ft_array_index] = new Integer[]{x_current, y_current};
        ft_array_index += 1;
    }

    public void moveUp() {
        y_current += 1;
        TETile_world[x_current][y_current] = Tileset.FLOWER;
        floor_tiles[ft_array_index] = new Integer[]{x_current, y_current};
        ft_array_index += 1;
    }

    public void moveDown() {
        y_current -= 1;
        TETile_world[x_current][y_current] = Tileset.FLOWER;
        floor_tiles[ft_array_index] = new Integer[]{x_current, y_current};
        ft_array_index += 1;
    }

    private void addFloors () {
        TETile_world[x_current][y_current] = Tileset.FLOWER;

        for (int repeats = 0; repeats < 1; repeats++) {
            x_current = x_start;
            y_current = y_start;

            for (int i = 0; i < 63; i++) {
                int direction = RandomUtils.uniform(r, 1, 4);
                while (isValid(direction) != true) {
                    direction = RandomUtils.uniform(r, 1, 4);
                }

                if (direction == 1) {
                    moveRight();
                }

                if (direction == 2) {
                    moveUp();
                }

                if (direction == 3) {
                    moveLeft();

                } else {
                    moveDown();
                    ;
                }
            }
        }
    }
}














