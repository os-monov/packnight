package byog.Core;

import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class MapGenerator {
    private static Integer[] world_parameters;
    private static final int HEIGHT = 30;
    private static final int WIDTH = 80;
//    private static Integer[][][] room_coordinates;
    static TETile[][] TETile_world = new TETile[WIDTH][HEIGHT];
    private static long SEED;
    public static Random r = new Random(SEED);

    private static Integer[] RandomWorldParameters(){
        int num_rooms = RandomUtils.uniform(r, 10);
        int num_hallways = RandomUtils.uniform(r, 15);
        Integer[] params = new Integer[]{num_rooms, num_hallways};
//        room_coordinates = new Integer[num_rooms][4][2];
        return params;
    }

    public MapGenerator(String input){
        SEED = numerical_seed(input);
    }


    private static void fillTileBackground(TETile[][] tile_array) {
        for (int i = 0; i < tile_array.length; i += 1) {
            for (int j = 0; j < tile_array[0].length; j += 1) {
                tile_array[i][j] = Tileset.FLOWER;
            }
        }
    }

    private static void addHallways(){
        int length = RandomUtils.uniform(r, 10);
        int x_start = RandomUtils.uniform(r,0, 79);
        int y_start = RandomUtils.uniform(r,0, 29);
        Hallway.drawHorizontalHallwayRight(x_start, y_start, length);

//        for (int i = 1; i < world_parameters[1]; i+= 1){
        int len = RandomUtils.uniform(r, 10);
        Integer[] next_xy = Hallway.lastXYpos();
        Hallway.drawVerticalHallwayDown(next_xy[0], next_xy[1], len);

//            int hallway_type = RandomUtils.uniform(r, 1, 4);
//            int len = RandomUtils.uniform(r, 10);
//            Integer[] next_xy = Hallway.lastXYpos();
//
//            if (hallway_type == 1){
//                Hallway.drawHorizontalHallwayRight(next_xy[0], next_xy[1], len);
//            }
//
//            if (hallway_type == 2){
//                Hallway.drawVerticalHallwayUp(next_xy[0], next_xy[1], len);
//            }
//            if (hallway_type == 3){
//                Hallway.drawHorizontalHallwayLeft(next_xy[0], next_xy[1], len);
//            }
//
//            if (hallway_type == 4){
//                Hallway.drawVerticalHallwayDown(next_xy[0], next_xy[1], len);
//            }
//
//
//        }

    }

    private TETile[][] create_tile_world(Integer[] params){

        fillTileBackground(TETile_world);
        addHallways();
        Hallway.clearTheHalls();
        //int add_rooms;


        return TETile_world;
    }


    public TETile[][] Generate(){
        world_parameters = RandomWorldParameters();
        TETile[][] world = create_tile_world(world_parameters);
        return world;
    }

    public void main (String[] args){

    }

    private long numerical_seed(String input){
        input = input.replace("N", "");
        input = input.replace("n", "");
        input = input.replace("S", "");
        input = input.replace("n", "");
        Long final_seed = Long.parseLong(input);
        return final_seed;
    }
}
