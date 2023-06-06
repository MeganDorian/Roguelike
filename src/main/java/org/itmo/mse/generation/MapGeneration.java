package org.itmo.mse.generation;

import com.googlecode.lanterna.TerminalSize;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.ui.Printer;

public class MapGeneration extends Generation {
    
    private final static String fileName = System.getProperty("user.dir") + "\\src\\main" +
                                           "\\resources\\" +
                                           "new_level";
    
    public static void generateMap() throws IOException {
        TerminalSize size = Printer.getSize();
        int mapWeight = (int) (size.getColumns() * Proportions.mapWidth);
        int mapHeight = (int) (size.getRows() * Proportions.mapHeight);
        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }
        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        char[][] map = generateMap(3, 8, 3,'@', ' ');
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                raf.write(map[x][y]);
            }
            raf.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        raf.close();
    }
    
    private static enum RelationMarker {
        UP, RIGHT, LEFT, DOWN, START, EMPTY;
    }
    
    private static char[][] generateMap(final int yRooms, final int xRooms, final int roomSize,
                                       final char wallChar, final char floorChar) {
        
        if (xRooms * yRooms * roomSize == 0) {
            throw new RuntimeException("xRoom, yRoom, and roomSize must all be non-zero.");
        }
        
        final boolean[][] connected = new boolean[xRooms][yRooms];
        final RelationMarker[][] neighbor = new RelationMarker[xRooms][yRooms];
        for (int x = 0; x < neighbor.length; x++) {
            for (int y = 0; y < neighbor[x].length; y++) {
                neighbor[x][y] = RelationMarker.EMPTY;
                connected[x][y] = false;
            }
        }
        
        final Random rand = new Random();
        
        // pick a random room to start
        final int startX = rand.nextInt(xRooms);
        final int startY = rand.nextInt(yRooms);
        connected[startX][startY] = true;
        neighbor[startX][startY] = RelationMarker.START;
        
        // Build a list of rooms remaining to be connected.
        final List<Integer> remainingRooms = new ArrayList<Integer>();
        for (int i = 0; i < xRooms * yRooms; i++) {
            remainingRooms.add(i);
        }
        
        while (!remainingRooms.isEmpty()) {
            
            // pick a random unconnected room
            final int roomIndexInList = rand.nextInt(remainingRooms.size());
            final int roomIndex = remainingRooms.get(roomIndexInList);
            final int[] room = new int[] { roomIndex % xRooms, roomIndex / xRooms };
            
            if (connected[room[0]][room[1]]) {
                remainingRooms.remove(roomIndexInList);
            } else {
                // Look for neighbors in random order
                final RelationMarker[] lookOrder = RelationMarker.values();
                Collections.shuffle(Arrays.asList(lookOrder));
                
                for (RelationMarker i : lookOrder) {
                    
                    switch (i) {
                        case UP:
                            if (room[1] != 0 && connected[room[0]][room[1] - 1]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = RelationMarker.UP;
                            }
                            break;
                        case LEFT:
                            if (room[0] != 0 && connected[room[0] - 1][room[1]]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = RelationMarker.LEFT;
                            }
                            break;
                        case RIGHT:
                            if (room[0] != xRooms - 1 && connected[room[0] + 1][room[1]]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = RelationMarker.RIGHT;
                            }
                            break;
                        case DOWN:
                            if (room[1] != yRooms - 1 && connected[room[0]][room[1] + 1]) {
                                connected[room[0]][room[1]] = true;
                                neighbor[room[0]][room[1]] = RelationMarker.DOWN;
                            }
                        case START:
                        default:
                    }
                    
                }
                
            }
        }
        
        char[][] map = new char[xRooms * roomSize + xRooms + 1][yRooms * roomSize + yRooms + 1];
        
        // Build all ground
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                map[x][y] = floorChar;
            }
        }
        
        // Build wall grid
        for (int x = 0; x < map.length; x += roomSize + 1) {
            for (int y = 0; y < map[x].length; y++) {
                map[x][y] = wallChar;
            }
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y += roomSize + 1) {
                map[x][y] = wallChar;
            }
        }
        
        // Drill the connecting holes
        for (int x = 0; x < neighbor.length; x++) {
            for (int y = 0; y < neighbor[x].length; y++) {
                switch (neighbor[x][y]) {
                    case UP:
                        int dx = x * (roomSize + 1);
                        int dy = y * (roomSize + 1);
                        for (int i = 1; i < (roomSize + 1); i++) {
                            map[dx + i][dy] = floorChar;
                        }
                        break;
                    case LEFT:
                        dx = x * (roomSize + 1);
                        dy = y * (roomSize + 1);
                        for (int i = 1; i < roomSize + 1; i++) {
                            map[dx][dy + i] = floorChar;
                        }
                        break;
                    case RIGHT:
                        dx = (x + 1) * (roomSize + 1);
                        dy = (y) * (roomSize + 1);
                        for (int i = 1; i < roomSize + 1; i++) {
                            map[dx][dy + i] = floorChar;
                        }
                        break;
                    case DOWN:
                        dx = (x) * (roomSize + 1);
                        dy = (y + 1) * (roomSize + 1);
                        for (int i = 1; i < roomSize + 1; i++) {
                            map[dx + i][dy] = floorChar;
                        }
                    case START:
                    default:
                }
            }
        }
        
        return map;
    }
}
