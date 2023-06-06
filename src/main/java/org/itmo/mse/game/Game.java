package org.itmo.mse.game;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.game.objects.Mob;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;

@Getter
public class Game {
    
    private int dungeonLevel = 1;
    
    @Setter
    private Map levelMap;
    
    @Setter
    private Player player;
    
    public Game() {
        player = new Player(new TerminalRectangle(0, 0, 0, 0));
    }
    
    /**
     * Changes and redraws player position after he moves
     *
     * @param direction to move
     */
    public List<String> updatePlayerPosition(KeyType direction) {
        List<String> info = List.of();
        TerminalRectangle playerPosition = player.getPosition();
        TerminalRectangle newPosition = getNextPosition(direction, playerPosition);
        if (isWallNearby(newPosition)) {
            newPosition = playerPosition;
        }
        
        Optional<Item> itemOnNewPosition = isItemNearby(newPosition);
        if (itemOnNewPosition.isPresent()) {
            newPosition = playerPosition;
            info = itemOnNewPosition.get().getInfo();
        }
        
        player.setPosition(newPosition);
        return info;
    }
    
    private boolean isWallNearby(TerminalRectangle position) {
        return levelMap.getWalls().stream().anyMatch(wall -> checkIsIntersect(wall, position));
    }
    
    private Optional<Mob> isMobNearby(TerminalRectangle position) {
        return levelMap.getMobs().stream().filter(mob -> checkIsIntersect(mob, position))
                       .findFirst();
    }
    
    private Optional<Item> isItemNearby(TerminalRectangle position) {
        return levelMap.getItems().stream().filter(item -> checkIsIntersect(item, position))
                       .findFirst();
//        return levelMap.getItems().stream().filter(item -> {
//            TerminalRectangle right =
//                new TerminalRectangle(position.x + 1, position.y, position.width, position.height);
//            TerminalRectangle left =
//                new TerminalRectangle(position.x - 1, position.y, position.width, position.height);
//            TerminalRectangle down =
//                new TerminalRectangle(position.x, position.y + 1, position.width, position.height);
//            TerminalRectangle up =
//                new TerminalRectangle(position.x, position.y - 1, position.width, position.height);
//            TerminalRectangle rightDown =
//                new TerminalRectangle(position.x + 1, position.y + 1, position.width,
//                                      position.height);
//            TerminalRectangle rightUp =
//                new TerminalRectangle(position.x + 1, position.y - 1, position.width,
//                                      position.height);
//            TerminalRectangle leftDown =
//                new TerminalRectangle(position.x - 1, position.y + 1, position.width,
//                                      position.height);
//            TerminalRectangle leftUp =
//                new TerminalRectangle(position.x - 1, position.y - 1, position.width,
//                                      position.height);
//            return checkIsIntersect(item, right) || checkIsIntersect(item, down) ||
//                   checkIsIntersect(item, left) || checkIsIntersect(item, up) ||
//                   checkIsIntersect(item, rightDown) || checkIsIntersect(item, rightUp) ||
//                   checkIsIntersect(item, leftDown) || checkIsIntersect(item, leftUp);
//        }).findFirst();
    }
    
    private boolean checkIsIntersect(Object object, TerminalRectangle position) {
        TerminalRectangle wallPosition = object.getPosition();
        return position.x >= wallPosition.x && position.x <= wallPosition.xAndWidth - 1 &&
               position.y >= wallPosition.y && position.y <= wallPosition.yAndHeight - 1;
    }
    
    /**
     * Returns next position according to the direction
     */
    private TerminalRectangle getNextPosition(KeyType direction, TerminalRectangle position) {
        switch (direction) {
            case ArrowUp:
                return new TerminalRectangle(position.x, position.y - 1, position.width,
                                             position.height);
            case ArrowDown:
                return new TerminalRectangle(position.x, position.y + 1, position.width,
                                             position.height);
            case ArrowLeft:
                return new TerminalRectangle(position.x - 1, position.y, position.width,
                                             position.height);
            default:
                return new TerminalRectangle(position.x + 1, position.y, position.width,
                                             position.height);
        }
    }
}
