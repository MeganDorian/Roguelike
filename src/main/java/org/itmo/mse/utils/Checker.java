package org.itmo.mse.utils;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.map.Wall;

/**
 * Class for business logic checks
 */
@UtilityClass
public class Checker {
    
    /**
     * Checks whether there object (mob, item) at the position
     *
     * @return object at this position * if found
     */
    public Optional<? extends Object> isObjectAtPosition(TerminalRectangle position,
                                                         List<? extends Object> objects) {
        return objects.stream().filter(item -> checkIsIntersect(item, position)).findFirst();
    }
    
    /**
     * Checks is there wall at the position
     *
     * @return true if there wall
     */
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
    public TerminalRectangle getNextPosition(Direction direction, TerminalRectangle position) {
        return switch (direction) {
            case UP ->
                new TerminalRectangle(position.x, position.y - 1, position.width, position.height);
            case DOWN ->
                new TerminalRectangle(position.x, position.y + 1, position.width, position.height);
            case LEFT ->
                new TerminalRectangle(position.x - 1, position.y, position.width, position.height);
            default ->
                new TerminalRectangle(position.x + 1, position.y, position.width, position.height);
        };
    }
}
