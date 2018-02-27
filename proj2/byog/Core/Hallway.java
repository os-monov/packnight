package byog.Core;
import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class Hallway {
    static Integer[][] blocks_to_delete = new Integer[1000][2]; // 2D array of the x,y coordinates of blocks we want to replace with floor
    static int array_size = 0; // size of 2D array
    static int last_index = 0;


    public Hallway(){
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawHorizontalHallwayRight(int x, int y, int len){
        for (int i = x; i < (x + len); i += 1){

            if (i == 78) {
                break;
            }

            else{
                MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y] = Tileset.WALL;
                blocks_to_delete[array_size] = new Integer[]{i, y};
                array_size += 1;
                last_index += 1;

            }

        }
    }


    ///////////////////////////////////////////////////////////////////////////

    public static void drawHorizontalHallwayLeft(int x, int y, int len){
        for (int i = x; i > (x - len); i -= 1){
            if (i == 1){
                break;
            }

            else {
                MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
                MapGenerator.TETile_world[i][y] = Tileset.WALL;
                blocks_to_delete[array_size] = new Integer[]{i, y};
                array_size += 1;
                last_index += 1;

            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawVerticalHallwayUp(int x, int y, int len){
        for (int i = y; i < (y + len); i += 1){
            if (i == 28){
                break;
            }

            else {
                MapGenerator.TETile_world[x + 1][i] = Tileset.WALL;
                MapGenerator.TETile_world[x - 1][i] = Tileset.WALL;
                MapGenerator.TETile_world[x][i] = Tileset.WALL;
                blocks_to_delete[array_size] = new Integer[]{x, i};
                array_size += 1;
                last_index += 1;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void drawVerticalHallwayDown(int x, int y, int len){
        for (int i = y; i > (y - len); i -= 1){

            if (i == 1) {
                break;
            }
            else {

                MapGenerator.TETile_world[x + 1][i] = Tileset.WALL;
                MapGenerator.TETile_world[x - 1][i] = Tileset.WALL;
                MapGenerator.TETile_world[x][i] = Tileset.WALL;
                blocks_to_delete[array_size] = new Integer[]{x, i};
                array_size += 1;
                last_index += 1;
            }

        }
    }

    public static Integer[] lastXYpos(){
        return blocks_to_delete[array_size -1];
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void clearTheHalls(){
//        System.out.println(blocks_to_delete[array_size -1][0]);
//        System.out.println(blocks_to_delete[array_size -1][1]);
        blocks_to_delete[0] = null;
        blocks_to_delete[array_size -1] = null;
        array_size -= 2;
        last_index -= 1;

        for (int i = 1; i < (array_size +1); i += 1){
            int x_cor_to_rep = blocks_to_delete[i][0];
            int y_cor_to_rep = blocks_to_delete[i][1];

            MapGenerator.TETile_world[x_cor_to_rep][y_cor_to_rep] = Tileset.NOTHING;
        }



    }

}
