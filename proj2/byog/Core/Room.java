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

    public Room(int[] BL) {
        Random r = new Random();
        height = RandomUtils.uniform(r, 6);
        width = RandomUtils.uniform(r, 7);
        BR[0] = BL[0] + width;
        BR[1] = BL[1];
        TL[0] = BL[0];
        TL[1] = BL[1] + height;
        TR[0] = TL[0] + width;
        TR[1] = TL[1];
    }

    public void drawRoom() {
        for (int x = BL[0]; x < BL[0] + width; x++) {
            if (x == 80) {
                break;
            }
            for (int y = BL[1]; y < BL[1] + height; y++) {
                if (y == 30) {
                    break;
                }
                MapGenerator.TETile_world[x][y] = Tileset.WALL;
            }
        }
    }

    public void clearRoom() {
        for (int x = BL[0] + 1; x < BL[0] + width - 1; x++) {
            if (x == 0) {
                x++;
            }
            if (x == 79) {
                break;
            }
            for (int y = BL[1] + 1; y < BL[1] + height - 1; y++) {
                if (y == 0) {
                    y++;
                }
                if (y == 29) {
                    break;
                }
                MapGenerator.TETile_world[x][y] = Tileset.FLOOR;
            }
        }
    }


}