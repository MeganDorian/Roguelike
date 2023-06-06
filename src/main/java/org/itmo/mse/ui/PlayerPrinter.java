package org.itmo.mse.ui;

import static org.itmo.mse.constants.Proportions.playerBlockHeight;
import static org.itmo.mse.constants.Proportions.playerBlockWidth;
import static org.itmo.mse.ui.Printer.getSize;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.Game;

public class PlayerPrinter {
    
    private final TextGraphics textGraphics;
    
    protected PlayerPrinter(TextGraphics textGraphics) throws IOException {
        this.textGraphics = textGraphics;
    }
    
    public void printPlayerInfo(Game game, int startRow) throws IOException {
        int column = (int) (getSize().getColumns() * Proportions.mapWidth);
        printPlayerStats(game, column, startRow);
    }
    
    private void printPlayerStats(Game game, int column, int startRow) throws IOException {
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
}
