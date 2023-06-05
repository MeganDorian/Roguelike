package org.itmo.mse.ui;

import static com.googlecode.lanterna.Symbols.ARROW_DOWN;
import static com.googlecode.lanterna.Symbols.ARROW_LEFT;
import static com.googlecode.lanterna.Symbols.ARROW_RIGHT;
import static com.googlecode.lanterna.Symbols.ARROW_UP;
import static org.itmo.mse.constants.Proportions.helpHeight;
import static org.itmo.mse.constants.Proportions.playerBlockHeight;
import static org.itmo.mse.constants.Proportions.playerBlockWidth;

import com.googlecode.lanterna.TerminalPosition;
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
import org.itmo.mse.game.objects.Player;


public abstract class Printer {
    
    protected static Terminal terminal;
    
    protected static TerminalScreen screen;
    
    protected final TextGraphics textGraphics = screen.newTextGraphics();
    
    protected final BackpackPrinter backpackPrinter = new BackpackPrinter(textGraphics);
    
    protected Printer() throws IOException {
    }
    
    public void printObject(Object object) throws IOException {
        object.print(textGraphics);
        screen.refresh();
    }
    
    protected void printStringAtPosition(String string, TerminalPosition position)
        throws IOException {
        textGraphics.putString(position, string);
        screen.refresh();
    }
    
    protected void eraseStringAtPosition(TerminalPosition position, int length) throws IOException {
        textGraphics.drawLine(position, position.withRelativeColumn(length - 1), ' ');
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
    
    public void printPlayerInfo(Game game) throws IOException {
        int column = (int) (getSize().getColumns() * Proportions.mapWidth);
        printPlayerStats(game, column);
        backpackPrinter.printBackpack(game, column);
    }
    
    private void printPlayerStats(Game game, int column) throws IOException {
        int row = 1;
        Player player = game.getPlayer();
        List<String> playerInfo =
            List.of("Student", "", "LVL: " + player.getLevel(), "XP: " + player.getExperience(),
                    "HP: " + player.getHealth(), "ATTACK: " + player.getAttack(),
                    "ARMOR: " + player.getArmor());
        textGraphics.putString(column + 1, row, "DUNGEON LEVEL: " + game.getDungeonLevel());
        row++;
        
        textGraphics.drawRectangle(new TerminalPosition(column, row), new TerminalSize(
            (int) (getSize().getColumns() * playerBlockWidth),
            (int) (getSize().getRows() * playerBlockHeight) - 1), SpecialCharacters.DELIMITER);
        
        for (String info : playerInfo) {
            row++;
            textGraphics.putString(column + 2, row, info);
        }
    }
    
    protected static TerminalSize getSize() throws IOException {
        return terminal.getTerminalSize();
    }
}
