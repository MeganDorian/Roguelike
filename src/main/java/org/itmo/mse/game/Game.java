package org.itmo.mse.game;

import static org.itmo.mse.constants.ItemCharacteristic.USUAL;
import static org.itmo.mse.constants.Proportions.backpackSize;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.ObjectEffect;
import org.itmo.mse.constants.ObjectNames;
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
    
    /**
     * Applies selected item in the backpack
     */
    public void applySelectedItem() {
        int selectedItem = player.getBackpack().getSelectedItem();
        Item item = player.getBackpack().get(selectedItem);
        switch (item.getItemType()) {
            case MEDICAL_AID:
                applyMedicalAid(item, selectedItem);
                break;
            case ARMOR:
                applyArmor(item, selectedItem);
                break;
            case WEAPON:
                applyWeapon(item, selectedItem);
                break;
        }
        player.getBackpack().setSelectedItem(Math.max(0, selectedItem - 1));
    }
    
    /**
     * Applies medical aid selected in the backpack according to the medical aid type
     */
    private void applyMedicalAid(Item item, int selectedItem) {
        int addHealth = (int) (player.getMaxHealth() *
                               (item.getItemCharacteristic() == USUAL ? ObjectEffect.usualAid :
                                ObjectEffect.legendaryAid)) + player.getHealth();
        player.setHealth(Math.min(addHealth, player.getMaxHealth()));
        player.getBackpack().getItems().remove(item);
    }
    
    
    /**
     * If equipped armor class == armor to put on class, then armor will be stacked with the
     * equipped one <br> otherwise equipped armor will be stored in the backpack and replaced
     * with the armor to put
     */
    private void applyArmor(Item item, int selectedItem) {
        int addArmor;
        Item equippedArmor = player.getArmor();
        if (item.getItemClass() == equippedArmor.getItemClass()) {
            addArmor = equippedArmor.getValue() + increaseValueBasedOnItemClass(item);
            equippedArmor.setValue(addArmor);
            player.getBackpack().getItems().remove(item);
            player.setArmor(equippedArmor);
        } else {
            player.getBackpack().getItems().remove(item);
            if (!equippedArmor.getName().equals(ObjectNames.noArmor)) {
                player.getBackpack().getItems().add(selectedItem, equippedArmor);
            }
            player.setArmor(item);
        }
    }
    
    /**
     * If equipped weapon class == weapon to put on class, then weapon will be stacked with the
     * equipped one <br> otherwise equipped weapon will be stored in the backpack (if it's not the
     * empty player hands) and replaced with the weapon to put
     */
    private void applyWeapon(Item item, int selectedItem) {
        int addWeapon;
        Item equippedWeapon = player.getWeapon();
        if (item.getItemClass() == equippedWeapon.getItemClass()) {
            addWeapon = increaseValueBasedOnItemClass(item);
            if (!equippedWeapon.getName().equals(ObjectNames.emptyHands)) {
                addWeapon += equippedWeapon.getValue();
            }
            player.getBackpack().getItems().remove(item);
            player.getWeapon().setValue(addWeapon);
        } else {
            player.getBackpack().getItems().remove(item);
            if (!equippedWeapon.getName().equals(ObjectNames.emptyHands)) {
                player.getBackpack().getItems().add(selectedItem, equippedWeapon);
            }
            player.setWeapon(item);
        }
    }
    
    private int increaseValueBasedOnItemClass(Item item) {
        return switch (item.getItemClass()) {
            case LIGHT -> ObjectEffect.light;
            case MEDIUM -> ObjectEffect.medium;
            case HEAVY -> ObjectEffect.heavy;
        };
    }
}
