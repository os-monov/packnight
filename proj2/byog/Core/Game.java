package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.*;
import java.util.Map;



public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean gameOver;
    private boolean gameStarted;
    private MapGenerator nm;
    private TETile[][] finalWorldFrame;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */

    public void playWithKeyboard() {
        gameOver = false;
        gameStarted = false;

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


        String SEED = "";

        while (!gameOver) {
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
            }

            if (Character.isLetter(key)) {

                if (!gameStarted) {
                    if (key == 'S' || key == 's') {
                        gameStarted = true;
                        nm = new MapGenerator(SEED.substring(1, SEED.length() - 1));
                        finalWorldFrame = nm.generate();
                        ter.initialize(WIDTH, HEIGHT + 3);
                        ter.renderFrame(finalWorldFrame);
                    }

                    else if (key == 'L' || key == 'l'){
//                        loadWorld();
                    }
                }

                if (gameStarted) {

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
                    }

                    else if (key == 'Q' || key == 'q'){
//                        saveWorld(this);

                    }
                }
//
            }

        }

    }
//
//    private static void saveWorld(Game g) {
//        File f = new File("./SavedGame.ser");
//        try {
//            if (!f.exists()) {
//                f.createNewFile();
//            }
//            FileOutputStream fs = new FileOutputStream(f);
//            ObjectOutputStream os = new ObjectOutputStream(fs);
//            os.writeObject(g);
//
//        }  catch (FileNotFoundException e) {
//            System.out.println("file not found");
//            System.exit(0);
//
//        } catch (IOException e) {
//            System.out.println(e);
//            System.exit(0);
//        }
//    }


//    private static Game loadWorld() {
//        File f = new File("./SavedGame.ser");
//        if (f.exists()) {
//            try {
//                FileInputStream fs = new FileInputStream(f);
//                ObjectInputStream os = new ObjectInputStream(fs);
//                return (Game) os.readObject();
//
//            } catch (IOException e) {
//                System.out.println(e);
//                System.exit(0);
//
//            } catch (ClassNotFoundException e) {
//                System.out.println("class not found");
//                System.exit(0);
//
//            }
//        }
//
//
//    }



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
        nm.mouseOverTileType();
        return finalWorldFrame;
    }
}
