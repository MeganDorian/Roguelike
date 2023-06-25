package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.List;

public class BackpackAction implements Action {
    
    /**
     * Open/close backpack
     */
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (game.getPlayer().getBackpackSize() == 0) {
            if (!game.isBackpackOpened()) {
                return List.of("Your backpack is empty");
            }
            game.setBackpackOpened(false);
            return List.of();
        }
        game.setBackpackOpened(!game.isBackpackOpened());
        game.getPlayer().setSelectedItemIndex(0);
        return game.isBackpackOpened() ? game.getPlayer().getSelectedInBackpackItem().getInfo() : List.of();
    }
}
