package byog.Core;
import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class Hallway {
    Integer[][] blocks_to_delete = new Integer[1000][2]; // 2D array of the x,y coordinates of blocks we want to replace with floor
    int helper_index_array = 0; // index of 2D array so we can add to back
    int remove_size = 0; // size of 2D array


    public Hallway(){
    }

    ///////////////////////////////////////////////////////////////////////////

    public void drawHorizontalHallwayRight(int x, int y, int len){
        for (int i = x; i < (x + len); i += 1){
            MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y] = Tileset.WALL;
            blocks_to_delete[helper_index_array] = new Integer[]{i, y};
            helper_index_array += 1;
            remove_size += 1;

            if (i == 79){
                return;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public void drawHorizontalHallwayLeft(int x, int y, int len){
        for (int i = x; i > (x - len); i -= 1){
            MapGenerator.TETile_world[i][y + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y] = Tileset.WALL;
            blocks_to_delete[helper_index_array] = new Integer[]{i, y};
            helper_index_array += 1;
            remove_size += 1;

            if (i == 0){
                return;
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public void drawVerticalHallwayUp(int x, int y, int len){
        for (int i = y; i < (y + len); i += 1){
            MapGenerator.TETile_world[i][x + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x] = Tileset.WALL;
            blocks_to_delete[helper_index_array] = new Integer[]{i, x};
            helper_index_array += 1;
            remove_size += 1;

            if (i == 29){
                return;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public void drawVerticalHallwayDown(int x, int y, int len){
        for (int i = y; i > (y - len); i -= 1){
            MapGenerator.TETile_world[i][x + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x] = Tileset.WALL;
            blocks_to_delete[helper_index_array] = new Integer[]{i, x};
            helper_index_array += 1;
            remove_size += 1;

            if (i == 0) {
                return;
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////

    public void cleartheHalls(){
        blocks_to_delete[0] = null;
        blocks_to_delete[remove_size] = null;
        remove_size -= 2;

        for (int i = 1; i < remove_size; i += 1){
            int x_cor_to_rep = blocks_to_delete[i][0];
            int y_cor_to_rep = blocks_to_delete[i][1];

            MapGenerator.TETile_world[x_cor_to_rep][y_cor_to_rep] = Tileset.FLOWER;
        }
    }

}
