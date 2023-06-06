package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;

public class BackpackAction implements Action {
    @Override
    public List<String> execute(TextGraphics graphics) {
        game.getPlayer().getBackpack().setSelectedItem(0);
        game.setBackpackOpened(!game.isBackpackOpened());
        int selectedItem = game.getPlayer().getBackpack().getSelectedItem();
        return game.isBackpackOpened() ?
               game.getPlayer().getBackpack().get(selectedItem).getInfo() : List.of();
    }
}
