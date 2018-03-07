
package byog.Core;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.Random;

import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

public class MapGenerator implements Serializable {

    private static final int HEIGHT = 30;
    private static final int WIDTH = 80;
    static TETile[][] TETILE_WORLD = new TETile[WIDTH][HEIGHT];
    private static long randomSEED;
    private static String FULLSEED;
    private Random r;
    private int[][] ft = new int[8000][2];
    private int ftai = 0;
    private int[][] wt = new int[8000][2];
    private int wtai = 0;
    private int[][] st = new int[100][2];
    private int stai = 0;
    private static final int XSTART = 38; //RandomUtils.uniform(r, 30, 50);
    private static final int YSTART = 15; // RandomUtils.uniform(r, 12, 18);
    protected static int X_CURRENT = XSTART;
    private static int Y_CURRENT = YSTART;
    protected static int PLAYER_X;
    protected static int PLAYER_Y;
    static int SCORE = 0;
    static int HEALTH = 1;
    static String moves;




    public MapGenerator(String input) {
        randomSEED = numericalseed(input);
        r = new Random(randomSEED);

    }


    private boolean inArray(int[][] array, int[] item){
        for (int j = 0; j < array.length; j++){
            if (array[j][0] == (item[0]) && array[j][1] == (item[1])){
                return true;
            }
        }
        return false;
    }

    private TETile[][] ctw() {

        fillTileBackground(TETILE_WORLD);
        addFloors();
        addRooms();
        addWalls();
        ft = cleanTheTiles(ft);
        PlayerStart();
        spikedWalls(25);
        addFlowers(6);
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
        return Long.parseLong(input);
    }

//    public long numberSEED(String input){
//        return Long.parseLong(input);
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ///// INTERACTIVITY ///////
    ////////////////////////////////////////////////////////////////////////////////////////////


    private void PlayerStart() {
        int start = RandomUtils.uniform(r, 0, ftai);
        PLAYER_X = ft[start][0];
        PLAYER_Y = ft[start][1];
        TETILE_WORLD[PLAYER_X][PLAYER_Y] = Tileset.PLAYER;


    }


    public int TileType(int x, int y, TETile[][] world) {
        String type = world[PLAYER_X][PLAYER_Y].description();

        if (type.equals("floor")) {
            return 1;
        }

        else if (type.equals("flower")){
            return 2;
        }

        else return 0;
    }


    public void updateHUD (int x){
        if (x == 1 && HEALTH > 0){
            SCORE += 1;
        }

        else if (x == 2){
            HEALTH += 1;
        }

    }

    public void playerMove(char direction, TETile[][] world) {

        if (direction == 'D' || direction == 'd') {
            if (isMoveValid(direction)) {
                PLAYER_X++;
                updateHUD(TileType(PLAYER_X - 1, PLAYER_Y, world));
                world[PLAYER_X - 1][PLAYER_Y] = Tileset.NOTHING;
                world[PLAYER_X][PLAYER_Y] = Tileset.PLAYER;
            }
            if (isWallSpiked(direction)) {
                HEALTH -= 1;
            }
        }

        else if (direction == 'W' || direction == 'w') {
            if (isMoveValid(direction)) {
                PLAYER_Y++;
                updateHUD(TileType(PLAYER_X, PLAYER_Y - 1, world));
                world[PLAYER_X][PLAYER_Y - 1] = Tileset.NOTHING;
                world[PLAYER_X][PLAYER_Y] = Tileset.PLAYER;

            }
            if (isWallSpiked(direction)) {
                HEALTH -= 1;
            }
        }

        else if (direction == 'A' || direction == 'a') {
            if (isMoveValid(direction)) {
                PLAYER_X--;
                updateHUD(TileType(PLAYER_X + 1, PLAYER_Y, world));
                world[PLAYER_X + 1][PLAYER_Y] = Tileset.NOTHING;
                world[PLAYER_X][PLAYER_Y] = Tileset.PLAYER;

            }
            if (isWallSpiked(direction)) {
                HEALTH -= 1;
            }
        }

        else if (direction == 'S' || direction == 's'){
            if (isMoveValid(direction)) {
                PLAYER_Y--;
                updateHUD(TileType(PLAYER_X, PLAYER_Y - 1, world));
                world[PLAYER_X][PLAYER_Y + 1] = Tileset.NOTHING;
                world[PLAYER_X][PLAYER_Y] = Tileset.PLAYER;

            }
            if (isWallSpiked(direction)) {
                HEALTH -= 1;
            }

        }
    }


    private boolean isMoveValid(char direction) {
        int[] test_coordinates = new int[]{PLAYER_X, PLAYER_Y};

        if (direction == 'D' || direction == 'd'){
            test_coordinates[0] += 1;
        }

        else if (direction == 'W' || direction == 'w'){
            test_coordinates[1] += 1;
        }

        else if (direction == 'A' || direction == 'a'){
            test_coordinates[0] -= 1;
        }

        else {
            test_coordinates[1] -= 1;
        }

        return inArray(ft, test_coordinates);
    }

    private boolean isWallSpiked(char direction) {
        int[] test_coordinates = new int[]{PLAYER_X, PLAYER_Y};

        if (direction == 'D' || direction == 'd'){
            test_coordinates[0] += 1;
        }

        else if (direction == 'W' || direction == 'w'){
            test_coordinates[1] += 1;
        }

        else if (direction == 'A' || direction == 'a'){
            test_coordinates[0] -= 1;
        }

        else {
            test_coordinates[1] -= 1;
        }
        return inArray(st, test_coordinates);
    }





    ////////////////////////////////////////////////////////////////////////////////////////////
    ///// MAP GENERATION ///////
    ////////////////////////////////////////////////////////////////////////////////////////////

    private static void fillTileBackground(TETile[][] ta) {
        for (int i = 0; i < ta.length; i += 1) {
            for (int j = 0; j < ta[0].length; j += 1) {
                ta[i][j] = Tileset.NOTHING;
            }
        }
    }

    private int [][] cleanTheTiles(int[][] tilecoord) {
        int [][] cleancopy = new int [ftai][2];
        int copyindex = 0;

        for (int i = 0; i < tilecoord.length; i++) {
            if (tilecoord[i] != null) {
                if (!inArray(cleancopy, tilecoord[i])) {
                    cleancopy[copyindex] = tilecoord[i];
                    copyindex += 1;

                }
            }
        }

        ftai = copyindex;
        return cleancopy;

    }

    private boolean isValid(int direction) {
        if (direction == 1) {
//            return (X_CURRENT != 76 && X_CURRENT != 77 && X_CURRENT != 78);
            return (X_CURRENT!= 76 && X_CURRENT != 77 && X_CURRENT != 78);
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
            ft[ftai] = new int[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveLeft() {
        for (int i = 0; i < 3; i++) {
            X_CURRENT -= 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new int[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveUp() {
        for (int i = 0; i < 3; i++) {
            Y_CURRENT += 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new int[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }

    public void moveDown() {
        for (int i = 0; i < 3; i++) {
            Y_CURRENT -= 1;
            TETILE_WORLD[X_CURRENT][Y_CURRENT] = Tileset.FLOOR;
            ft[ftai] = new int[]{X_CURRENT, Y_CURRENT};
            ftai += 1;
        }
    }


    private void addFloors() {
        TETILE_WORLD[XSTART][YSTART] = Tileset.FLOOR;
        ft[ftai] = new int[]{XSTART, YSTART};
        ftai += 1;



        for (int repeats = 0; repeats < 5; repeats++) {
            X_CURRENT = XSTART; //RandomUtils.uniform(r, 10, 75);
            Y_CURRENT = YSTART; //RandomUtils.uniform(r, 5, 19);

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

    private void addFlowers(int num) {
        for (int i = 0; i < num; i++){
            int flowerindex = RandomUtils.uniform(r, 0, ftai);
            int flowerx = ft[flowerindex][0];
            int flowery = ft[flowerindex][1];
            TETILE_WORLD[flowerx][flowery] = Tileset.FLOWER;
        }
    }

    private int[][] getSurroundings(int[] floorCoords) {
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

    private void addWalls() {
        for (int i = 0; i < ftai; i++) {
            int[][] theseSurroundings = getSurroundings(ft[i]);
            for (int j = 0; j < 8; j++) {
                int xcurr = theseSurroundings[j][0];
                int ycurr = theseSurroundings[j][1];
                if (TETILE_WORLD[xcurr][ycurr].equals(Tileset.NOTHING)) {
                    TETILE_WORLD[xcurr][ycurr] = Tileset.WALL;
                    wt[wtai] = new int[]{xcurr,ycurr};
                    wtai += 1;
                }
            }
        }
    }

    private void spikedWalls(int num) {
        for (int i = 0; i < num; i++){
            int spikeindex = RandomUtils.uniform(r, 0, wtai);
            int spikex = wt[spikeindex][0];
            int spikey = wt[spikeindex][1];
            TETILE_WORLD[spikex][spikey] = Tileset.SPIKED_WALL;
            st[stai] = new int[]{spikex, spikey};
            stai += 1;
        }
    }

    private void addRooms() {
        int numRooms = RandomUtils.uniform(r, 24, 30);
        for (int i = 0; i < numRooms; i++) {
            addRoom();
        }
    }




    private boolean isRoomValid(int[] coords, int xDim, int yDim) {
        boolean x = ((coords[0] - xDim) <= 0 || (coords[0] + xDim) >= 79);
        boolean y = ((coords[1] - yDim) <= 0 || (coords[1] + yDim) >= 29);
        return !(x || y);
    }


    private void addRoom() {
        int xDim = RandomUtils.uniform(r, 2, 8);
        int yDim = RandomUtils.uniform(r, 2, 8);
        int roomindex = RandomUtils.uniform(r, 0, ftai);
        int[] roomcoordinates = ft[roomindex];

        while (!isRoomValid(roomcoordinates, xDim, yDim)) {
            roomindex = RandomUtils.uniform(r, 0, ftai);
            roomcoordinates = ft[roomindex];
        }

        int xroom = roomcoordinates[0];
        int yroom = roomcoordinates[1];


        for (int x = xroom; x < xroom + xDim; x++) {
            for (int y = yroom; y < yroom + yDim; y++) {
                TETILE_WORLD[x][y] = Tileset.FLOOR;
                ft[ftai] = new int[]{x, y};
                ftai += 1;
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
}
