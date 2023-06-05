package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;
import org.itmo.mse.utils.map.MapLoader;

public class GameWindow extends Window {
    private final Game game;
    
    private final BackpackBlock backpack = new BackpackBlock();
    
    public GameWindow(Game game) throws IOException, IncorrectMapFormatException {
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
    
    public void play() throws IOException {
        while (true) {
            KeyStroke input = screen.pollInput();
            if (input == null) {
                continue;
            }
            switch (input.getKeyType()) {
                case Escape:
                    terminal.close();
                    return;
            }
            
        }
    }
}
