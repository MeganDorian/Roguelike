package org.itmo.mse.ui;

import static org.itmo.mse.constants.Proportions.backpackHeight;
import static org.itmo.mse.constants.Proportions.backpackSize;
import static org.itmo.mse.constants.Proportions.playerBlockHeight;
import static org.itmo.mse.constants.Proportions.playerBlockWidth;
import static org.itmo.mse.ui.Printer.getSize;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.Game;
import org.itmo.mse.game.objects.Backpack;


public class BackpackPrinter {
    private final TextGraphics textGraphics;
    
    private final List<TerminalRectangle> backpackCells = new ArrayList<>();
    private int width;
    private int height;
    
    private int column;
    
    private int row;
    
    @Getter
    private TerminalRectangle infoBlock;
    
    private int itemHeight;
    private int itemWidth;
    
    @Getter
    private int backpackItemsCountInRow = 0;
    
    public BackpackPrinter(TextGraphics graphics) throws IOException {
        textGraphics = graphics;
        width = (int) (getSize().getColumns() * playerBlockWidth);
        height = (int) (getSize().getRows() * backpackHeight) - 1;
        column = (int) (getSize().getColumns() * Proportions.mapWidth);
        row = (int) (getSize().getRows() * playerBlockHeight + 1);
        itemHeight = (int) (height * Proportions.itemHeight);
        itemWidth = (int) (width * Proportions.itemWidth) + 1;
        infoBlock =
            new TerminalRectangle(column, row + itemHeight * 3, width, height - 3 * itemHeight);
    }
    
    /**
     * Prints the backpack together with its contents
     *
     * @param game
     * @throws IOException
     */
    public void printBackpack(Game game) throws IOException {
        textGraphics.drawRectangle(new TerminalPosition(column, row),
                                   new TerminalSize(width, height), SpecialCharacters.DELIMITER);
        printBackpackCells(game);
    }
    
    /**
     * Lights up the selected item in the backpack
     *
     * @param item
     * @param color
     */
    public void printSelectBackpackItem(int item, TextCharacter color) {
        textGraphics.fillRectangle(new TerminalPosition(column, row),
                                   new TerminalSize(width,
                                                    height - infoBlock.height),
                                   SpecialCharacters.SPACE);
        TerminalRectangle selectedItem = backpackCells.get(item);
        textGraphics.fillRectangle(selectedItem.position, selectedItem.size, color);
    }
    
    /**
     * Prints the backpack cells
     * @param game
     * @throws IOException
     */
    private void printBackpackCells(Game game) throws IOException {
        int itemColumn = column;
        int currRow = row;
        for (int i = 0; i < backpackSize; i++) {
            if (backpackCells.size() < backpackSize) {
                backpackCells.add(
                    new TerminalRectangle(itemColumn, currRow, itemWidth, itemHeight));
            }
            textGraphics.drawRectangle(new TerminalPosition(itemColumn, currRow),
                                       new TerminalSize(itemWidth, itemHeight),
                                       SpecialCharacters.DELIMITER);
            if (itemColumn + itemWidth < getSize().getColumns()) {
                itemColumn += itemWidth;
                backpackItemsCountInRow++;
            } else {
                itemColumn = column;
                currRow += itemHeight;
                backpackItemsCountInRow = backpackSize / backpackItemsCountInRow;
            }
        }
        printBackpackItems(game);
    }
    
    /**
     * Prints the items in the backpack
     *
     * @param game
     */
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
