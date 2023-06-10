package org.itmo.mse.game;

import static org.itmo.mse.constants.ItemCharacteristic.USUAL;
import static org.itmo.mse.constants.Proportions.backpackSize;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.Direction;
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
    
    public Game(Game state) {
        this.objectUnderPlayer = state.objectUnderPlayer;
        this.mobUnderPlayer = state.mobUnderPlayer;
        this.dungeonLevel = state.dungeonLevel;
        this.isBackpackOpened = state.isBackpackOpened;
        this.backpackItemsInRow = state.backpackItemsInRow;
        this.levelMap = state.levelMap;
        this.player = state.player;
    }
    
    /**
     * Changes and redraws player position after he moves
     *
     * @param direction to move
     */
    public List<String> updatePlayerPosition(Direction direction, TextGraphics graphics) {
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
            dungeonLevel++;
            return null;
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
    
    /**
     * Picks up item from map and puts it in backpack
     * @return information about item or notification of overfilling the backpack
     */
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
    
    /**
     * Changes the selected item in the backpack according to the pressed by player arrow button
     * (up, down, left or right)
     */
    public void setSelectedItemInBackpack(Direction direction) {
        int selectedItem = player.getBackpack().getSelectedItemIndex();
        if (direction == Direction.RIGHT && selectedItem + 1 < player.getBackpack().size()) {
            selectedItem++;
        } else if (direction == Direction.LEFT && selectedItem != 0) {
            selectedItem--;
        } else if (direction == Direction.UP && selectedItem - backpackItemsInRow >= 0) {
            selectedItem -= backpackItemsInRow;
        } else if (direction == Direction.DOWN &&
                   selectedItem + backpackItemsInRow < player.getBackpack().size()) {
            selectedItem += backpackItemsInRow;
        }
        player.getBackpack().setSelectedItemIndex(selectedItem);
    }
    
    /**
     * Applies selected item in the backpack
     */
    public void applySelectedItem() {
        int selectedItem = player.getBackpack().getSelectedItemIndex();
        Item item = player.getBackpack().get(selectedItem);
        switch (item.getItemType()) {
            case MEDICAL_AID -> applyMedicalAid(item);
            case ARMOR -> applyArmor(item, selectedItem);
            case WEAPON -> applyWeapon(item, selectedItem);
        }
        player.getBackpack().setSelectedItemIndex(Math.max(0, selectedItem - 1));
        if(player.getBackpack().size() == 0) {
            isBackpackOpened = false;
        }
    }
    
    /**
     * Applies medical aid selected in the backpack according to the medical aid type
     */
    private void applyMedicalAid(Item item) {
        int addHealth = (int) (player.getMaxHealth() *
                               (item.getItemCharacteristic() == USUAL ? ObjectEffect.usualAid :
                                ObjectEffect.legendaryAid)) + player.getHealth();
        player.setHealth(Math.min(addHealth, player.getMaxHealth()));
        player.getBackpack().getItems().remove(item);
    }
    
    
    /**
     * If equipped armor class == armor to put on class, then armor will be stacked with the
     * equipped one <br> otherwise equipped armor will be stored in the backpack and replaced with
     * the armor to put
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
    
    /**
     * Performs dropping the item on the floor. Get the current selected item in the backpack and
     * searches for the free place near the player
     */
    public void dropItemFromBackpack() {
        int selectedItem = player.getBackpack().getSelectedItemIndex();
        Item item = player.getBackpack().get(selectedItem);
        player.getBackpack().getItems().remove(item);
        TerminalRectangle freePosition = getNearestFreePlace().get();
        item.setPosition(freePosition);
        if (freePosition.equals(player.getPosition())) {
            objectUnderPlayer = item;
        }
        levelMap.getItems().add(item);
        player.getBackpack().setSelectedItemIndex(Math.max(0, selectedItem - 1));
        if(player.getBackpack().size() == 0) {
            isBackpackOpened = false;
        }
    }
    
    /**
     * Searches for the nearest free position around the player. If there is no free place, return
     * player position
     *
     * @return the first free position where no wall, no item or mob placed
     */
    private Optional<TerminalRectangle> getNearestFreePlace() {
        TerminalRectangle playerPosition = player.getPosition();
        return Stream.of(playerPosition,
                         new TerminalRectangle(playerPosition.x + 1, playerPosition.y,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x - 1, playerPosition.y,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x, playerPosition.y - 1,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x, playerPosition.y + 1,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x + 1, playerPosition.y + 1,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x + 1, playerPosition.y - 1,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x - 1, playerPosition.y + 1,
                                               playerPosition.width, playerPosition.height),
                         new TerminalRectangle(playerPosition.x - 1, playerPosition.y - 1,
                                               playerPosition.width, playerPosition.height)).filter(
                         position -> !Checker.isWallNearby(position, levelMap.getWalls()) &&
                                     Checker.isObjectAtPosition(position, levelMap.getItems()).isEmpty() &&
                                     Checker.isObjectAtPosition(position, levelMap.getMobs()).isEmpty())
                     .findFirst().or(() -> Optional.of(playerPosition));
    }
    
}
