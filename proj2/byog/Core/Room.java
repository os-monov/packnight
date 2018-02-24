package byog.Core;
import java.util.Random;
import byog.TileEngine.Tileset;

public class Room {
    int height;
    int width;
    int[] BL;
    int[] BR;
    int[] TL;
    int[] TR;
    int[] center;

    public Room(int[] center) {
        this.center = center;
        Random r = new Random();
        height = RandomUtils.uniform(r, 6);
        width = RandomUtils.uniform(r, 7);
        BL[0] = RandomUtils.uniform(r, 0, 79 - width);
        BL[1] = RandomUtils.uniform(r, 0, 29 - height);
        BR[0] = BL[0] + width;
        BR[1] = BL[1];
        TL[0] = BL[0];
        TL[1] = BL[1] + height;
        TR[0] = TL[0] + width;
        TR[1] = TL[1];
        center[0] = (BL[0] + TL[0]) / 2;
        center[1] = (BL[1] + TL[1]) / 2;
    }

    public void drawRoom(int[] BL, int height, int width) {
        for (int x = BL[0]; x < BL[0] + width; x++) {
            for (int y = BL[1]; y < BL[1] + height; y++){
                MapGenerator.TETile_world[x][y] = Tileset.WALL;
            }
        }
    }


}