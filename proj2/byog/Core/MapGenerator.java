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
    Room[] roomArray = new Room[100];

    private static Integer[] RandomWorldParameters(){
        int num_rooms = RandomUtils.uniform(r, 10);
        int num_hallways = RandomUtils.uniform(r, 100);
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
        int length = RandomUtils.uniform(r, 15,28);
        int x_start = RandomUtils.uniform(r,3, 6);
        int y_start = RandomUtils.uniform(r,5, 10);
        Hallway.drawHorizontalHallwayRight(x_start, y_start, 3*length);
        int big_counter = 0;
        System.out.println(world_parameters[1]);
        System.out.println(length);


        for (int i = 1; i < (world_parameters[1]); i+= 1) {
            if (big_counter < 20) {

                Integer[] next_xy = Hallway.lastXYpos();
                int len = RandomUtils.uniform(r, 25);
                int counter = RandomUtils.uniform(r, 1, 3);

                if (counter == 1) {
                    Hallway.drawHorizontalHallwayRight(next_xy[0], next_xy[1], 3 * len);
                }

                if (counter == 2) {
                    Hallway.drawVerticalHallwayUp(next_xy[0], next_xy[1], 2 * len);
                }
                if (counter == 3) {
                    Hallway.drawHorizontalHallwayLeft(next_xy[0], next_xy[1], 3 * len);
                }

                if (counter == 4) {
                    Hallway.drawVerticalHallwayDown(next_xy[0], next_xy[1], 2 * len);
                }
                big_counter += 1;
            }

            else {
                int new_start = RandomUtils.uniform(r, Hallway.array_size);
                Integer[] next_xy = Hallway.blocks_to_delete[new_start];
                int len = RandomUtils.uniform(r, 25);
                int counter = RandomUtils.uniform(r, 1, 3);

                if (counter == 1) {
                    Hallway.drawHorizontalHallwayRight(next_xy[0], next_xy[1], 3 * len);
//                    Hallway.blocks_to_delete[Hallway.array_size -3*len] = null;
//                    Hallway.blocks_to_delete[Hallway.array_size -1 ] = null;
                }

                if (counter == 2) {
                    Hallway.drawVerticalHallwayUp(next_xy[0], next_xy[1], 2 * len);
                }
                if (counter == 3) {
                    Hallway.drawHorizontalHallwayLeft(next_xy[0], next_xy[1], 3 * len);
                }

                if (counter == 4) {
                    Hallway.drawVerticalHallwayDown(next_xy[0], next_xy[1], 2 * len);
                }
            }

        }

    }


    private TETile[][] create_tile_world(Integer[] params){

        fillTileBackground(TETile_world);
        addHallways();
        addRooms();
        Hallway.clearTheHalls();
        clearRooms();


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

    public void addRooms() {
        for (int i = 0; i < world_parameters[0]; i += 1){
//            int room_start = RandomUtils.uniform(r, Hallway.array_size);
//            int[] r_coordin = new int[]{Hallway.blocks_to_delete[room_start][0], Hallway.blocks_to_delete[room_start][1]};
            Room newRoom = new Room(new int[]{10, 15});
            newRoom.drawRoom();
            roomArray[i] = newRoom;
        }
    }

    public void clearRooms() {
        for (int i = 0; i < world_parameters[0]; i += 1) {
            roomArray[i].clearRoom();
        }
    }


}
