package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.actions.Action;
import org.itmo.mse.game.actions.Move;
import org.itmo.mse.generation.MapGeneration;
import org.itmo.mse.ui.Printer;
import org.itmo.mse.utils.map.MapLoader;

public class GameWindow extends Window {
    private final Game game;
    
    private final Move move;
    
    private final Action interact;
    private final Action backpackAction;
    private final Action dropItem;
    
    public GameWindow(Move move, Game game, Action interact, Action backpackAction, Action dropItem)
        throws IOException, IncorrectMapFormatException {
        this.move = move;
        this.game = game;
        this.interact = interact;
        this.backpackAction = backpackAction;
        this.dropItem = dropItem;
        screen.clear();
        screen.refresh();
        printLevel();
    }
    
    private void printLevel() throws IOException, IncorrectMapFormatException {
        if (game.getDungeonLevel() == 1) {
            // load tutorial level from file
            loadLevelFromFile("first_lvl", true);
        } else {
            screen.clear();
            MapGeneration.generate((int) (Printer.getSize().getColumns() * Proportions.mapWidth),
                (int) (Printer.getSize().getRows() * Proportions.mapHeight),
                Proportions.numberMobs, Proportions.numberItems);
            loadLevelFromFile(MapGeneration.fileName, false);
        }
        printObject(game.getLevelMap());
        printObject(game.getPlayer());
        printHelp();
        playerPrinter.printPlayerInfo(game, startRow);
        backpackPrinter.printBackpack(game);
        game.setBackpackItemsInRow(backpackPrinter.getBackpackItemsCountInRow());
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
                if(nearestObject == null) {
                    printLevel();
                } else {
                    printObjectInfo(nearestObject);
                }
            } else if (input.getCharacter() != null) {
                if (input.getCharacter().equals('e')) {
                    List<String> pickedUpItem = interact.execute(textGraphics);
                    printObjectInfo(pickedUpItem);
                    backpackPrinter.printBackpack(game);
                } else if (input.getCharacter().equals('i')) {
                    List<String> selectedItem = backpackAction.execute(textGraphics);
                    printObjectInfo(selectedItem);
                } else if (input.getCharacter().equals('x')) {
                    dropItem.execute(textGraphics);
                }
            }
            printObject(game.getLevelMap());
            printObject(game.getPlayer());
            TextCharacter color =
                game.isBackpackOpened() ? SpecialCharacters.SELECTED_ITEM : SpecialCharacters.SPACE;
            int selectedItemIndex = game.getPlayer().getBackpack().getSelectedItem();
            backpackPrinter.printSelectBackpackItem(selectedItemIndex, color);
            backpackPrinter.printBackpack(game);
            playerPrinter.printPlayerInfo(game, startRow);
            screen.refresh();
        }
    }
}
