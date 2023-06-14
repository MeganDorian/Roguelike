package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.game.objects.map.Wall;
import org.itmo.mse.utils.Checker;

import java.util.List;

public class AggressiveMobStrategy implements MobStrategy {
    @Override
    public TerminalRectangle execute(TerminalRectangle mobVision, TerminalRectangle mobPosition,
                                     TerminalRectangle playerPosition, List<Wall> walls) {
        if (isPlayerInVisionRange(mobVision, mobPosition, playerPosition, walls)) {
            return Checker.getNextPosition(Checker.getObjectsRelativePosition(mobPosition, playerPosition),
                                           mobPosition);
        }
        return mobPosition;
    }
}
