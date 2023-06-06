package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;

public class Interact implements Action {
    @Override
    public List<String> execute(TextGraphics graphics) throws IOException {
        return game.pickUpItemNearby();
    }
}
