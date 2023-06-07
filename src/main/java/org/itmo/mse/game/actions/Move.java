package org.itmo.mse.game.actions;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;
import org.itmo.mse.constants.Direction;

public class Move implements Action {
    
    private Direction direction;
    
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (!game.isBackpackOpened()) {
            TerminalRectangle oldPosition = game.getPlayer().getPosition();
            graphics.drawRectangle(oldPosition.position, oldPosition.size, ' ');
            return game.updatePlayerPosition(direction, graphics);
        } else {
            game.setSelectedItemInBackpack(direction);
            if (game.getPlayer().getBackpack().size() != 0) {
                return game.getPlayer().getBackpack()
                           .get(game.getPlayer().getBackpack().getSelectedItemIndex()).getInfo();
            } else {
                game.setBackpackOpened(false);
                return List.of();
            }
        }
    }
    
    public void setDirection(KeyType pressedKey) {
        switch (pressedKey) {
            case ArrowUp -> direction = Direction.UP;
            case ArrowDown -> direction=Direction.DOWN;
            case ArrowLeft -> direction=Direction.LEFT;
            case ArrowRight -> direction=Direction.RIGHT;
        }
    }
}
