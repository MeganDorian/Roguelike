package org.itmo.mse.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Object;

import java.io.IOException;
import java.util.List;

import static com.googlecode.lanterna.Symbols.*;
import static org.itmo.mse.constants.Proportions.helpHeight;


public abstract class Printer {
    protected static Terminal terminal;
    protected static TerminalScreen screen;
    protected static TextGraphics textGraphics = null;
    protected final int startRow = 1;
    protected final BackpackPrinter backpackPrinter;
    
    protected final PlayerPrinter playerPrinter;
    
    protected Printer() throws IOException {
        textGraphics = screen.newTextGraphics();
        backpackPrinter = new BackpackPrinter(textGraphics);
        playerPrinter = new PlayerPrinter(textGraphics);
    }
    
    /**
     * Erases a set position (sets a system-defined character in a set position)
     */
    public static void eraseAtPosition(TerminalPosition position, int length) throws IOException {
        textGraphics.drawLine(position, position.withRelativeColumn(length - 1), SpecialCharacters.SPACE);
        screen.refresh();
    }
    
    /**
     * Prints the object on the screen
     */
    protected void printObject(Object object) throws IOException {
        object.print(textGraphics);
        screen.refresh();
    }
    
    /**
     * Prints a line at a given position
     */
    protected void printStringAtPosition(String string, TerminalPosition position) throws IOException {
        textGraphics.putString(position, string);
        screen.refresh();
    }
    
    /**
     * Prints help for the player
     */
    protected void printHelp() throws IOException {
        List<String> help = List.of(ARROW_DOWN + " " + ARROW_UP + " " + ARROW_LEFT + " " + ARROW_RIGHT + " : move",
                                    "I : open/close backpack",
                                    "E : interact",
                                    "X : drop item",
                                    "ESC : exit game");
        int column = 1;
        int row = getSize().getRows() - (int) (getSize().getRows() * helpHeight) + 1;
        
        for (String h : help) {
            if (row >= getSize().getRows()) {
                row = getSize().getRows() - (int) (getSize().getRows() * helpHeight) + 1;
                int longestHelpString = help.stream().mapToInt(String::length).max().getAsInt() + 5;
                column += longestHelpString;
            }
            textGraphics.putString(new TerminalPosition(column, row), h);
            row += 2;
        }
    }
    
    /**
     * Gets the current terminal size
     *
     * @return terminal size
     */
    protected static TerminalSize getSize() throws IOException {
        return terminal.getTerminalSize();
    }
    
    /**
     * Prints object info under the backpack in the right
     */
    protected void printObjectInfo(List<String> info) throws IOException {
        TerminalRectangle infoBlock = backpackPrinter.getInfoBlock();
        TerminalPosition infoBlockPosition = infoBlock.position.withRelativeColumn(1).withRelativeRow(1);
        textGraphics.fillRectangle(infoBlockPosition,
                                   infoBlock.size.withRelativeRows(-2).withRelativeColumns(-2),
                                   SpecialCharacters.SPACE);
        for (String i : info) {
            textGraphics.putString(infoBlockPosition, i);
            infoBlockPosition = infoBlockPosition.withRelativeRow(1);
        }
        screen.refresh();
    }
}
