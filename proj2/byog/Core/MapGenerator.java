
package byog.Core;

import java.util.Random;

import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class MapGenerator {
    private static final int HEIGHT = 30;
    private static final int WIDTH = 80;
    static TETile[][] TETILE_WORLD = new TETile[WIDTH][HEIGHT];
    private static long SEED;
    private Random r;
    private Integer[][] ft = new Integer[8000][2];
    private Integer[][] ct = new Integer[2400][2];
    private int ftai = 0;
    private static final int XSTART = 38; //RandomUtils.uniform(r, 30, 50);
    private static final int YSTART = 15; // RandomUtils.uniform(r, 12, 18);
    private static int X_CURRENT = XSTART;
    private static int Y_CURRENT = YSTART;



    public MapGenerator(String input) {
        SEED = numericalseed(input);
        r = new Random(SEED);

    }


    private static void fillTileBackground(TETile[][] ta) {
        for (int i = 0; i < ta.length; i += 1) {
            for (int j = 0; j < ta[0].length; j += 1) {
                ta[i][j] = Tileset.NOTHING;
            }
        }
    }

    private boolean inArray(Integer[][] array, Integer[] item){
        for (int j = 0; j < array.length; j++){
            if (array[j][0] == item[0] && array[j][1] == item[1]){
                return true;
            }
        }
        return false;
    }

    private Integer[][] cleanTheTiles(Integer[][] tilecoord) {
        Integer[][] cleancopy = new Integer[ftai][2];
        int copyindex = 0;

        for (int i = 0; i < tilecoord.length; i++) {
            if (tilecoord[i] != null) {
                if (!inArray(cleancopy, tilecoord[i])) {
                    cleancopy[copyindex] = tilecoord[i];
                    copyindex += 1;

                }
            }
        }

        Integer[][] finalcopy = new Integer[copyindex][2];
        int findex = 0;

        for (int j = 0; j < finalcopy.length; j++) {
            if (cleancopy[j][0] != null & cleancopy[j][1] != null) {
                finalcopy[findex] = cleancopy[j];
                findex++;
            }
        }
        ftai = findex;
        return finalcopy;

    }

    private void testPrint(){
        for (int i = 0; i < ftai; i++){
            System.out.println(ft[i][0] + " " + ft[i][1]);
        }
    }

    private TETile[][] ctw() {

        fillTileBackground(TETILE_WORLD);
        addFloors();
        addRooms();
        addWalls();
        ft = cleanTheTiles(ft);
        testPrint();

        return TETILE_WORLD;
    }

    public TETile[][] generate() {
        TETile[][] world = ctw();
        return world;
    }

    private long numericalseed(String input) {
        input = input.replace("N", "");
        input = input.replace("n", "");
        input = input.replace("S", "");
        input = input.replace("s", "");
        Long finalseed = Long.parseLong(input);
        return finalseed;
    }


    private boolean isValid(int direction) {
        if (direction == 1) {
            return (X_CURRENT != 76 && X_CURRENT != 77 && X_CURRENT != 78);
        } else if (direction == 2) {
            return (Y_CURRENT != 26 && Y_CURRENT != 27 && Y_CURRENT != 28);
        } else if (direction == 3) {
            return (X_CURRENT != 3 && X_CURRENT != 2 && X_CURRENT != 1);
        } else {
            return (Y_CURRENT != 3 && Y_CURRENT != 2 && Y_CURRENT != 1);
        }


    }

    public void moveRight() {
        for (int i = 0; i < 3; i++) {
            X_CURRENT += 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new Integer[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveLeft() {
        for (int i = 0; i < 3; i++) {
            X_CURRENT -= 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new Integer[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveUp() {
        for (int i = 0; i < 3; i++) {
            Y_CURRENT += 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new Integer[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveDown() {
        for (int i = 0; i < 3; i++) {
            Y_CURRENT -= 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new Integer[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }


    private void addFloors() {
        TETILE_WORLD[XSTART][YSTART] = Tileset.FLOOR;
        ft[ftai] = new Integer[]{XSTART, YSTART};
        ftai += 1;


<<<<<<< HEAD
        for (int repeats = 0; repeats < 4; repeats++) {
            X_CURRENT = x_start; //RandomUtils.uniform(r, 10, 75);
            Y_CURRENT = y_start; //RandomUtils.uniform(r, 5, 19);
=======
        for (int repeats = 0; repeats < 5; repeats++) {
            X_CURRENT = XSTART; //RandomUtils.uniform(r, 10, 75);
            Y_CURRENT = YSTART; //RandomUtils.uniform(r, 5, 19);
>>>>>>> 4a401b4fe87c78433fc034772f9bf72595762e3e

            for (int i = 0; i < 100; i++) {
                int direction = RandomUtils.uniform(r, 1, 5);
                while (!isValid(direction)) {
                    direction = RandomUtils.uniform(r, 1, 5);
                }

                if (direction == 1) {
                    moveRight();
                } else if (direction == 2) {
                    moveUp();
                } else if (direction == 3) {
                    moveLeft();

                } else {
                    moveDown();

                }
            }
        }

    }

    private int[][] getSurroundings(Integer[] floorCoords) {
        int[][] surroundings = new int[8][2];
        surroundings[0] = new int[]{floorCoords[0] - 1, floorCoords[1] + 1};
        surroundings[1] = new int[]{floorCoords[0], floorCoords[1] + 1};
        surroundings[2] = new int[]{floorCoords[0] + 1, floorCoords[1] + 1};
        surroundings[3] = new int[]{floorCoords[0] + 1, floorCoords[1]};
        surroundings[4] = new int[]{floorCoords[0] + 1, floorCoords[1] - 1};
        surroundings[5] = new int[]{floorCoords[0], floorCoords[1] - 1};
        surroundings[6] = new int[]{floorCoords[0] - 1, floorCoords[1] - 1};
        surroundings[7] = new int[]{floorCoords[0] - 1, floorCoords[1]};

        return surroundings;

    }

    public void addWalls() {
        for (int i = 0; i < ftai; i++) {
            int[][] theseSurroundings = getSurroundings(ft[i]);
            for (int j = 0; j < 8; j++) {
                int xcurr = theseSurroundings[j][0];
                int ycurr = theseSurroundings[j][1];
                if (TETILE_WORLD[xcurr][ycurr] == Tileset.NOTHING) {
                    TETILE_WORLD[xcurr][ycurr] = Tileset.WALL;
                }
            }
        }
    }

    public void addRooms() {
        int numRooms = RandomUtils.uniform(r, 24, 30);
        for (int i = 0; i < numRooms; i++) {
            addRoom();

        }
    }


    public boolean isRoomValid(Integer[] coords, int xDim, int yDim) {
        boolean x = ((coords[0] - xDim) <= 0 || (coords[0] + xDim) >= 79);
        boolean y = ((coords[1] - yDim) <= 0 || (coords[1] + yDim) >= 29);
        return !(x || y);
    }


    private void addRoom() {
        Integer xDim = RandomUtils.uniform(r, 2, 8);
        Integer yDim = RandomUtils.uniform(r, 2, 8);
        Integer roomindex = RandomUtils.uniform(r, 0, ftai);
        Integer[] roomcoordinates = ft[roomindex];

        while (!isRoomValid(roomcoordinates, xDim, yDim)) {
            roomindex = RandomUtils.uniform(r, 0, ftai);
            roomcoordinates = ft[roomindex];
        }

        int xroom = roomcoordinates[0];
        int yroom = roomcoordinates[1];


        for (int x = xroom; x < xroom + xDim; x++) {
            for (int y = yroom; y < yroom + yDim; y++) {
                TETILE_WORLD[x][y] = Tileset.FLOOR;
                ft[ftai] = new Integer[]{x, y};
                ftai += 1;

            }
        }

    }
}
