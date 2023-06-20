package org.itmo.mse.game.actions;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.utils.Checker;

import java.util.List;

public class Move implements Action {
    
    private Direction direction;
    
    /**
     * Move the player around the map or move around the backpack cells
     */
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (!game.isBackpackOpened()) {
            TerminalRectangle oldPosition = game.getPlayerPosition();
            List<String> updateResult = game.updatePlayerPosition(direction, graphics);
            if (!game.getPlayerPosition().equals(oldPosition) && Checker.isObjectAtPosition(oldPosition,
                                                                                            game.getItems())
                                                                        .isEmpty()) {
                graphics.drawRectangle(oldPosition.position, oldPosition.size, ' ');
            }
            return updateResult;
        } else {
            game.setSelectedItemInBackpack(direction);
            if (game.getPlayer().getBackpackSize() != 0) {
                return game.getPlayer().getSelectedInBackpackItem().getInfo();
            } else {
                game.setBackpackOpened(false);
                return List.of();
            }
        }
    }
    
    /**
     * Set direction
     *
     * @param pressedKey -- button pressed by the user
     */
    public void setDirection(KeyType pressedKey) {
        switch (pressedKey) {
            case ArrowUp -> direction = Direction.UP;
            case ArrowDown -> direction = Direction.DOWN;
            case ArrowLeft -> direction = Direction.LEFT;
            case ArrowRight -> direction = Direction.RIGHT;
        }
    }
}
