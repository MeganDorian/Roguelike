package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.game.objects.map.Wall;
import org.itmo.mse.utils.Checker;

import java.util.List;

public class ShyMobStrategy implements MobStrategy {
    @Override
    public TerminalRectangle execute(TerminalRectangle mobVision, TerminalRectangle mobPosition,
                                     TerminalRectangle playerPosition, List<Wall> walls) {
        if (isPlayerInVisionRange(mobVision, mobPosition, playerPosition, walls)) {
            Direction direction = Checker.getObjectsRelativePosition(playerPosition, mobPosition);
            Direction nextDirection = getRandomDirection(direction, walls, mobPosition);
            return Checker.getNextPosition(nextDirection, mobPosition);
        }
        return mobPosition;
    }
}
