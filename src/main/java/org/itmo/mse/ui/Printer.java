package org.itmo.mse.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.utils.SpecialCharacters;

public abstract class Printer {
    
    protected static Terminal terminal;
    
    protected static TerminalScreen screen;
    
    protected final TextGraphics textGraphics = screen.newTextGraphics();
    
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
    
    protected void printHelp() {
    
    }
    
    protected void printPlayerInfo(Game game) throws IOException {
        int column = (int) (getSize().getColumns() * Proportions.mapWidth);
        int row = 1;
        printPlayerStats(game, column, row);
    }
    
    private void printPlayerStats(Game game, int column, int row) throws IOException {
        List<String> playerInfo = List.of("Player", "", "LVL: " + game.getPlayer().getLevel(),
                                          "XP: " + game.getPlayer().getExperience(),
                                          "HP: " + game.getPlayer().getHealth(),
                                          "ATTACK: " + game.getPlayer().getDamage(),
                                          "ARMOR: " + game.getPlayer().getArmor());
        textGraphics.putString(column + 1, row, "DUNGEON LEVEL: " + game.getCurrentLevel());
        row++;
        
        printPlayerInfoBorder(column, row, getSize().getColumns() - 1, row + playerInfo.size() + 1);
        
        for (String info : playerInfo) {
            row++;
            textGraphics.putString(column + 2, row, info);
        }
    }
    
    private void printPlayerInfoBorder(int x1, int y1, int x2, int y2) {
        textGraphics.drawLine(x1, y1, x2, y1, SpecialCharacters.DELIMITER);
        textGraphics.drawLine(x1, y1, x1, y2, SpecialCharacters.DELIMITER);
        textGraphics.drawLine(x2, y1, x2, y2, SpecialCharacters.DELIMITER);
        textGraphics.drawLine(x1, y2, x2, y2, SpecialCharacters.DELIMITER);
    }
    
    protected TerminalSize getSize() throws IOException {
        return terminal.getTerminalSize();
    }
}
