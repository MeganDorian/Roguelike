package org.itmo.mse.ui;

import static org.itmo.mse.constants.Proportions.backpackHeight;
import static org.itmo.mse.constants.Proportions.playerBlockHeight;
import static org.itmo.mse.constants.Proportions.playerBlockWidth;
import static org.itmo.mse.ui.Printer.getSize;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.objects.Backpack;


public class BackpackPrinter {
    private final TextGraphics textGraphics;
    
    private final List<TerminalRectangle> backpackCells = new ArrayList<>();
    private final int width;
    private final int height;
    
    public BackpackPrinter(TextGraphics graphics) throws IOException {
        textGraphics = graphics;
        width = (int) (getSize().getColumns() * playerBlockWidth);
        height = (int) (getSize().getRows() * backpackHeight) - 1;
    }
    
    public void printBackpack(Game game, int column) throws IOException {
        int row = (int) (getSize().getRows() * playerBlockHeight + 1);
        textGraphics.drawRectangle(new TerminalPosition(column, row),
                                   new TerminalSize(width, height), SpecialCharacters.DELIMITER);
        printBackpackCells(game, column, row);
        printBackpackItems(game);
    }
    
    private void printBackpackCells(Game game, int column, int row) throws IOException {
        int itemHeight = (int) (height * Proportions.itemHeight);
        int itemWidth = (int) (width * Proportions.itemWidth) + 1;
        int itemColumn = column;
        Backpack backpack = game.getPlayer().getBackpack();
        for (int i = 0; i < backpack.size(); i++) {
            backpackCells.add(new TerminalRectangle(itemColumn, row, itemWidth, itemHeight));
            textGraphics.drawRectangle(new TerminalPosition(itemColumn, row),
                                       new TerminalSize(itemWidth, itemHeight),
                                       SpecialCharacters.DELIMITER);
            if (itemColumn + itemWidth < getSize().getColumns()) {
                itemColumn += itemWidth;
            } else {
                itemColumn = column;
                row += itemHeight;
            }
        }
    }
    
    private void printBackpackItems(Game game) {
        Backpack backpack = game.getPlayer().getBackpack();
        for (int i = 0; i < backpack.size(); i++) {
            TerminalRectangle item = backpackCells.get(i);
            TerminalPosition position = item.position.withRelativeColumn(item.width / 2 - 1)
                                                     .withRelativeRow(item.height / 2);
            textGraphics.drawLine(position, position, backpack.get(i).getCharacter());
        }
    }
}
