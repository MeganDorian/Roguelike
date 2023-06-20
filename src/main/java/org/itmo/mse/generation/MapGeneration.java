package org.itmo.mse.generation;

import com.googlecode.lanterna.TextCharacter;
import lombok.experimental.UtilityClass;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.itmo.mse.constants.SpecialCharacters.*;

@UtilityClass
public class MapGeneration extends Generation {
    
    public String fileName = "new_level";
    
    //default value
    private int xRoom = 17;
    private int yRoom = 4;
    private int size = 6;
    
    private int resizeWight = 0;
    private int resizeHeight = 1;
    private int numberMobs = Proportions.numberMobs;
    private int numberItems = Proportions.numberItems;
    
    //actual values
    private int mapWight = 120;
    private int mapHeight = 30;
    private char[][] map;
    
    
    //old values
    private int oldMapWight = 120;
    private int oldMapHeight = 30;
    
    /**
     * Generates map with the set parameters
     *
     * @throws IOException - if unable to write to file
     */
    private void generate() throws IOException {
        //generate map
        generateWall();
        generateObject(numberMobs, MOB);
        generateObject(numberItems, ITEM);
        inputAndOutputGeneration();
        
        //write to file
        writeMapInFile();
        
        //forget map
        map = null;
    }
    
    /**
     * Builds walls on the map
     */
    public void generateWall() {
        if (mapHeight != oldMapHeight || mapWight != oldMapWight) {
            generateRoomParameters();
            oldMapHeight = mapHeight;
            oldMapWight = mapWight;
        }
        generateRooms();
    }
    
    /**
     * Generates same objects (items and mobs) on the map
     *
     * @param number -- the number of objects on the map
     * @param symbol -- symbol of object
     */
    private void generateObject(int number, TextCharacter symbol) {
        int rangeW = mapWight - resizeWight;
        int rangeH = mapHeight - resizeHeight;
        boolean setObject;
        for (int i = 0; i < number; i++) {
            setObject = false;
            while (!setObject) {
                int positionX = rand.nextInt(rangeW);
                int positionY = rand.nextInt(rangeH);
                if (map[positionX][positionY] == SPACE.getCharacterString().charAt(0)) {
                    map[positionX][positionY] = symbol.getCharacterString().charAt(0);
                    setObject = true;
                }
            }
        }
    }
    
    /**
     * Finds the greatest common divisor
     *
     * @param a -- first number
     * @param b -- second number
     * @return -- greatest common divisor
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * Generates parameters for the map according to the set dimensions
     */
    private void generateRoomParameters() {
        int halfWight = mapWight / 2;
        int halfHeight = mapHeight / 2;
        int wight = mapWight;
        do {
            int forChange = mapHeight;
            do {
                int gcdWH = gcd(wight - 1, forChange - 1);
                if (gcdWH > 1) {
                    xRoom = (wight - 1) / gcdWH;
                    yRoom = (forChange - 1) / gcdWH;
                    size = gcdWH - 1;
                    resizeWight = mapWight - wight;
                    resizeHeight = mapHeight - forChange;
                    return;
                } else {
                    forChange--;
                }
            } while (forChange >= halfHeight);
            wight--;
        } while (wight >= halfWight);
        
        //set default or build big, or throw exception?
        xRoom = 17;
        yRoom = 4;
        size = 6;
        resizeWight = 0;
        resizeHeight = 1;
    }
    
    /**
     * Writes the generated map to file
     */
    @SuppressWarnings("all")
    private void writeMapInFile() throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream fileForWrite = FileUtils.getFileForWrite(fileName);
        for (int y = 0; y < map[0].length + resizeHeight; y++) {
            int yForGet = y;
            if (y >= map[0].length - 1) {
                if (y == map[0].length + resizeHeight - 1) {
                    yForGet = map[0].length - 1;
                } else {
                    yForGet = map[0].length - 2;
                }
            }
            for (int x = 0; x < map.length + resizeWight; x++) {
                int xForGet = x;
                if (x >= map.length - 1) {
                    if (x == map.length + resizeWight - 1) {
                        xForGet = map.length - 1;
                    } else {
                        xForGet = map.length - 2;
                    }
                }
                
                //making sure there is no dubbing of items and mobs when sizing up
                if ((y >= map[0].length - 1 || x >= map.length - 1) &&
                    (map[xForGet][yForGet] != SPACE.getCharacterString().charAt(0) &&
                     map[xForGet][yForGet] != WALL.getCharacterString().charAt(0))) {
                    fileForWrite.write(SPACE.getCharacterString().charAt(0));
                } else {
                    fileForWrite.write(map[xForGet][yForGet]);
                }
            }
            
            fileForWrite.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fileForWrite.close();
    }
    
    /**
     * Generates y to randomly position the entrance and exit to the level, taking into account
     * the fit of the map to the required dimensions
     *
     * @return y
     */
    private int getY() {
        int y = rand.nextInt(mapHeight);
        if (y >= map[0].length - 1) {
            if (y == map[0].length + resizeHeight - 1) {
                y = map[0].length - 1;
            } else {
                y = map[0].length - 2;
            }
        }
        return y;
    }
    
    /**
     * Generates a random input and output on the map
     */
    private void inputAndOutputGeneration() {
        int yIn;
        int yOut;
        do {
            yIn = getY();
            if (map[1][yIn] == WALL.getCharacterString().charAt(0)) {
                yIn = 0;
            }
        } while (yIn == 0);
        map[0][yIn] = SPACE.getCharacterString().charAt(0);
        map[1][yIn] = PLAYER.getCharacterString().charAt(0);
        do {
            yOut = getY();
            if (map[mapWight - resizeWight - 2][yOut] == WALL.getCharacterString().charAt(0)) {
                yOut = 0;
            }
        } while (yOut == 0);
        map[mapWight - resizeWight - 1][yOut] = SPACE.getCharacterString().charAt(0);
    }
    
    /**
     * Generate rooms and delete random walls <br>
     * Guaranteed that there will be a passage from the entrance to the level to the exit
     */
    private void generateRooms() {
        boolean[][] connected = new boolean[xRoom][yRoom];
        Direction[][] neighbor = new Direction[xRoom][yRoom];
        for (int x = 0; x < neighbor.length; x++) {
            for (int y = 0; y < neighbor[x].length; y++) {
                neighbor[x][y] = Direction.EMPTY;
                connected[x][y] = false;
            }
        }
        // pick a random room to start
        final int startX = rand.nextInt(xRoom);
        final int startY = rand.nextInt(yRoom);
        connected[startX][startY] = true;
        neighbor[startX][startY] = Direction.START;
        
        // Build a list of rooms remaining to be connected.
        final List<Integer> remainingRooms = new ArrayList<>();
        for (int i = 0; i < xRoom * yRoom; i++) {
            remainingRooms.add(i);
        }
        
        while (!remainingRooms.isEmpty()) {
            // pick a random unconnected room
            final int roomIndexInList = rand.nextInt(remainingRooms.size());
            final int roomIndex = remainingRooms.get(roomIndexInList);
            final int[] room = new int[]{roomIndex % xRoom, roomIndex / xRoom};
            if (connected[room[0]][room[1]]) {
                remainingRooms.remove(roomIndexInList);
            } else {
                // Look for neighbors in random order
                final Direction[] lookOrder = Direction.values();
                Collections.shuffle(Arrays.asList(lookOrder));
                
                for (Direction i : lookOrder) {
                    
                    switch (i) {
                        case UP:
                            if (room[1] != 0 && connected[room[0]][room[1] - 1]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = Direction.UP;
                            }
                            break;
                        case LEFT:
                            if (room[0] != 0 && connected[room[0] - 1][room[1]]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = Direction.LEFT;
                            }
                            break;
                        case RIGHT:
                            if (room[0] != xRoom - 1 && connected[room[0] + 1][room[1]]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = Direction.RIGHT;
                            }
                            break;
                        case DOWN:
                            if (room[1] != yRoom - 1 && connected[room[0]][room[1] + 1]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = Direction.DOWN;
                            }
                        default:
                    }
                    
                }
                
            }
        }
        map = new char[xRoom * size + xRoom + 1][yRoom * size + yRoom + 1];
        // Build all ground
        for (char[] chars : map) {
            Arrays.fill(chars, SPACE.getCharacterString().charAt(0));
        }
        // Build wall grid
        for (int x = 0; x < map.length; x += size + 1) {
            Arrays.fill(map[x], WALL.getCharacterString().charAt(0));
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y += size + 1) {
                map[x][y] = WALL.getCharacterString().charAt(0);
            }
        }
        // Drill the connecting holes
        int dx;
        int dy;
        for (int x = 0; x < neighbor.length; x++) {
            for (int y = 0; y < neighbor[x].length; y++) {
                switch (neighbor[x][y]) {
                    case UP:
                        dx = x * (size + 1);
                        dy = y * (size + 1);
                        for (int i = 1; i < (size + 1); i++) {
                            map[dx + i][dy] = SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case LEFT:
                        dx = x * (size + 1);
                        dy = y * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx][dy + i] = SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case RIGHT:
                        dx = (x + 1) * (size + 1);
                        dy = (y) * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx][dy + i] = SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case DOWN:
                        dx = (x) * (size + 1);
                        dy = (y + 1) * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx + i][dy] = SPACE.getCharacterString().charAt(0);
                        }
                    default:
                }
            }
        }
    }
    
    public MapGenerationBuilder builder() {
        return new MapGenerationBuilder();
    }
    
    public class MapGenerationBuilder {
        
        /**
         * Sets the default map parameters
         *
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder walls() {
            xRoom = 17;
            yRoom = 4;
            size = 6;
            resizeWight = 0;
            resizeHeight = 1;
            return this;
        }
        
        /**
         * Sets new dimensions for map generation
         *
         * @param wight  - map width to generate
         * @param height - map height to generate
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder walls(int wight, int height) {
            mapWight = wight;
            mapHeight = height;
            return this;
        }
        
        /**
         * Sets the default number of mobs
         *
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder mobs() {
            numberMobs = Proportions.numberMobs;
            return this;
        }
        
        /**
         * Sets the number of mobs
         *
         * @param num - number of mobs
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder mobs(int num) {
            numberMobs = num;
            return this;
        }
        
        /**
         * Sets the default number of items
         *
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder items() {
            numberItems = Proportions.numberItems;
            return this;
        }
        
        /**
         * Sets the number of items
         *
         * @param num - number of items
         * @return MapGenerationBuilder for further construction
         */
        public MapGenerationBuilder items(int num) {
            numberItems = num;
            return this;
        }
        
        /**
         * Builds a map from the given parameters and writes it to a file
         *
         * @throws IOException - if unable to write to file
         */
        public void build() throws IOException {
            generate();
        }
        
    }
}
