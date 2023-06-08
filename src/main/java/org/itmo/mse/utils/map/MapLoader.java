package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;
import org.itmo.mse.game.objects.map.Wall;
import org.itmo.mse.utils.FileUtils;

/**
 * Loads map from file
 */
// TODO check if map in file is larger than terminal size
@UtilityClass
public class MapLoader {
    
    private int height;
    
    private int width;
    
    private TerminalPosition exitPosition;
    private TerminalPosition startPosition;
    
    private TerminalRectangle playerPosition;
    
    private WallsLoader wallsLoader;
    
    private ItemsLoader itemLoader;
    
    private MobLoader mobLoader;
    
    /**
     * Resets the map
     */
    private void reset() {
        exitPosition = null;
        startPosition = null;
        playerPosition = null;
        wallsLoader = new WallsLoader();
        itemLoader = new ItemsLoader();
        mobLoader = new MobLoader();
    }
    
    /**
     * Checking for the existence of a level entry position
     *
     * @param line
     * @param indexToSearch
     * @param position
     * @param error
     * @return
     * @throws IncorrectMapFormatException
     */
    private TerminalPosition checkExitStartPosition(String line, int indexToSearch,
                                                    TerminalPosition position, String error)
        throws IncorrectMapFormatException {
        if (line.charAt(indexToSearch - 1) == SpecialCharacters.getSpaceChar()) {
            if (position != null) {
                throw new IncorrectMapFormatException(error);
            }
            return new TerminalPosition(indexToSearch, height);
        }
        return position;
    }
    
    /**
     * Sets the user's position on the map.
     * Performs a check to ensure that the number of users
     * on the map is correct.
     *
     * @param line
     * @throws IncorrectMapFormatException
     */
    private void getPlayerPosition(String line) throws IncorrectMapFormatException {
        if (line.indexOf(SpecialCharacters.getUserChar()) != -1) {
            if (playerPosition != null) {
                throw new IncorrectMapFormatException("It can't be two players on map");
            }
            playerPosition =
                new TerminalRectangle(line.indexOf(SpecialCharacters.getUserChar()), height, 1, 1);
        }
    }
    
    /**
     * Interprets the characters in the string according to the game objects
     *
     * @param line -- analysis line
     */
    private void searchForObjectsOnMap(String line) {
        for (int i = 0; i < line.length(); i++) {
            TerminalRectangle position = new TerminalRectangle(i, height, 1, 1);
            if (line.charAt(i) == SpecialCharacters.getWallChar()) {
                wallsLoader.getWalls(position, i, height);
            } else if (line.charAt(i) == SpecialCharacters.getMobChar()) {
                mobLoader.getMobs(position);
            } else if (line.charAt(i) == SpecialCharacters.getItemChar()) {
                itemLoader.getItems(position);
            }
        }
    }
    
    /**
     * Performs string processing
     * @param line -- analysis line
     * @throws IncorrectMapFormatException
     */
    private void processLine(String line) throws IncorrectMapFormatException {
        exitPosition =
            checkExitStartPosition(line, width, exitPosition, "It can't be two exits on map");
        startPosition =
            checkExitStartPosition(line, 1, startPosition, "It can't be two starts on map");
        getPlayerPosition(line);
        
        searchForObjectsOnMap(line);
    }
    
    /**
     * Read map from file
     * First level is static
     *
     * @param fileName
     * @param isFirst
     * @throws IOException
     * @throws IncorrectMapFormatException
     */
    private void readMap(String fileName, boolean isFirst)
        throws IOException, IncorrectMapFormatException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            isFirst ? FileUtils.getFileFromResource(fileName) : FileUtils.getFile(fileName)))) {
            height = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                width = line.length();
                processLine(line);
                height++;
            }
        }
        
        if (exitPosition == null) {
            throw new IncorrectMapFormatException("There is no exit from the level");
        }
        if (playerPosition == null) {
            throw new IncorrectMapFormatException("There is no player position on the map");
        }
    }
    
    /**
     * Load map from file and return map object
     *
     * @param fileName
     * @param isFirst
     * @param player
     * @return map
     * @throws IOException
     * @throws IncorrectMapFormatException
     */
    public Map loadFromFile(String fileName, boolean isFirst, Player player)
        throws IOException, IncorrectMapFormatException {
        reset();
        readMap(fileName, isFirst);
        
        player.setPosition(playerPosition);
        
        TerminalRectangle mapBorder = new TerminalRectangle(0, 0, width, height);
        return Map.builder().start(startPosition).exit(exitPosition).border(mapBorder)
                  .walls(wallsLoader.getWalls().entrySet().stream().map(entry -> {
                      TerminalPosition wallStart = entry.getValue();
                      TerminalPosition wallEnd = entry.getKey();
                      int width = Math.abs(wallStart.getColumn() - wallEnd.getColumn()) + 1;
                      int height = Math.abs(wallStart.getRow() - wallEnd.getRow()) + 1;
                      return new Wall(
                          new TerminalRectangle(wallStart.getColumn(), wallStart.getRow(), width,
                                                height));
                  }).collect(Collectors.toList())).things(itemLoader.getItems())
                  .mobs(mobLoader.getMobs()).build();
    }
}
