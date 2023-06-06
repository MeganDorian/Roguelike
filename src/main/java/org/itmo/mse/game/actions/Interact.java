package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;

public class Interact implements Action {
    @Override
    public List<String> execute(TextGraphics graphics) {
        if (!game.isBackpackOpened()) {
            return game.pickupItem();
        } else {
            return List.of();
        }
    }
}
