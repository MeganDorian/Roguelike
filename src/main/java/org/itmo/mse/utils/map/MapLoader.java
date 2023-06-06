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
    
    private TerminalRectangle playerPosition;
    
    private final WallsLoader wallsLoader = new WallsLoader();
    
    private final ItemsLoader itemLoader = new ItemsLoader();
    
    private final MobLoader mobLoader = new MobLoader();
    
    private void getExitPosition(String line) throws IncorrectMapFormatException {
        if (line.charAt(width - 1) == SpecialCharacters.getSpaceChar()) {
            if (exitPosition != null) {
                throw new IncorrectMapFormatException("It can't be two exits on map");
            }
            exitPosition = new TerminalPosition(width, height);
        }
    }
    
    private void getPlayerPosition(String line) throws IncorrectMapFormatException {
        if (line.indexOf(SpecialCharacters.getUserChar()) != -1) {
            if (playerPosition != null) {
                throw new IncorrectMapFormatException("It can't be two players on map");
            }
            playerPosition =
                new TerminalRectangle(line.indexOf(SpecialCharacters.getUserChar()), height, 1, 1);
        }
    }
    
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
    
    private void processLine(String line) throws IncorrectMapFormatException {
        getExitPosition(line);
        getPlayerPosition(line);
        
        searchForObjectsOnMap(line);
    }
    
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
    
    public Map loadFromFile(String fileName, boolean isFirst, Player player)
        throws IOException, IncorrectMapFormatException {
        readMap(fileName, isFirst);
        
        player.setPosition(playerPosition);
        
        TerminalRectangle start = new TerminalRectangle(0, 0, width, height);
        return Map.builder().exit(exitPosition).border(start)
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
