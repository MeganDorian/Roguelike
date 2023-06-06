package org.itmo.mse.game.actions;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;
import lombok.Setter;

public class Move implements Action {
    @Setter
    private KeyType direction;
    
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
                           .get(game.getPlayer().getBackpack().getSelectedItem()).getInfo();
            } else {
                game.setBackpackOpened(false);
                return List.of();
            }
        }
    }
}
