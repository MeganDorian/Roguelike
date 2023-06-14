package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.List;

public class Drop implements Action {
    
    /**
     * Drop item from backpack
     */
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (game.isBackpackOpened()) {
            if (game.getPlayer().getBackpack().size() != 0) {
                game.dropItemFromBackpack();
            } else {
                game.setBackpackOpened(false);
            }
        }
        return List.of();
    }
}
