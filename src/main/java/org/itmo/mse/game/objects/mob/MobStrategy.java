package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.game.objects.map.Wall;
import org.itmo.mse.utils.Checker;

import java.util.List;

public interface MobStrategy {
    
    TerminalRectangle execute(TerminalRectangle mobVision, TerminalRectangle mobPosition,
                              TerminalRectangle playerPosition, List<Wall> walls);
    
    default boolean isPlayerInVisionRange(TerminalRectangle mobVision, TerminalRectangle mobPosition,
                                          TerminalRectangle playerPosition, List<Wall> walls) {
        return Checker.isBetween(mobVision.x, playerPosition.x, mobVision.xAndWidth) &&
               Checker.isBetween(mobVision.y,
                                 playerPosition.y,
                                 mobVision.yAndHeight) &&
               !isPlayerBehindTheWall(mobPosition, playerPosition, walls);
    }
    
    default boolean isPlayerBehindTheWall(TerminalRectangle mobPosition, TerminalRectangle playerPosition,
                                          List<Wall> walls) {
        TerminalRectangle zone = new TerminalRectangle(Math.min(playerPosition.x, mobPosition.x),
                                                       Math.min(playerPosition.y, mobPosition.y),
                                                       Math.abs(playerPosition.x - mobPosition.x) + 1,
                                                       Math.abs(playerPosition.y - mobPosition.y) + 1);
        
        Checker.Filter isWallInZone =
                (Wall wall) -> wall.getPosition().x <= zone.xAndWidth && wall.getPosition().y <= zone.yAndHeight;
        List<Wall> list = walls.stream().filter(wall -> {
            TerminalRectangle position = wall.getPosition();
            return isWallInZone.filter(wall) && Checker.isIntersect(position.position,
                                                                    position.position.withRelativeColumn(position.width)
                                                                                     .withRelativeRow(position.height),
                                                                    mobPosition.position,
                                                                    playerPosition.position);
        }).toList();
        return walls.stream().anyMatch(wall -> {
            TerminalRectangle position = wall.getPosition();
            return isWallInZone.filter(wall) && Checker.isIntersect(position.position,
                                                                    position.position.withRelativeColumn(position.width)
                                                                                     .withRelativeRow(position.height),
                                                                    mobPosition.position,
                                                                    playerPosition.position);
        });
    }
}
