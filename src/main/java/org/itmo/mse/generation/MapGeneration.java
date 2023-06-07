package org.itmo.mse.generation;

import com.googlecode.lanterna.TextCharacter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.utils.FileUtils;

public class MapGeneration extends Generation {
    
    public static String fileName = "new_level";
    
    private static int xRoom = 17;
    private static int yRoom = 4;
    private static int size = 6;
    
    private static int resizeWight = 0;
    private static int resizeHeight = 0;
    
    public static void generate(int mapWight, int mapHeight, int numberMobs, int numberItems)
        throws IOException {
        generateWall(mapWight, mapHeight);
        generateObject(numberMobs, mapWight, mapHeight, SpecialCharacters.MOB);
        generateObject(numberItems, mapWight, mapHeight, SpecialCharacters.ITEM);
    }
    
    /**
     * Builds walls on the map (generates a new map without mobs and items)
     *
     * @param mapWight
     * @param mapHeight
     * @throws IOException
     */
    public static void generateWall(int mapWight, int mapHeight) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }
        file.createNewFile();
        generateRoomParameters(mapWight, mapHeight);
        char[][] map = generateRooms();
        writeWallInFile(map);
        inputAndOutputGeneration(map, mapWight, mapHeight);
    }
    
    /**
     * Generates same objects (items and mobs) on the map
     * @param number -- the number of objects on the map
     * @param mapWight
     * @param mapHeight
     * @param symbol -- symbol of object
     * @throws IOException
     */
    private static void generateObject(int number, int mapWight, int mapHeight,
                                       TextCharacter symbol)
        throws IOException {
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        int range = mapWight * mapHeight;
        boolean setObject;
        for(int i = 0; i < number; i++) {
            setObject = false;
            while (!setObject) {
                int position = rand.nextInt(range);
                file.seek(position + position / mapWight);
                if(file.read() == SpecialCharacters.SPACE.getCharacterString().charAt(0)) {
                    file.seek(position + position / mapWight);
                    file.write(symbol.getCharacterString().charAt(0));
                    setObject = true;
                }
            }
        }
        file.close();
    }
    
    private static void generateItems(int numberMobs, int mapWight, int mapHeight) {
    
    }
    
    /**
     * Finds greatest common divisor
     * @param a -- first number
     * @param b -- second number
     * @return -- greatest common divisor
     */
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * Generates parameters for the map according to the set dimensions
     * @param mapWight
     * @param mapHeight
     */
    private static void generateRoomParameters(int mapWight, int mapHeight) {
        int halfWight = mapWight / 2;
        int halfHeight = mapHeight / 2;
        int wight = mapWight;
        do {
            int forChange = mapHeight;
            do {
                int gcdWH = gcd(wight - 1, forChange - 1);
                if(gcdWH > 1) {
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
        resizeHeight = 0;
    }
    
    /**
     * Writes the generated map without mobs and items to a file
     *
     * @param map
     * @throws IOException
     */
    private static void writeWallInFile(char[][] map) throws IOException {
        OutputStream file = FileUtils.getFileForWrite(fileName);
        for (int y = 0; y < map[0].length + resizeHeight; y++) {
            int yForGet = y;
            if(y >= map[0].length - 1) {
                if(y == map[0].length + resizeHeight - 1) {
                    yForGet = map[0].length - 1;
                } else {
                    yForGet = map[0].length - 2;
                }
            }
            for (int x = 0; x < map.length + resizeWight; x++) {
                int xForGet = x;
                if(x >= map.length - 1) {
                    if(x == map.length + resizeWight - 1) {
                        xForGet = map.length - 1;
                    } else {
                        xForGet = map.length - 2;
                    }
                }
                file.write(map[xForGet][yForGet]);
            }
            file.write("\n".getBytes(StandardCharsets.UTF_8));
        }
    }
    
    /**
     * Generates a random input and output on the map and writes it to a file
     *
     * @param map -- map as array of characters[x][y], where x is width and y is height
     * @param mapWight
     * @param mapHeight
     * @throws IOException
     */
    private static void inputAndOutputGeneration(char[][] map, int mapWight, int mapHeight)
        throws IOException {
        int yIn;
        int yOut;
        do {
            yIn = rand.nextInt(mapHeight);
            if(yIn >= map[0].length - 1) {
                if(yIn == map[0].length + resizeHeight - 1) {
                    yIn = map[0].length - 1;
                } else {
                    yIn = map[0].length - 2;
                }
            }
            if(map[1][yIn] == SpecialCharacters.WALL.getCharacterString().charAt(0)) {
                yIn = 0;
            }
        } while (yIn == 0);
        do {
            yOut = rand.nextInt(mapHeight);
            if(yOut >= map[0].length - 1) {
                if(yOut == map[0].length + resizeHeight - 1) {
                    yOut = map[0].length - 1;
                } else {
                    yOut = map[0].length - 2;
                }
            }
            if(map[mapWight - resizeWight - 2][yOut] == SpecialCharacters.WALL.getCharacterString().charAt(0)) {
                yOut = 0;
            }
        } while (yOut == 0);
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        file.seek(mapWight * yIn + yIn);
        file.write(SpecialCharacters.SPACE.getCharacterString().charAt(0));
        file.write(SpecialCharacters.PLAYER.getCharacterString().charAt(0));
        file.seek(mapWight * (yOut + 1) + yOut - 1);
        file.write(SpecialCharacters.SPACE.getCharacterString().charAt(0));
        file.close();
    }
  
    
    private static char[][] generateRooms() {
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
        final List<Integer> remainingRooms = new ArrayList<Integer>();
        for (int i = 0; i < xRoom * yRoom; i++) {
            remainingRooms.add(i);
        }
        while (!remainingRooms.isEmpty()) {
            // pick a random unconnected room
            final int roomIndexInList = rand.nextInt(remainingRooms.size());
            final int roomIndex = remainingRooms.get(roomIndexInList);
            final int[] room = new int[] { roomIndex % xRoom, roomIndex / xRoom };
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
                        case START:
                        default:
                    }
                    
                }
                
            }
        }
        char[][] map = new char[xRoom * size + xRoom + 1][yRoom * size + yRoom + 1];
        // Build all ground
        for (int x = 0; x < map.length; x++) {
            Arrays.fill(map[x], SpecialCharacters.SPACE.getCharacterString().charAt(0));
        }
        // Build wall grid
        for (int x = 0; x < map.length; x += size + 1) {
            Arrays.fill(map[x], SpecialCharacters.WALL.getCharacterString().charAt(0));
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y += size + 1) {
                map[x][y] = SpecialCharacters.WALL.getCharacterString().charAt(0);
            }
        }
        // Drill the connecting holes
        for (int x = 0; x < neighbor.length; x++) {
            for (int y = 0; y < neighbor[x].length; y++) {
                switch (neighbor[x][y]) {
                    case UP:
                        int dx = x * (size + 1);
                        int dy = y * (size + 1);
                        for (int i = 1; i < (size + 1); i++) {
                            map[dx + i][dy] = SpecialCharacters.SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case LEFT:
                        dx = x * (size + 1);
                        dy = y * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx][dy + i] = SpecialCharacters.SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case RIGHT:
                        dx = (x + 1) * (size + 1);
                        dy = (y) * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx][dy + i] = SpecialCharacters.SPACE.getCharacterString().charAt(0);
                        }
                        break;
                    case DOWN:
                        dx = (x) * (size + 1);
                        dy = (y + 1) * (size + 1);
                        for (int i = 1; i < size + 1; i++) {
                            map[dx + i][dy] = SpecialCharacters.SPACE.getCharacterString().charAt(0);
                        }
                    case START:
                    default:
                }
            }
        }
        return map;
    }
}
