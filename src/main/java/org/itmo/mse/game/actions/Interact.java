package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.ArrayList;
import java.util.List;

public class Interact implements Action {
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (!game.isBackpackOpened()) {
            return game.pickupItem();
        } else {
            List<String> info = new ArrayList<>();
            info.add("You used:");
            info.addAll(
                game.getPlayer().getBackpack().get(game.getPlayer().getBackpack().getSelectedItemIndex())
                    .getInfo());
            game.applySelectedItem();
            return info;
        }
    }
}
