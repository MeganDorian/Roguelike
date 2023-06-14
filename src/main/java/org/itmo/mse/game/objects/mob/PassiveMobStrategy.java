package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.game.objects.map.Wall;

import java.util.List;

public class PassiveMobStrategy implements MobStrategy {
    @Override
    public TerminalRectangle execute(TerminalRectangle mobVision, TerminalRectangle mobPosition,
                                     TerminalRectangle playerPosition, List<Wall> walls) {
        // just stands on his place :(
        return mobPosition;
    }
}
