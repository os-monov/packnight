package byog.Core;
import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class Hallway {
    int length;
    int x_start;
    int y_start;

    public Hallway(){
        Random r = new Random();
        int length = RandomUtils.uniform(r, 10);
        int x_start = RandomUtils.uniform(r, 79);
        int y_start = RandomUtils.uniform(r, 30);
    }

    public void drawHorizontalHallway(){
        for (int i = x_start; i < (x_start + length); i += 1){
            MapGenerator.TETile_world[i][y_start + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y_start - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][y_start] = Tileset.FLOWER;
        }
    }

    public void drawVerticalHallway(){
        for (int i = y_start; i < (y_start + length); i += 1){
            MapGenerator.TETile_world[i][x_start + 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x_start - 1] = Tileset.WALL;
            MapGenerator.TETile_world[i][x_start] = Tileset.FLOWER;
        }
    }
}
