package org.itmo.mse.game;

import static org.itmo.mse.constants.Proportions.backpackSize;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.game.objects.Mob;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;
import org.itmo.mse.ui.Printer;

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
        
        // is there item nearby
        Optional<Item> itemOnNewPosition = isItemNearby(newPosition);
        if (itemOnNewPosition.isPresent()) {
            newPosition = playerPosition;
            info = itemOnNewPosition.get().getInfo();
        }
        
        // is newPosition == map start
        if (isStart(newPosition)) {
            newPosition = playerPosition;
        }
        
        if (isExit(newPosition)) {
            // TODO level ended action
            dungeonLevel++;
            newPosition = playerPosition;
        }
        
        player.setPosition(newPosition);
        return info;
    }
    
    private boolean isStart(TerminalRectangle position) {
        return position.position.equals(levelMap.getStart());
    }
    
    private boolean isExit(TerminalRectangle position) {
        return position.position.equals(levelMap.getExit());
    }
    
    private boolean isWallNearby(TerminalRectangle position) {
        return levelMap.getWalls().stream().anyMatch(wall -> checkIsIntersect(wall, position));
    }
    
    private Optional<Mob> isMobNearby(TerminalRectangle position) {
        return levelMap.getMobs().stream().filter(mob -> checkIsIntersect(mob, position))
                       .findFirst();
    }
    
    public List<String> pickUpItemNearby() throws IOException {
        Optional<Item> nearestItem = getNearestItem(player.getPosition());
        if (nearestItem.isPresent()) {
            // store it in backpack if its enough space in it
            if (player.getBackpack().size() == backpackSize) {
                return List.of("Your backpack is full!");
            }
            
            Item item = nearestItem.get();
            levelMap.getItems().remove(item);
            player.getBackpack().getItems().add(item);
            Printer.eraseAtPosition(item.getPosition().position, 1);
            List<String> pickedItemInfo = new ArrayList<>();
            pickedItemInfo.add("You picked up :");
            pickedItemInfo.addAll(item.getInfo());
            return pickedItemInfo;
        }
        return List.of();
    }
    
    private Optional<Item> getNearestItem(TerminalRectangle position) {
        return levelMap.getItems().stream().filter(item -> {
            TerminalRectangle right =
                new TerminalRectangle(position.x + 1, position.y, position.width, position.height);
            TerminalRectangle left =
                new TerminalRectangle(position.x - 1, position.y, position.width, position.height);
            TerminalRectangle down =
                new TerminalRectangle(position.x, position.y + 1, position.width, position.height);
            TerminalRectangle up =
                new TerminalRectangle(position.x, position.y - 1, position.width, position.height);
            TerminalRectangle rightDown =
                new TerminalRectangle(position.x + 1, position.y + 1, position.width,
                                      position.height);
            TerminalRectangle rightUp =
                new TerminalRectangle(position.x + 1, position.y - 1, position.width,
                                      position.height);
            TerminalRectangle leftDown =
                new TerminalRectangle(position.x - 1, position.y + 1, position.width,
                                      position.height);
            TerminalRectangle leftUp =
                new TerminalRectangle(position.x - 1, position.y - 1, position.width,
                                      position.height);
            return checkIsIntersect(item, right) || checkIsIntersect(item, down) ||
                   checkIsIntersect(item, left) || checkIsIntersect(item, up) ||
                   checkIsIntersect(item, rightDown) || checkIsIntersect(item, rightUp) ||
                   checkIsIntersect(item, leftDown) || checkIsIntersect(item, leftUp);
        }).findFirst();
    }
    
    private Optional<Item> isItemNearby(TerminalRectangle position) {
        return levelMap.getItems().stream().filter(item -> checkIsIntersect(item, position))
                       .findFirst();
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
