package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;

public class BackpackAction implements Action {
    
    /**
     * Open/close backpack
     *
     * @param graphics
     * @return
     */
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (game.getPlayer().getBackpack().size() == 0) {
            if (!game.isBackpackOpened()) {
                return List.of("Your backpack is empty");
            }
            game.setBackpackOpened(false);
            return List.of();
        }
        game.setBackpackOpened(!game.isBackpackOpened());
        game.getPlayer().getBackpack().setSelectedItemIndex(0);
        int selectedItem = game.getPlayer().getBackpack().getSelectedItemIndex();
        return game.isBackpackOpened() ?
               game.getPlayer().getBackpack().get(selectedItem).getInfo() : List.of();
    }
}
