package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Font;
import java.awt.Color;
import java.io.Serializable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Game implements Serializable {
    private TERenderer ter = new TERenderer();
    private static final int width = 80;
    private static final int height = 33;
    private MapGenerator mapGenerator;
    private TETile[][] currMap;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private boolean readyToSave = false;
    private String userInput;
    private String moves = "";
    private String numberSeed = "";


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */


    private void showStartScreen() {

        StdDraw.setCanvasSize(width / 2 * 16, height * 16);
        Font largeFont = new Font("Monaco", Font.BOLD, 40);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(largeFont);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


        StdDraw.setPenColor(Color.white);
        StdDraw.text(width / 2, height / 1.5, "PacKnight");
        StdDraw.setFont(smallFont);
        StdDraw.text(width / 2, (height / 2), "New Game (N)");
        StdDraw.text(width / 2, (height / 2) - 2, "Reinforcement Learning Mode (R)");
        StdDraw.text(width / 2, (height / 2) - 4, "Load Game (L)");
        StdDraw.text(width / 2, (height / 2) - 6, "Quit Game (Q)");
        StdDraw.show();

        userInput = "";


        while (!gameStarted) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char key = StdDraw.nextKeyTyped();
            userInput += String.valueOf(key);

            if (key == 'N' || key == 'n') {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(width / 2, height / 2, "SEED:");
                StdDraw.show();

            }

            if (Character.isDigit(key)) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(width / 2, height / 2, "SEED:");
                StdDraw.text(width / 2, (height / 2) - 2, userInput.substring(1));
                StdDraw.text(width / 2, (height / 2) - 10, "Press 'S' to Start");
                StdDraw.show();
                StdDraw.show();

            } else if (key == 'S' || key == 's') {
                gameStarted = true;
                mapGenerator = new MapGenerator(userInput);
                ter.initialize(width, height + 3);
                currMap = mapGenerator.returnMap();
                ter.renderFrame(currMap);

            } else if (key == 'R' || key == 'r'){
                StdDraw.clear(Color.BLACK);
                StdDraw.text(width / 2, height / 2, "Not Implemented Yet");
                StdDraw.show();


            } else if (key == 'L' || key == 'l') {
                Game reloaded = loadWorld();
                this.ter = reloaded.ter;
                ter.initialize(width, height + 3);
                this.mapGenerator = reloaded.mapGenerator;
                this.currMap = this.mapGenerator.returnMap();
                this.gameOver = false;
                this.readyToSave = false;
                this.gameStarted = true;

            }
        }
    }


    void playWithKeyboard() {
        while (!gameStarted) {
            showStartScreen();
        }


        while (!gameOver) {

            if (mapGenerator.playerHealth <= 0){
                gameOver = true;
                break;
            }

            ter.renderFrame(currMap);
            headsUpDisplay();
            StdDraw.clear(Color.black);


            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char key = StdDraw.nextKeyTyped();
            userInput += String.valueOf(key);

            switch (key) {
                case 'D':
                case 'd':
                case 'W':
                case 'w':
                case 'A':
                case 'a':
                case 'S':
                case 's':
                    updateMap(key);
                    break;

                case ':':
                    readyToSave = true;
                    break;

                case 'Q':
                case 'q':
                    gameOver = true;
                    saveWorld(this);
                    System.exit(0);
                    break;
            }

        }

        StdDraw.setPenColor(Color.white);
        Font largeFont = new Font("Monaco", Font.BOLD, 100);
        StdDraw.text(width / 2, height / 2, "GAME OVER");
        StdDraw.setFont(largeFont);
        StdDraw.show();

        try
        {
            Thread.sleep(1000);
            System.exit(0);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

    }

    private void updateMap(char key){
        mapGenerator.playerMove(key);
        currMap = mapGenerator.returnMap();
        ter.renderFrame(currMap);
    }

    private void headsUpDisplay() {
        String message = tileMessage();
        Font largeFont = new Font("Monaco", Font.BOLD, 24);
        StdDraw.setFont(largeFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(8, height + 1, message);

        Font smallFont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(smallFont);
        StdDraw.text(8, height - 1, "Health: " + mapGenerator.playerHealth);
        StdDraw.text(8, height - 2, "Score: " + mapGenerator.playerScore);
        StdDraw.show();


    }


    private String tileMessage() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        if (mouseInBounds(mouseX, mouseY)) {
            TETile tileHoveringOver = currMap[mouseX][mouseY]; //
            if (tileHoveringOver.equals(Tileset.FLOOR)) {
                return "Floor";
            } else if (tileHoveringOver.equals(Tileset.PLAYER)) {
                return "You";
            } else if (tileHoveringOver.equals(Tileset.RADIOACTIVE)) {
                return "Radioactive!";
            } else if (tileHoveringOver.equals(Tileset.WALL)) {
                return "Wall";
            } else if (tileHoveringOver.equals(Tileset.HEART)) {
                return "Heart";
            } else {
                return "Nothing";
            }
        }

        return "Nothing";

    }


    private boolean mouseInBounds(int x, int y) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);

    }

    private static void saveWorld(Game g) {
        File f = new File("./SavedGame.txt");

        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(g);
            os.reset();
            os.close();
            fs.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
            System.exit(0);

        } catch (IOException e) {
            System.out.println("Unable to Save");
            System.exit(0);
        }
    }


    private static Game loadWorld() {
        File f = new File("./SavedGame.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);

                Game oldgame = (Game) os.readObject();
                os.close();
                fs.close();
                return oldgame;

            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);

            } catch (IOException e) {
                System.out.println("Unable to Load");
                System.exit(0);

            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String separatedSeed = parseInput(input);
        char[] arrayedmoves = moves.toCharArray();

        int i = 0;

        if (arrayedmoves[0] == 'L' || arrayedmoves[0] == 'l') {
            i++;
            char[] newarray = new char[arrayedmoves.length - 1];
            System.arraycopy(arrayedmoves, 1, newarray, 0, arrayedmoves.length - 1);
            String inputnew = new String(newarray);


            Game reloaded = loadWorld();
            mapGenerator = reloaded.mapGenerator;
            TETile[][] tempFrame = reloaded.currMap;
            currMap = tempFrame;
            reloaded.playWithInputString(inputnew);

        } else if (arrayedmoves[i] == 's' || arrayedmoves[i] == 'S') {
            i++;
            mapGenerator = new MapGenerator(separatedSeed);
            currMap = mapGenerator.returnMap();
        }

        while (i < arrayedmoves.length) {
            if (arrayedmoves[i] == ':') {
                readyToSave = true;
            } else if (readyToSave) {
                if (arrayedmoves[i] == 'Q' || arrayedmoves[i] == 'q') {
                    saveWorld(this);
                }
            } else {
                mapGenerator.playerMove(arrayedmoves[i]);
            }
            i++;
        }
        return currMap;
    }


    private String parseInput(String input) {
        char[] inputarray = input.toCharArray();
        boolean readyForMoves = false;
        char first = inputarray[0];
        if (first != 'n' && first != 'N' && first != 'l' && first != 'L') {
            readyForMoves = true;
        }
        int i = 0;
        moves = "";
        while (i < inputarray.length) {
            if (!readyForMoves) {
                if (inputarray[i] == 'N' || inputarray[i] == 'n') {
                    moves += "";
                } else if (inputarray[i] == 'L' || inputarray[i] == 'l') {
                    readyForMoves = true;
                    moves += inputarray[i];
                } else if (Character.isDigit(inputarray[i])) {
                    numberSeed += inputarray[i];
                } else if (inputarray[i] == 'S' || inputarray[i] == 's') {
                    moves += inputarray[i];
                    readyForMoves = true;
                }
            } else {
                moves += inputarray[i];
            }
            i++;
        }
        return numberSeed;
    }
}
