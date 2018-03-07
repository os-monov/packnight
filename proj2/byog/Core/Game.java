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

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private MapGenerator nm;
    private TETile[][] finalWorldFrame;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    boolean readytoSave = false;
    private String SEED;
    private int player_X;
    private int player_Y;
    private int SCORE;
    private int HEALTH;
    private String moves = "";
    private String numberseed = "";


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */


    public void showStartScreen() {
        gameOver = false;
        gameStarted = false;
        readytoSave = false;


        StdDraw.setCanvasSize(WIDTH / 2 * 16, HEIGHT * 16);
        Font largeFont = new Font("Monaco", Font.BOLD, 40);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(largeFont);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 1.5, "CS61B: THE GAME");
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2), "New Game (N)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 4, "Quit Game (Q)");
        StdDraw.show();


        SEED = "";

        while (!gameStarted) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char key = StdDraw.nextKeyTyped();
            SEED += String.valueOf(key);

            if (key == 'N' || key == 'n') {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "SEED:");
                StdDraw.show();

            }

            if (Character.isDigit(key)) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "SEED:");
                StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 2, SEED.substring(1, SEED.length()));
                StdDraw.show();

            } else if (key == 'S' || key == 's') {
                gameStarted = true;
                nm = new MapGenerator(SEED.substring(1, SEED.length() - 1));
                finalWorldFrame = nm.generate();
                ter.initialize(WIDTH, HEIGHT + 3);
                ter.renderFrame(finalWorldFrame);

            } else if (key == 'L' || key == 'l') {
                Game reloaded = loadWorld();
                this.nm.SCORE = reloaded.SCORE;
                this.nm.HEALTH = reloaded.HEALTH;
                ter.initialize(WIDTH, HEIGHT + 3);
                ter.renderFrame(reloaded.finalWorldFrame);
                reloaded.gameOver = false;
                reloaded.readytoSave = false;
                this.player_X = reloaded.nm.PLAYER_X;
                this.player_Y = reloaded.nm.PLAYER_Y;
                reloaded.playWithKeyboard();

            }
        }
    }


    public void playWithKeyboard() {

        while (!gameStarted) {
            showStartScreen();
        }

        while (!gameOver) {

            SCORE = nm.SCORE;
            HEALTH = nm.HEALTH;

            ter.renderFrame(finalWorldFrame);
            headsUpDisplay();
            StdDraw.clear(Color.black);


            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char key = StdDraw.nextKeyTyped();
            SEED += String.valueOf(key);


            if (key == 'D' || key == 'd') {
                nm.playerMove(key, finalWorldFrame);
                ter.renderFrame(finalWorldFrame);

            } else if (key == 'W' || key == 'w') {
                nm.playerMove(key, finalWorldFrame);
                ter.renderFrame(finalWorldFrame);


            } else if (key == 'A' || key == 'a') {
                nm.playerMove(key, finalWorldFrame);
                ter.renderFrame(finalWorldFrame);


            } else if (key == 's' || key == 'S') {
                nm.playerMove(key, finalWorldFrame);
                ter.renderFrame(finalWorldFrame);


            } else if (key == ':') {
                readytoSave = true;

            } else if (readytoSave) {
                if (key == 'q' || key == 'Q') {
                    gameOver = true;
                    this.player_X = nm.PLAYER_X;
                    this.player_Y = nm.PLAYER_Y;
                    finalWorldFrame = nm.TETILE_WORLD;
                    saveWorld(this);
                    System.exit(0);
                }
            }
        }
    }


    public void headsUpDisplay() {
        String message = tileMessage();
        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(5, HEIGHT + 2, message);
        if (HEALTH <= 0) {
            StdDraw.text(5, HEIGHT + 1, "Game Over");
        } else {
            StdDraw.text(5, HEIGHT + 1, "Health: " + String.valueOf(HEALTH));
        }
        StdDraw.text(5, HEIGHT, "Score: " + String.valueOf(SCORE));
        StdDraw.show();


    }


    public String tileMessage() {
        int mousex = (int) StdDraw.mouseX(); //StdDraw.mouseX();
        int mousey = (int) StdDraw.mouseY();

        if (mouseInBounds(mousex, mousey)) {
            TETile twm = finalWorldFrame[mousex][mousey];
            if (twm.equals(Tileset.FLOOR)) {
                return "Floor";
            } else if (twm.equals(Tileset.PLAYER)) {
                return "You";
            } else if (twm.equals(Tileset.SPIKED_WALL)) {
                return "Spiked Wall";
            } else if (twm.equals(Tileset.WALL)) {
                return "Wall";
            } else if (twm.equals(Tileset.NOTHING)) {
                return "Nothing";
            } else if (twm.equals(Tileset.FLOWER)) {
                return "Heart";
            } else if (twm.equals(Tileset.NOTHING)) {
                return "Nothing";
            } else {
                return "";
            }
        }

        return "";

    }


    private boolean mouseInBounds(int x, int y) {
        return (x > 0) && (x < WIDTH) && (y > 0) && (y < HEIGHT);

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
            System.out.println("file not found");
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
    public TETile[][] playWithInputString(String input) {
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
            nm = reloaded.nm;
            TETile[][] tempFrame = reloaded.finalWorldFrame;
            finalWorldFrame = tempFrame;
//            nm = reloaded.nm;
//            this.nm.SCORE = reloaded.SCORE;
//            this.nm.HEALTH = reloaded.HEALTH;
//            reloaded.gameOver = false;
//            reloaded.readytoSave = false;
//            this.nm.PLAYER_X = reloaded.nm.PLAYER_X;
//            this.nm.PLAYER_Y = reloaded.nm.PLAYER_Y;
            reloaded.playWithInputString(inputnew);

        } else if (arrayedmoves[i] == 's' || arrayedmoves[i] == 'S') {
            i++;
            nm = new MapGenerator(separatedSeed);
            finalWorldFrame = nm.generate();
        }

        while (i < arrayedmoves.length) {
            if (arrayedmoves[i] == ':') {
                readytoSave = true;
            } else if (readytoSave) {
                if (arrayedmoves[i] == 'Q' || arrayedmoves[i] == 'q') {
                    this.player_X = nm.PLAYER_X;
                    this.player_Y = nm.PLAYER_Y;
                    saveWorld(this);
                }
            } else {
                nm.playerMove(arrayedmoves[i], finalWorldFrame);
            }
            i++;
        }
        return finalWorldFrame;
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
                    numberseed += inputarray[i];
                } else if (inputarray[i] == 'S' || inputarray[i] == 's') {
                    moves += inputarray[i];
                    readyForMoves = true;
                }
            } else {
                moves += inputarray[i];
            }
            i++;
        }
        return numberseed;
    }
}
