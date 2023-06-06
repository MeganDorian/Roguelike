package org.itmo.mse.ui;

import static com.googlecode.lanterna.Symbols.ARROW_DOWN;
import static com.googlecode.lanterna.Symbols.ARROW_LEFT;
import static com.googlecode.lanterna.Symbols.ARROW_RIGHT;
import static com.googlecode.lanterna.Symbols.ARROW_UP;
import static org.itmo.mse.constants.Proportions.helpHeight;
import static org.itmo.mse.constants.Proportions.playerBlockHeight;
import static org.itmo.mse.constants.Proportions.playerBlockWidth;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.objects.Object;


public abstract class Printer {
    private final int startRow = 1;
    
    protected static Terminal terminal;
    
    protected static TerminalScreen screen;
    
    protected static TextGraphics textGraphics = null;
    
    protected final BackpackPrinter backpackPrinter = new BackpackPrinter(textGraphics);
    
    protected Printer() throws IOException {
        textGraphics = screen.newTextGraphics();
    }
    
    
    protected void printObject(Object object) throws IOException {
        object.print(textGraphics);
        screen.refresh();
    }
    
    protected void printStringAtPosition(String string, TerminalPosition position)
        throws IOException {
        textGraphics.putString(position, string);
        screen.refresh();
    }
    
    public static void eraseAtPosition(TerminalPosition position, int length) throws IOException {
        textGraphics.drawLine(position, position.withRelativeColumn(length - 1),
                              SpecialCharacters.SPACE);
        screen.refresh();
    }
    
    protected void printHelp() throws IOException {
        List<String> help =
            List.of(ARROW_DOWN + " " + ARROW_UP + " " + ARROW_LEFT + " " + ARROW_RIGHT + " : move",
                    "I : open/close backpack", "E : interact", "SPACE : put on item",
                    "ESC : " + "exit game");
        int column = 1;
        int row = getSize().getRows() - (int) (getSize().getRows() * helpHeight) + 1;
        
        for (String h : help) {
            textGraphics.putString(new TerminalPosition(column, row), h);
            row += 2;
        }
    }
    
    protected void printPlayerInfo(Game game) throws IOException {
        int column = (int) (getSize().getColumns() * Proportions.mapWidth);
        printPlayerStats(game, column);
    }
    
    private void printPlayerStats(Game game, int column) throws IOException {
        List<String> playerInfo = game.getPlayer().getInfo();
        textGraphics.putString(column + 1, startRow, "DUNGEON LEVEL: " + game.getDungeonLevel());
        int row = 2;
        
        textGraphics.drawRectangle(new TerminalPosition(column, row), new TerminalSize(
            (int) (getSize().getColumns() * playerBlockWidth),
            (int) (getSize().getRows() * playerBlockHeight) - 1), SpecialCharacters.DELIMITER);
        
        for (String info : playerInfo) {
            row++;
            textGraphics.putString(column + 2, row, info);
        }
    }
    
    /**
     * Prints object info under the backpack in the right
     */
    protected void printObjectInfo(List<String> info) throws IOException {
        TerminalRectangle infoBlock = backpackPrinter.getInfoBlock();
        TerminalPosition infoBlockPosition =
            infoBlock.position.withRelativeColumn(1).withRelativeRow(1);
        textGraphics.fillRectangle(infoBlockPosition,
                                   infoBlock.size.withRelativeRows(-2).withRelativeColumns(-2),
                                   SpecialCharacters.SPACE);
        for (String i : info) {
            textGraphics.putString(infoBlockPosition, i);
            infoBlockPosition = infoBlockPosition.withRelativeRow(1);
        }
        screen.refresh();
    }
    
    protected static TerminalSize getSize() throws IOException {
        return terminal.getTerminalSize();
    }
}
