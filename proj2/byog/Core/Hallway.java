package byog.Core;
import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class Hallway {
    static Integer[][] blocks_to_delete = new Integer[1000][2]; // 2D array of the x,y coordinates of blocks we want to replace with floor
    static int remove_size = 0; // size of 2D array


    public Hallway(){
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawHorizontalHallwayRight(int x, int y, int len){
        for (int i = x; i < (x + len); i += 1){

            if (i == 79) {
                break;
            }

            else{
                MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y] = Tileset.WALL;
                blocks_to_delete[remove_size] = new Integer[]{i, y};
                remove_size += 1;
                }

            }
    }


    ///////////////////////////////////////////////////////////////////////////

    public static void drawHorizontalHallwayLeft(int x, int y, int len){
        for (int i = x; i > (x - len); i -= 1){
            if (i == 0){
                break;
            }

            else {
                MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y] = Tileset.WALL;
                blocks_to_delete[remove_size] = new Integer[]{i, y};
                remove_size += 1;

            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawVerticalHallwayUp(int x, int y, int len){
        for (int i = y; i < (y + len); i += 1){
            if (i == 29){
                break;
            }

            else {
                MapGenerator.TETile_world[i][x + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][x - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][x] = Tileset.WALL;
                blocks_to_delete[remove_size] = new Integer[]{i, x};
                remove_size += 1;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawVerticalHallwayDown(int x, int y, int len){
        for (int i = y; i > (y - len); i -= 1){

            if (i == 0) {
                break;
            }
            else {

                MapGenerator.TETile_world[i][x + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][x - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][x] = Tileset.WALL;
                blocks_to_delete[remove_size] = new Integer[]{i, x};
                remove_size += 1;
            }

        }
    }

    public static Integer[] lastXYpos(){
        return blocks_to_delete[remove_size];
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void clearTheHalls(){
        blocks_to_delete[0] = null;
        blocks_to_delete[remove_size-1] = null;
        remove_size -= 2;

        for (int i = 1; i < (remove_size +1); i += 1){
            int x_cor_to_rep = blocks_to_delete[i][0];
            int y_cor_to_rep = blocks_to_delete[i][1];

            MapGenerator.TETile_world[x_cor_to_rep][y_cor_to_rep] = Tileset.NOTHING;
        }

        System.out.print(Hallway.remove_size);
    }

}
