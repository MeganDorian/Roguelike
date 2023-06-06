package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.actions.Move;
import org.itmo.mse.utils.map.MapLoader;

public class GameWindow extends Window {
    private final Game game;
    
    private final BackpackBlock backpack = new BackpackBlock();
    
    private final Move move;
    
    public GameWindow(Move move, Game game) throws IOException, IncorrectMapFormatException {
        this.move = move;
        this.game = game;
        screen.clear();
        screen.refresh();
        printLevel();
    }
    
    private void printLevel() throws IOException, IncorrectMapFormatException {
        if (game.getDungeonLevel() == 1) {
            // load tutorial level from file
            loadLevelFromFile("first_lvl", true);
        } else {
            // generate new map
        }
        printObject(game.getLevelMap());
        printObject(game.getPlayer());
        printHelp();
        printPlayerInfo(game);
        screen.refresh();
    }
    
    private void loadLevelFromFile(String fileName, boolean isFirst)
        throws IOException, IncorrectMapFormatException {
        game.setLevelMap(MapLoader.loadFromFile(fileName, isFirst, game.getPlayer()));
    }
    
    public void play() throws IOException, IncorrectMapFormatException {
        while (true) {
            KeyStroke input = screen.pollInput();
            if (input == null) {
                continue;
            }
            KeyType pressedKey = input.getKeyType();
            if (pressedKey == KeyType.Escape) {
                terminal.close();
                return;
                
            } else if (pressedKey == KeyType.ArrowDown || pressedKey == KeyType.ArrowUp ||
                       pressedKey == KeyType.ArrowLeft || pressedKey == KeyType.ArrowRight) {
                move.setDirection(pressedKey);
                List<String> nearestObject = move.execute(textGraphics);
                //print nearest object info
                printObjectInfo(nearestObject);
                printObject(game.getPlayer());
            }
            
        }
    }
}
