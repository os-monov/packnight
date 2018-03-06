
package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;
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
    boolean readytoSave;
    private String SEED;
    private int SCORE;
    private int HEALTH;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */


    public void showStartScreen() {
        gameOver = false;
        gameStarted = false;
        readytoSave = false;

        StdDraw.setCanvasSize(WIDTH / 2 * 16, HEIGHT * 16);
        Font large_font = new Font("Monaco", Font.BOLD, 40);
        Font small_font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(large_font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 1.5, "CS61B: THE GAME");
        StdDraw.setFont(small_font);
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
                this.nm = reloaded.nm;
                this.finalWorldFrame = reloaded.finalWorldFrame;
                this.gameOver = false;
                this.gameStarted = true;
                this.readytoSave = false;
                this.SEED = reloaded.SEED;
                ter.initialize(WIDTH, HEIGHT + 3);
                ter.renderFrame(reloaded.finalWorldFrame);

//                this.nm.ft = reloaded.nm.ft;
                this.playWithKeyboard();

//
//
//
//                this.ter = reloaded.ter;


//                this.playWithKeyboard();



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
            HeadsUpDisplay();
            StdDraw.clear(Color.black);


            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char key = StdDraw.nextKeyTyped();
            SEED += String.valueOf(key);


            if (key == 'D' || key == 'd') {
                nm.playerMove(key);
                ter.renderFrame(finalWorldFrame);

            } else if (key == 'W' || key == 'w') {
                nm.playerMove(key);
                ter.renderFrame(finalWorldFrame);


            } else if (key == 'A' || key == 'a') {
                nm.playerMove(key);
                ter.renderFrame(finalWorldFrame);



            } else if (key == 's' || key == 'S') {
                nm.playerMove(key);
                ter.renderFrame(finalWorldFrame);


            } else if (key == ':') {
                readytoSave = true;

            } else if (readytoSave) {
                if (key == 'q' || key == 'Q') {
                    gameOver = false;
                    saveWorld(this);
                    System.exit(0);
                }
            }
        }
    }


    public void HeadsUpDisplay() {
        String message = tileMessage();
        Font small_font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(small_font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(5, HEIGHT + 2, message);
        StdDraw.text(5, HEIGHT + 1, String.valueOf(HEALTH));
        StdDraw.text(5, HEIGHT , String.valueOf(SCORE));
        StdDraw.show();



    }

    public String tileMessage() {
        int mousex = (int) StdDraw.mouseX(); //StdDraw.mouseX();
        int mousey = (int) StdDraw.mouseY();

        if (mouseInBounds(mousex, mousey)){
            TETile twm = finalWorldFrame[mousex][mousey];
            if (twm.equals(Tileset.FLOOR)) {
                return "Floor";
            } else if (twm.equals(Tileset.PLAYER)) {
                return "You";
            } else if (twm.equals(Tileset.WALL)) {
                return "Wall";
            } else if (twm.equals(Tileset.NOTHING)) {
                return "Nothing";
            } else {
                return "";
            }
        }

        return "";

    }


    private boolean mouseInBounds(int x, int y){
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

        MapGenerator nm = new MapGenerator(input);
        TETile[][] finalWorldFrame = nm.generate();
        ter.initialize(WIDTH, HEIGHT + 3);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
}