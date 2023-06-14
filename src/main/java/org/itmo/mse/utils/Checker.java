package org.itmo.mse.utils;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import lombok.experimental.UtilityClass;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.map.Wall;

import java.util.List;
import java.util.Optional;

/**
 * Class for business logic checks
 */
@UtilityClass
public class Checker {
    
    /**
     * Checks whether there object (mob, item) at the position
     *
     * @return object at this position if found
     */
    public Optional<? extends Object> isObjectAtPosition(TerminalRectangle position, List<? extends Object> objects) {
        return objects.stream().filter(item -> checkIsIntersect(item, position)).findFirst();
    }
    
    /**
     * Returns true if there wall at the position
     */
    public boolean isWallAtPosition(TerminalRectangle position, List<Wall> walls) {
        return walls.stream().anyMatch(wall -> Checker.checkIsIntersect(wall, position));
    }
    
    /**
     * Returns true if position is "inside" the object
     */
    public boolean checkIsIntersect(Object object, TerminalRectangle position) {
        TerminalRectangle objectPosition = object.getPosition();
        return isBetween(objectPosition.x, position.x, objectPosition.xAndWidth) && isBetween(objectPosition.y,
                                                                                              position.y,
                                                                                              objectPosition.yAndHeight);
    }
    
    /**
     * Returns true if value between left and right coordinates
     */
    public boolean isBetween(int left, int value, int right) {
        return left <= value && right > value;
    }
    
    /**
     * Returns next position according to the direction
     */
    public TerminalRectangle getNextPosition(Direction direction, TerminalRectangle position) {
        return switch (direction) {
            case UP -> new TerminalRectangle(position.x, position.y - 1, position.width, position.height);
            case DOWN -> new TerminalRectangle(position.x, position.y + 1, position.width, position.height);
            case LEFT -> new TerminalRectangle(position.x - 1, position.y, position.width, position.height);
            case RIGHT -> new TerminalRectangle(position.x + 1, position.y, position.width, position.height);
            case UP_LEFT -> new TerminalRectangle(position.x - 1, position.y - 1, position.width, position.height);
            case UP_RIGHT -> new TerminalRectangle(position.x + 1, position.y - 1, position.width, position.height);
            case DOWN_LEFT -> new TerminalRectangle(position.x - 1, position.y + 1, position.width, position.height);
            default -> new TerminalRectangle(position.x + 1, position.y + 1, position.width, position.height);
        };
    }
    
    /**
     * Returns relative direction of the base object to the other
     */
    public Direction getObjectsRelativePosition(TerminalRectangle base, TerminalRectangle other) {
        TerminalPosition basePosition = base.position;
        TerminalPosition otherPosition = other.position;
        
        if (otherPosition.getRow() < basePosition.getRow()) {
            if (otherPosition.getColumn() < basePosition.getColumn()) {
                return Direction.UP_LEFT;
            } else if (otherPosition.getColumn() > basePosition.getColumn()) {
                return Direction.UP_RIGHT;
            } else {
                return Direction.UP;
            }
        } else if (otherPosition.getRow() > basePosition.getRow()) {
            if (otherPosition.getColumn() < basePosition.getColumn()) {
                return Direction.DOWN_LEFT;
            } else if (otherPosition.getColumn() > basePosition.getColumn()) {
                return Direction.DOWN_RIGHT;
            } else {
                return Direction.DOWN;
            }
        } else {
            if (otherPosition.getColumn() < basePosition.getColumn()) {
                return Direction.LEFT;
            } else if (otherPosition.getColumn() > basePosition.getColumn()) {
                return Direction.RIGHT;
            } else {
                return Direction.EMPTY;
            }
        }
    }
    
    @FunctionalInterface
    public interface Filter {
        boolean filter(Wall wall);
    }
    
    /**
     * <a href=https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/>
     *
     * @return if two object have intersection
     */
    public boolean isIntersect(TerminalPosition p1, TerminalPosition q1, TerminalPosition p2, TerminalPosition q2) {
        
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }
        
        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }
        
        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }
        
        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(p2, q1, q2);// Doesn't fall in any of the above cases
    }
    
    private int orientation(TerminalPosition p, TerminalPosition q, TerminalPosition r) {
        int val = (q.getRow() - p.getRow()) * (r.getColumn() - q.getColumn()) -
                  (q.getColumn() - p.getColumn()) * (r.getRow() - q.getRow());
        
        if (val == 0) {
            return 0;
        }
        
        return (val > 0) ? 1 : 2;
    }
    
    private boolean onSegment(TerminalPosition p, TerminalPosition q, TerminalPosition r) {
        return q.getColumn() <= Math.max(p.getColumn(), r.getColumn()) &&
               q.getColumn() >= Math.min(p.getColumn(),
                                         r.getColumn()) &&
               q.getRow() <= Math.max(p.getRow(), r.getRow()) &&
               q.getRow() >= Math.min(p.getRow(), r.getRow());
    }
}
