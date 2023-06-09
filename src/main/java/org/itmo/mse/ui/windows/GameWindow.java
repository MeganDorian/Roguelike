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
    
    /**
     * Loads the level and prints it
     * The first level is static
     * the others are randomly generated
     *
     * @throws IOException
     * @throws IncorrectMapFormatException
     */
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
    
    /**
     * Loads a level from a file
     * If level one, it reads from the resource folder,
     * otherwise it takes the file with the name given
     *
     * @param fileName
     * @param isFirst
     * @throws IOException
     * @throws IncorrectMapFormatException
     */
    private void loadLevelFromFile(String fileName, boolean isFirst)
        throws IOException, IncorrectMapFormatException {
        game.setLevelMap(MapLoader.loadFromFile(fileName, isFirst, game.getPlayer()));
    }
    
    /**
     * Tracks user presses during play
     * and reacts according to the pressed button
     *
     * @throws IOException
     * @throws IncorrectMapFormatException
     */
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
                if (input.getCharacter().equals('e') || input.getCharacter().equals('у')) {
                    List<String> pickedUpItem = interact.execute(textGraphics);
                    printObjectInfo(pickedUpItem);
                    backpackPrinter.printBackpack(game);
                } else if (input.getCharacter().equals('i') || input.getCharacter().equals('ш')) {
                    List<String> selectedItem = backpackAction.execute(textGraphics);
                    printObjectInfo(selectedItem);
                } else if (input.getCharacter().equals('x') || input.getCharacter().equals('ч')) {
                    dropItem.execute(textGraphics);
                }
            }
            printObject(game.getLevelMap());
            printObject(game.getPlayer());
            TextCharacter color =
                game.isBackpackOpened() ? SpecialCharacters.SELECTED_ITEM : SpecialCharacters.SPACE;
            int selectedItemIndex = game.getPlayer().getBackpack().getSelectedItemIndex();
            backpackPrinter.printSelectBackpackItem(selectedItemIndex, color);
            backpackPrinter.printBackpack(game);
            playerPrinter.printPlayerInfo(game, startRow);
            screen.refresh();
        }
    }
}
