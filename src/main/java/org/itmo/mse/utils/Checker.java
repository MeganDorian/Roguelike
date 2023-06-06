package org.itmo.mse.utils;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.map.Wall;

/**
 * Class for business logic checks
 */
@UtilityClass
public class Checker {
    
    public Optional<? extends Object> isObjectAtPosition(TerminalRectangle position,
                                                         List<? extends Object> objects) {
        return objects.stream().filter(item -> checkIsIntersect(item, position)).findFirst();
    }
    
    public boolean isWallNearby(TerminalRectangle position, List<Wall> walls) {
        return walls.stream().anyMatch(wall -> Checker.checkIsIntersect(wall, position));
    }
    
    /**
     * Check if position is "inside" the object
     */
    public boolean checkIsIntersect(Object object, TerminalRectangle position) {
        TerminalRectangle wallPosition = object.getPosition();
        return position.x >= wallPosition.x && position.x <= wallPosition.xAndWidth - 1 &&
               position.y >= wallPosition.y && position.y <= wallPosition.yAndHeight - 1;
    }
    
    /**
     * Returns next position according to the direction
     */
    public TerminalRectangle getNextPosition(KeyType direction, TerminalRectangle position) {
        return switch (direction) {
            case ArrowUp ->
                new TerminalRectangle(position.x, position.y - 1, position.width, position.height);
            case ArrowDown ->
                new TerminalRectangle(position.x, position.y + 1, position.width, position.height);
            case ArrowLeft ->
                new TerminalRectangle(position.x - 1, position.y, position.width, position.height);
            default ->
                new TerminalRectangle(position.x + 1, position.y, position.width, position.height);
        };
    }
}
