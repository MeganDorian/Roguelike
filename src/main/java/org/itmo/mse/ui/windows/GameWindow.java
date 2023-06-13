package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.itmo.mse.constants.ChangeNames;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.itmo.mse.constants.Change;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.actions.Action;
import org.itmo.mse.game.actions.CheckForChanged;
import org.itmo.mse.game.actions.Move;
import org.itmo.mse.generation.MapGeneration;
import org.itmo.mse.ui.Printer;
import org.itmo.mse.utils.map.MapLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends Window {
    private final Game game;
    
    private final Move move;
    
    private final Action interact;
    private final Action backpackAction;
    private final Action dropItem;
    private final Action reaction;
    
    public GameWindow(Move move, Game game, Action interact, Action backpackAction, Action dropItem, Action reaction)
            throws IOException, IncorrectMapFormatException {
        this.move = move;
        this.game = game;
        this.interact = interact;
        this.backpackAction = backpackAction;
        this.dropItem = dropItem;
        this.reaction = reaction;
        ((CheckForChanged) reaction).setOldGameObject(new Game(game));
        screen.clear();
        screen.refresh();
        printLevel();
    }
    
    /**
     * Loads the level and prints it. The first level is static the others are randomly generated
     */
    private void printLevel() throws IOException, IncorrectMapFormatException {
        List<String> info = new ArrayList<>();
        if (game.getDungeonLevel() == 1) {
            // load tutorial level from file
            loadLevelFromFile("first_lvl", true);
        } else if (game.getDungeonLevel() == -1) {
            //after death
            screen.clear();
            loadLevelFromFile("first_lvl", true);
            game.setDungeonLevel(1);
            info.add("You died x.x");
            info.add("Try again");
        } else {
            screen.clear();
            MapGeneration.builder().walls((int) (Printer.getSize().getColumns() * Proportions.mapWidth),
                (int) (Printer.getSize().getRows() * Proportions.mapHeight)).
                mobs(Proportions.numberMobs).items(Proportions.numberItems).build();
            loadLevelFromFile(MapGeneration.fileName, false);
        }
        printObjectInfo(info);
        printObject(game.getLevelMap());
        printObject(game.getPlayer());
        printHelp();
        playerPrinter.printPlayerInfo(game, startRow);
        backpackPrinter.printBackpack(game);
        game.setBackpackItemsInRow(backpackPrinter.getBackpackItemsCountInRow());
        screen.refresh();
    }
    
    /**
     * Loads a level from a file <br> If level one, it reads from the resource folder,
     * otherwise it takes the file with the name given
     */
    private void loadLevelFromFile(String fileName, boolean isFirst) throws IOException, IncorrectMapFormatException {
        game.setLevelMap(MapLoader.loadFromFile(fileName, isFirst, game.getPlayer()));
    }
    
    /**
     * Tracks user presses during play and reacts according to the pressed button
     */
    public void play() throws IOException, IncorrectMapFormatException {
        while (true) {
            KeyStroke input = screen.pollInput();
            List<String> info = null;
            List<String> changes = null;
            if (input == null && game.getDungeonLevel() != -1 && changes == null) {
                changes = reaction.execute(textGraphics);
                if(!changes.isEmpty()) {
                    reprint(changes, null);
                }
                changes = null;
                continue;
            }
            if (game.getDungeonLevel() == -1) {
                printLevel();
            } else {
                KeyType pressedKey = input.getKeyType();
                if (pressedKey == KeyType.Escape) {
                    terminal.close();
                    game.getTimerForDamage().cancel();
                    return;
                } else if (pressedKey == KeyType.ArrowDown || pressedKey == KeyType.ArrowUp ||
                           pressedKey == KeyType.ArrowLeft || pressedKey == KeyType.ArrowRight) {
                    move.setDirection(pressedKey);
                    info = move.execute(textGraphics);
                if (game.getLevelMap() != null) {
                        game.makeAllMobsAlive();
                    }} else if (input.getCharacter() != null) {
                    if (input.getCharacter().equals('e') ) {
                        info = interact.execute(textGraphics);
                    } else if (input.getCharacter().equals('i') ) {
                        info = backpackAction.execute(textGraphics);
                    } else if (input.getCharacter().equals('x') ) {
                        dropItem.execute(textGraphics);
                    }
                }
                changes = reaction.execute(textGraphics);
                if (!changes.isEmpty()) {
                    reprint(changes, info);
                }
            }
            changes = null;
        }
    }
    
    public void reprint(List<String> changes, List<String> info)
        throws IncorrectMapFormatException, IOException {
        reprint(changes, info);
                }
            }
            changes = null;
        }
    }
    
    public void reprint(List<String> changes, List<String> info) throws IncorrectMapFormatException, IOException {if (changes.contains(ChangeNames.DUNGEON_LEVEL)) {
            printLevel();
        } else {
            if (info != null) {
                printObjectInfo(info);
            }
            if (changes.contains(Change.DEATH_MOB.name())) {
                printObject(game.getLevelMap());
            }
            if (changes.contains(ChangeNames.DEATH_MOB)) {
                printObject(game.getLevelMap());
            }
            if (changes.contains(ChangeNames.PLAYER_POSITION)) {
                printObject(game.getPlayer());
            }
            if (changes.contains(ChangeNames.SELECTED_INDEX_ITEM) ||
                changes.contains(ChangeNames.BACKPACK_OPENED_TRUE) || changes.contains(ChangeNames.ADD_REMOVE_ITEM)) {
                TextCharacter color = game.isBackpackOpened() ? SpecialCharacters.SELECTED_ITEM :
                                      SpecialCharacters.SPACE;
                int selectedItemIndex = game.getPlayer().getBackpack().getSelectedItemIndex();
                backpackPrinter.printSelectBackpackItem(selectedItemIndex, color);
                backpackPrinter.printBackpack(game);
            }
            if (changes.contains(ChangeNames.BACKPACK_OPENED_FALSE)) {
                backpackPrinter.printBackpack(game);
            }
            if (changes.contains(ChangeNames.PLAYER_INFO)) {
                playerPrinter.printPlayerInfo(game, startRow);
            }
            screen.refresh();
        }
    }
}
