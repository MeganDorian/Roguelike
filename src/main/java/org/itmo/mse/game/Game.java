package org.itmo.mse.game;

import static org.itmo.mse.constants.Proportions.backpackSize;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
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
import org.itmo.mse.utils.Checker;

@Getter
public class Game {
    private Item objectUnderPlayer;
    
    private Mob mobUnderPlayer;
    
    private int dungeonLevel = 1;
    
    @Setter
    private boolean isBackpackOpened = false;
    
    @Setter
    private int backpackItemsInRow = 3;
    
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
    public List<String> updatePlayerPosition(KeyType direction, TextGraphics graphics) {
        TerminalRectangle playerPosition = player.getPosition();
        TerminalRectangle newPosition = Checker.getNextPosition(direction, playerPosition);
        if (Checker.isWallNearby(newPosition, levelMap.getWalls())) {
            newPosition = playerPosition;
        }
        
        List<String> info = playerOnItem(newPosition, graphics);
        if (info.isEmpty()) {
            info = playerOnMob(newPosition, graphics);
        }
        
        // is newPosition == map start
        if (newPosition.position.equals(levelMap.getStart())) {
            newPosition = playerPosition;
        }
        
        if (newPosition.position.equals(levelMap.getExit())) {
            // TODO level ended action
            dungeonLevel++;
            newPosition = playerPosition;
        }
        
        player.setPosition(newPosition);
        return info;
    }
    
    /**
     * Proceeds if player will stand on the item we will save it
     */
    private List<String> playerOnItem(TerminalRectangle newPosition, TextGraphics graphics) {
        // if player will stand on the item save this item
        Optional<? extends Object> itemOnNewPosition =
            Checker.isObjectAtPosition(newPosition, levelMap.getItems());
        if (itemOnNewPosition.isPresent()) {
            objectUnderPlayer = (Item) itemOnNewPosition.get();
            return itemOnNewPosition.get().getInfo();
        } else if (objectUnderPlayer != null) {
            objectUnderPlayer.print(graphics);
            objectUnderPlayer = null;
        }
        return List.of();
    }
    
    /**
     * Proceeds if player will stand on the mob we will save it
     */
    private List<String> playerOnMob(TerminalRectangle newPosition, TextGraphics graphics) {
        // if player will stand on the item save this item
        Optional<? extends Object> itemOnNewPosition =
            Checker.isObjectAtPosition(newPosition, levelMap.getMobs());
        if (itemOnNewPosition.isPresent()) {
            mobUnderPlayer = (Mob) itemOnNewPosition.get();
            return itemOnNewPosition.get().getInfo();
        } else if (mobUnderPlayer != null) {
            mobUnderPlayer.print(graphics);
            mobUnderPlayer = null;
        }
        return List.of();
    }
    
    public List<String> pickupItem() {
        if (objectUnderPlayer != null) {
            // store it in backpack if its enough space in it
            if (player.getBackpack().size() == backpackSize) {
                return List.of("Your backpack is full!");
            }
            
            levelMap.getItems().remove(objectUnderPlayer);
            player.getBackpack().getItems().add(objectUnderPlayer);
            List<String> pickedItemInfo = new ArrayList<>();
            pickedItemInfo.add("You picked up :");
            pickedItemInfo.addAll(objectUnderPlayer.getInfo());
            objectUnderPlayer = null;
            return pickedItemInfo;
        }
        return List.of();
    }
    
    public void setSelectedItemInBackpack(KeyType direction) {
        int selectedItem = player.getBackpack().getSelectedItem();
        if (direction == KeyType.ArrowRight && selectedItem + 1 < player.getBackpack().size()) {
            selectedItem++;
        } else if (direction == KeyType.ArrowLeft && selectedItem != 0) {
            selectedItem--;
        } else if (direction == KeyType.ArrowUp && selectedItem - backpackItemsInRow >= 0) {
            selectedItem -= backpackItemsInRow;
        } else if (direction == KeyType.ArrowDown &&
                   selectedItem + backpackItemsInRow < player.getBackpack().size()) {
            selectedItem += backpackItemsInRow;
        }
        player.getBackpack().setSelectedItem(selectedItem);
    }
}
