package byog.Core;

import java.util.Random;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

public class MapGenerator {
    private static Integer[] world_parameters;
    private static final int HEIGHT = 80;
    private static final int WIDTH = 30;
    private static Integer[][] room_coordinates;

    private static Integer[] RandomWorldParameters(){
        Random r = new Random();
        int num_rooms = RandomUtils.uniform(r, 10);
        int num_hallways = RandomUtils.uniform(r, 15);
        Integer[] params = new Integer[]{num_rooms, num_hallways};
        room_coordinates = new Integer[num_rooms][4];
        return params;
    }


    public class Position {
        private int x_pos;
        private int y_pos;

        public Position(int x, int y){
            x_pos = x;
            y_pos = y;
        }

        public Integer[] returnPosition(){
            return new Integer[]{x_pos, y_pos};
        }
    }


    public static TETile[][] create_tile_world(Integer[] params){
        TETile[][] TETile_world = new TETile[WIDTH][HEIGHT];
        fillTileBackground(TETile_world);
        //int add_rooms;
        //int add_hallways;


        return TETile_world;
    }

    public static void fillTileBackground(TETile[][] tile_array) {
        for (int i = 0; i < tile_array.length; i += 1){
            for (int j = 0; j < tile_array[0].length; j += 1){
                tile_array[i][j] = Tileset.NOTHING;
            }
        }
    }

//    public void drawLTiles(int x_start, int y_start, int len, int direction, TETile t){
//        if direction
//
//        for (int i = x_start; i < x_final; i++){
//            for(int j = y_start; i < y_final)
//        }




    public void addRooms(TETile[][] tile_array, int n_rooms) {
        for (int i = 0; i < world_parameters[0]; i += 1){

        }
    }


    public static TETile[][] MapGenerator(){
        world_parameters = RandomWorldParameters();
        TETile[][] world = create_tile_world(world_parameters);
        return world;
    }

    public void main (String[] args){

    }
}
