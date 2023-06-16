package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.constants.Direction;
import org.itmo.mse.game.objects.map.Wall;
import org.itmo.mse.utils.Checker;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
        return walls.stream().anyMatch(wall -> {
            TerminalRectangle position = wall.getPosition();
            return isWallInZone.filter(wall) && Checker.isIntersect(position.position,
                                                                    position.position.withRelativeColumn(position.width)
                                                                                     .withRelativeRow(position.height),
                                                                    mobPosition.position,
                                                                    playerPosition.position);
        });
    }
    
    default List<Direction> getPossibleDirectionsToMove(Direction direction, List<Wall> walls,
                                                        TerminalRectangle mobPosition) {
        Set<Direction> possibleDirections = new HashSet<>();
        
        if (direction != Direction.DOWN && direction != Direction.DOWN_LEFT && direction != Direction.DOWN_RIGHT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.UP, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.RIGHT && direction != Direction.UP_RIGHT && direction != Direction.DOWN_RIGHT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.LEFT, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.RIGHT && direction != Direction.DOWN && direction != Direction.DOWN_RIGHT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.UP_LEFT, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.LEFT && direction != Direction.DOWN && direction != Direction.DOWN_LEFT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.UP_RIGHT, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.LEFT && direction != Direction.UP_LEFT && direction != Direction.DOWN_LEFT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.RIGHT, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.UP && direction != Direction.UP_LEFT && direction != Direction.UP_RIGHT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.DOWN, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.UP && direction != Direction.UP_RIGHT && direction != Direction.RIGHT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.DOWN_LEFT, mobPosition, walls, possibleDirections);
        }
        
        if (direction != Direction.UP && direction != Direction.UP_LEFT && direction != Direction.LEFT) {
            possibleDirections = checkPositionAndAddIfFree(Direction.DOWN_RIGHT,
                                                           mobPosition,
                                                           walls,
                                                           possibleDirections);
        }
        return possibleDirections.stream().toList();
    }
    
    default Direction getRandomDirection(Direction direction, List<Wall> walls, TerminalRectangle mobPosition) {
        List<Direction> directions = getPossibleDirectionsToMove(direction, walls, mobPosition);
        Random random = new Random();
        return directions.get(random.nextInt(directions.size()));
    }
    
    default Set<Direction> checkPositionAndAddIfFree(Direction direction, TerminalRectangle mobPosition,
                                                     List<Wall> walls, Set<Direction> directions) {
        if (!Checker.isWallAtPosition(Checker.getNextPosition(direction, mobPosition), walls)) {
            directions.add(direction);
        }
        return directions;
    }
}
