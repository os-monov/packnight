package byog.Core;

import java.io.Serializable;
import java.util.*;

import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class MapGenerator implements Serializable {


    private static final int height = 33;
    private static final int width = 80;
    private static Random r;
    int playerScore;
    int playerHealth;
    // Map Floor Attributes
    private TETile[][] tiles;
    private int playerX;
    private int playerY;
    private int[][] floorTiles = new int[10000][2];  // Array of floor tile coordinates ([] int) => [][]
    private int[][] wallTiles = new int[100000][2];   // Array of wall tile coordinates ([] int) => [][]

    private int numberOfFloorTiles; // Number of floor tiles
    private int numberOfWallTiles; // Number of wall tiles

    private HashSet<Tile> setOfFloorTiles;
    private HashSet<Tile> setOfWallTiles;
    private HashSet<Tile> setOfHeartTiles;


    // General Constructor Method w/ only seed input
    public MapGenerator(String input) {

        this.tiles = new TETile[width][height];
        r = new Random(stringSeedtoInt(input));
        this.playerScore = 0;
        this.playerHealth = 5;
        fillBackground();
        addFloors();
        addRooms();

        setOfFloorTiles = createTileSetFromArray(floorTiles);
        floorTiles = createArrayFromTileSet(setOfFloorTiles);
        numberOfFloorTiles = floorTiles.length;

        addWalls();

        setOfWallTiles = createTileSetFromArray(wallTiles);
        wallTiles = createArrayFromTileSet(setOfWallTiles);
        numberOfWallTiles = wallTiles.length;

        addRadioactivity(25);
        setOfHeartTiles = new HashSet<>();
        addHearts(6);
        playerStart();

    }

    TETile[][] returnMap() {
        return tiles;
    }


    ///////////////////////////////////////////////
    //////////// Player Interactivity /////////////
    ///////////////////////////////////////////////

    private void playerStart() {
        int startIndex = RandomUtils.uniform(r, 0, numberOfFloorTiles);
        playerX = floorTiles[startIndex][0];
        playerY = floorTiles[startIndex][1];
        numberOfFloorTiles = setOfFloorTiles.size();
        tiles[playerX][playerY] = Tileset.PLAYER;
    }


    private void updateInformation(int tile) {
        if (tile == 1) {
            playerScore++;
        } else if (tile == 2) {
            playerHealth++;
        } else if (tile == 4) {
            playerHealth--;
        }

    }

    void playerMove(char direction) {
        int[] currentPlayerPosition = new int[]{playerX, playerY};

        if (isPlayerMoveValid(direction, currentPlayerPosition)) {

            int[] newPlayerPosition = createNewCoordinatePosition(direction, currentPlayerPosition);
            int tileType = tileType(currentPlayerPosition[0], currentPlayerPosition[1]);
            updateInformation(tileType(newPlayerPosition[0], newPlayerPosition[1]));

            if (tileType == 2) {
                setOfHeartTiles.remove(new Tile(currentPlayerPosition[0], currentPlayerPosition[1]));
            }

            tiles[playerX][playerY] = Tileset.NOTHING;
            playerX = newPlayerPosition[0];
            playerY = newPlayerPosition[1];
            tiles[playerX][playerY] = Tileset.PLAYER;

        } else {
            int[] temporaryPosition = createNewCoordinatePosition(direction, currentPlayerPosition);
            updateInformation(tileType(temporaryPosition[0], temporaryPosition[1]));
        }
    }


    private boolean isPlayerMoveValid(char dir, int[] playerPosition) {
        int xTest = playerPosition[0];
        int yTest = playerPosition[1];

        if (dir == 'D' || dir == 'd') {
            xTest += 1;
        } else if (dir == 'W' || dir == 'w') {
            yTest += 1;
        } else if (dir == 'A' || dir == 'a') {
            xTest -= 1;
        } else {
            yTest -= 1;
        }

        return setOfFloorTiles.contains(new Tile(xTest, yTest)) || setOfHeartTiles.contains(new Tile(xTest, yTest));
    }


    ///////////////////////////////////////////////
    /////////////// MAP GENERATION ////////////////
    ///////////////////////////////////////////////


    // Initializes every tile to nothing
    private void fillBackground() {
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.tiles[i][j] = Tileset.NOTHING;
            }
        }
    }


    // Randomly traverse through map while placing floor tiles in the process
    private void addFloors() {
        int xStart = 39;
        int yStart = 16;

        tiles[xStart][yStart] = Tileset.FLOOR;
        floorTiles[numberOfFloorTiles] = new int[]{xStart, yStart};
        numberOfFloorTiles++;


        for (int iterations = 0; iterations < 20; iterations++) {
            int xCurr = xStart;
            int yCurr = yStart;
            int len = RandomUtils.uniform(r, 1, 5);
            int[] newPosition = new int[]{xCurr, yCurr};


            for (int j = 0; j < 75; j++) {
                int direction = RandomUtils.uniform(r, 1, 5);


                switch (direction) {
                    case 1:
                        newPosition = move("right", xCurr, yCurr, len);
                        break;
                    case 2:
                        newPosition = move("up", xCurr, yCurr, len);
                        break;
                    case 3:
                        newPosition = move("left", xCurr, yCurr, len);
                        break;
                    case 4:
                        newPosition = move("down", xCurr, yCurr, len);
                        break;
                    default:
                        System.out.println("Error: Invalid Direction, couldn't move");
                }
                xCurr = newPosition[0];
                yCurr = newPosition[1];
            }
        }

    }


    // Given a direction, (x,y) coordinate, and length (x), places x number of floor tiles in that direction
    private int[] move(String dir, int x, int y, int num) {
        for (int i = 0; i < num; i++) {
            switch (dir) {
                case "right":
                    if (isMoveValid(1, x, y)) {
                        x++;
                    }
                    break;

                case "left":
                    if (isMoveValid(3, x, y)) {
                        x--;
                    }
                    break;

                case "up":
                    if (isMoveValid(2, x, y)) {
                        y++;
                    }
                    break;

                case "down":
                    if (isMoveValid(4, x, y)) {
                        y--;
                    }
                    break;
            }

            tiles[x][y] = Tileset.FLOOR;
            floorTiles[numberOfFloorTiles] = new int[]{x, y};
            numberOfFloorTiles++;
        }
        return new int[]{x, y};
    }

    // Checks if moving in a certain direction from (x,y) is valid
    private boolean isMoveValid(int direction, int x, int y) {
        boolean maxX = (x < 76);
        boolean maxY = (y < 26);
        boolean minX = (x > 3);
        boolean minY = (y > 3);

        if (direction == 1) {
            return maxX;
        } else if (direction == 2) {
            return maxY;
        } else if (direction == 3) {
            return minX;
        } else {
            return minY;
        }
    }

    // Checks if a (x,y) position is a legal tile
    private boolean isLegalTile(int x, int y) {
        return x < (width - 1) && x > 1 && y > 1 && y < (height - 1);
    }


    // Chooses random dimensions and start point for a room & then place floor tiles
    private void addRoom() {
        int roomWidth = RandomUtils.uniform(r, 4, 18);
        int roomHeight = RandomUtils.uniform(r, 4, 9);
        int randomIndex = RandomUtils.uniform(r, 0, numberOfFloorTiles);
        int[] roomPosition = floorTiles[randomIndex];

        while (!isRoomValid(roomPosition, roomWidth, roomHeight)) {
            randomIndex = RandomUtils.uniform(r, 0, numberOfFloorTiles);
            roomPosition = floorTiles[randomIndex];
        }

        for (int x = roomPosition[0]; x < roomPosition[0] + roomWidth; x++) {
            for (int y = roomPosition[1]; y < roomPosition[1] + roomHeight; y++) {
                tiles[x][y] = Tileset.FLOOR;
                floorTiles[numberOfFloorTiles] = new int[]{x, y};
                numberOfFloorTiles++;
            }
        }
    }

    // Repeatedly adds 6 - 12 rooms
    private void addRooms() {
        int numRooms = RandomUtils.uniform(r, 6, 12);
        for (int i = 0; i < numRooms; i++) {
            addRoom();
        }
    }

    // Checks whether a room @ start point (x,y) with dimensions (xDim x yDim) is valid
    private boolean isRoomValid(int[] coords, int xDim, int yDim) {
        boolean x = ((coords[0] - xDim) <= 1 || (coords[0] + xDim) >= 79);
        boolean y = ((coords[1] - yDim) <= 1 || (coords[1] + yDim) >= 29);
        return !(x || y);
    }


    // Turns every 'nothing' tile around a floor tile into a wall tile
    private void addWalls() {
        for (int i = 0; i < numberOfFloorTiles; i++) {
            int[][] currSurroundingTiles = getSurroundingTiles(floorTiles[i]);
            for (int j = 0; j < 8; j++) {
                int x = currSurroundingTiles[j][0];
                int y = currSurroundingTiles[j][1];

                if (isLegalTile(x, y) && tiles[x][y].equals(Tileset.NOTHING)) {
                    tiles[x][y] = Tileset.WALL;
                    int[] coord = new int[]{x, y};
                    wallTiles[numberOfWallTiles] = coord;
                    numberOfWallTiles++;
                }
            }
        }
    }

    private int[][] getSurroundingTiles(int[] position) {
        int[][] surroundings = new int[8][2];
        surroundings[0] = new int[]{position[0] - 1, position[1] + 1};
        surroundings[1] = new int[]{position[0], position[1] + 1};
        surroundings[2] = new int[]{position[0] + 1, position[1] + 1};

        surroundings[3] = new int[]{position[0] - 1, position[1]};
        surroundings[4] = new int[]{position[0] + 1, position[1]};

        surroundings[5] = new int[]{position[0] - 1, position[1] - 1};
        surroundings[6] = new int[]{position[0], position[1] - 1};
        surroundings[7] = new int[]{position[0] + 1, position[1] - 1};

        return surroundings;

    }


    // Takes array of (x,y) coordinates [x, y] and condense into a GIVEN HashSet
    private int[][] createArrayFromTileSet(HashSet<Tile> tileSet) {

        int[][] copyArray = new int[tileSet.size()][2];
        Iterator<Tile> copyArrayIterator = tileSet.iterator();
        int i = 0;

        while (copyArrayIterator.hasNext()) {
            Tile next = copyArrayIterator.next();
            copyArray[i] = new int[]{next.getX(), next.getY()};
            i++;
        }

        return copyArray;
    }

    private HashSet<Tile> createTileSetFromArray(int[][] tiles) {
        HashSet<Tile> copySet = new HashSet<>();

        for (int[] pair : tiles) {
            Tile tile = new Tile(pair[0], pair[1]);
            copySet.add(tile);
        }
        return copySet;

    }

    private void addHearts(int num) {
        for (int i = 0; i < num; i++) {
            int random = RandomUtils.uniform(r, 0, numberOfFloorTiles); // Picks index of a random floor tile
            int x = floorTiles[random][0]; // extracts x-position
            int y = floorTiles[random][1]; // extracts y-position
            tiles[x][y] = Tileset.HEART;
            Tile heartTile = new Tile(x, y);
            setOfHeartTiles.add(heartTile);
            setOfFloorTiles.remove(heartTile);
            numberOfFloorTiles--;
        }

    }

    private void addRadioactivity(int num) {
        for (int i = 0; i < num; i++) {
            int random = RandomUtils.uniform(r, 0, numberOfWallTiles); // Picks index of a random wall tile
            int x = wallTiles[random][0];
            int y = wallTiles[random][1];
            tiles[x][y] = Tileset.RADIOACTIVE;
            Tile radioactiveTile = new Tile(x, y);
            setOfWallTiles.remove(radioactiveTile);
            numberOfWallTiles--;
        }
    }
    /////////////////////////////////////////


    // Transforms string input seed into a number
    private long stringSeedtoInt(String input) {
        String[] badChars = new String[]{"N", "n", "S", "s"};

        for (String bchar : badChars) {
            input = input.replace(bchar, "");

        }
        return Long.parseLong(input);
    }


    // Returns tile type (Nothing = 0, Floor = 1, Heart = 2, Player = 3, Radioactive = 4, Wall = 5)
    private int tileType(int x, int y) {

        String type = tiles[x][y].description();

        switch (type) {
            case "floor":
                return 1;

            case "heart":
                return 2;

            case "player":
                return 3;

            case "radioactive":
                return 4;


            case "wall":
                return 5;

            default:
                return 0;
        }
    }

    private int[] createNewCoordinatePosition(char direction, int[] currentCoordinates) {
        try {
            switch (direction) {
                case 'D':
                case 'd':
                    currentCoordinates[0]++;
                    break;
                case 'W':
                case 'w':
                    currentCoordinates[1]++;
                    break;
                case 'A':
                case 'a':
                    currentCoordinates[0]--;
                    break;
                case 'S':
                case 's':
                    currentCoordinates[1]--;
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid Direction");
        }
        return currentCoordinates;
    }


}




