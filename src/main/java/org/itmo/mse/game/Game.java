package org.itmo.mse.game;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.*;
import org.itmo.mse.game.actions.Action;
import org.itmo.mse.game.actions.Damage;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;
import org.itmo.mse.game.objects.mob.Mob;
import org.itmo.mse.utils.Checker;

import java.util.*;
import java.util.stream.Stream;

import static org.itmo.mse.constants.ChangeNames.*;
import static org.itmo.mse.constants.ItemCharacteristic.USUAL;
import static org.itmo.mse.constants.ObjectNames.noArmor;
import static org.itmo.mse.constants.Proportions.backpackSize;

@Getter
public class Game {
    private final Timer timerForDamage;
    private Item itemUnderPlayer;
    private Mob mobUnderPlayer;
    @Setter
    private int dungeonLevel = 1;
    private long timeUnderPlayer = 0;
    private boolean isBackpackOpened = false;
    @Setter
    private int backpackItemsInRow = 3;
    @Setter
    private Map levelMap;
    @Setter
    private Player player;
    private final ArrayList<String> changes;
    
    public Game() {
        player = new Player(new TerminalRectangle(0, 0, 0, 0));
        timerForDamage = new Timer();
        Action damage = new Damage();
        timerForDamage.schedule((TimerTask) damage, 0, 1000);
        changes = new ArrayList<>();
    }
    
    public Game(Game state) {
        this.itemUnderPlayer = state.itemUnderPlayer;
        this.mobUnderPlayer = state.mobUnderPlayer;
        this.dungeonLevel = state.dungeonLevel;
        this.isBackpackOpened = state.isBackpackOpened;
        this.backpackItemsInRow = state.backpackItemsInRow;
        if (this.levelMap == null && state.levelMap != null) {
            this.levelMap = Map.builder()
                               .start(state.levelMap.getStart())
                               .exit(state.levelMap.getExit())
                               .border(state.levelMap.getPosition())
                               .walls(state.levelMap.getWalls())
                               .things(state.levelMap.getItems())
                               .mobs(state.levelMap.getMobs())
                               .build();
        }
        this.player = new Player(state.player);
        this.timeUnderPlayer = state.timeUnderPlayer;
        this.timerForDamage = state.timerForDamage;
        this.changes = new ArrayList<>();
    }
    
    /**
     * Changes and redraws player position after he moves
     *
     * @param direction to move
     */
    public List<String> updatePlayerPosition(Direction direction, TextGraphics graphics) {
        TerminalRectangle playerPosition = player.getPosition();
        TerminalRectangle newPosition = Checker.getNextPosition(direction, playerPosition);
        if (Checker.isWallAtPosition(newPosition, levelMap.getWalls())) {
            newPosition = playerPosition;
        }
        
        List<String> info = playerOnItem(newPosition, graphics);
        if (info.isEmpty()) {
            info = playerOnMob(newPosition, graphics);
        }
        
        if (levelMap != null && newPosition.position.equals(levelMap.getStart())) {
            newPosition = playerPosition;
        }
        
        if (levelMap != null && newPosition.position.equals(levelMap.getExit())) {
            return performExit();
        }
        
        if (!newPosition.equals(player.getPosition())) {
            changes.add(PLAYER_POSITION);
        }
        player.setPosition(newPosition);
        return info;
    }
    
    private List<String> performExit() {
        dungeonLevel++;
        double upMobHealthy;
        double upMobDamage;
        if (dungeonLevel % 10 == 0) {
            upMobHealthy = Proportions.upTenthMobHealthy;
            upMobDamage = Proportions.upTenthMobDamage;
        } else {
            upMobHealthy = Proportions.upMobHealthy;
            upMobDamage = Proportions.upMobDamage;
        }
        MobSpecifications.lowerMobHealthy = (int) Math.ceil(MobSpecifications.lowerMobHealthy * upMobHealthy);
        MobSpecifications.upperMobHealthy = (int) Math.ceil(MobSpecifications.upperMobHealthy * upMobHealthy);
        MobSpecifications.lowerMobDamage = (int) Math.ceil(MobSpecifications.lowerMobDamage * upMobDamage);
        MobSpecifications.upperMobDamage = (int) Math.ceil(MobSpecifications.upperMobDamage * upMobDamage);
        MobSpecifications.defaultExperienceShyMob = (int) Math.ceil(MobSpecifications.defaultExperienceShyMob *
                                                                    Proportions.upMobExperience);
        MobSpecifications.defaultExperiencePassiveMob = (int) Math.ceil(MobSpecifications.defaultExperiencePassiveMob *
                                                                        Proportions.upMobExperience);
        MobSpecifications.defaultExperienceAggressiveMob =
                (int) Math.ceil(MobSpecifications.defaultExperienceAggressiveMob * Proportions.upMobExperience);
        changes.add(DUNGEON_LEVEL);
        return List.of();
    }
    
    /**
     * Proceeds if player will stand on the item we will save it
     */
    private List<String> playerOnItem(TerminalRectangle newPosition, TextGraphics graphics) {
        // if player will stand on the item save this item
        Optional<? extends Object> itemOnNewPosition = Checker.isObjectAtPosition(newPosition, levelMap.getItems());
        if (itemOnNewPosition.isPresent()) {
            itemUnderPlayer = (Item) itemOnNewPosition.get();
            return itemOnNewPosition.get().getInfo();
        } else if (itemUnderPlayer != null) {
            itemUnderPlayer.print(graphics);
            itemUnderPlayer = null;
        }
        return List.of();
    }
    
    /**
     * Proceeds if player will stand on the mob we will save it
     */
    private List<String> playerOnMob(TerminalRectangle newPosition, TextGraphics graphics) {
        // if player will stand on the item save this item
        Optional<? extends Object> itemOnNewPosition = Checker.isObjectAtPosition(newPosition, levelMap.getMobs());
        if (itemOnNewPosition.isPresent()) {
            timeUnderPlayer = System.currentTimeMillis();
            mobUnderPlayer = (Mob) itemOnNewPosition.get();
            List<String> info = itemOnNewPosition.get().getInfo();
            performDamage();
            return info;
        } else if (mobUnderPlayer != null) {
            mobUnderPlayer.print(graphics);
            mobUnderPlayer = null;
        }
        return List.of();
    }
    
    /**
     * The method deals damage if the user stands on
     * a mob for a certain amount of time
     */
    public void performDamage() {
        if (mobUnderPlayer != null && !isBackpackOpened) {
            if (System.currentTimeMillis() - timeUnderPlayer >= 1000) {
                mobUnderPlayer.setHealth(mobUnderPlayer.getHealth() - player.getWeaponValue());
                if (player.getArmorValue() > mobUnderPlayer.getDamage()) {
                    player.setArmorValue(player.getArmorValue() - mobUnderPlayer.getDamage());
                } else {
                    player.setHealth(player.getHealth() - mobUnderPlayer.getDamage() + player.getArmorValue());
                    player.setArmor(new Item(null,
                                             null,
                                             noArmor,
                                             ItemCharacteristic.USUAL,
                                             ItemType.ARMOR,
                                             null,
                                             "",
                                             0));
                }
                timeUnderPlayer = System.currentTimeMillis();
                changes.add(MOB_CHANGED);
                changes.add(PLAYER_INFO);
            }
            if (player.getHealth() <= 0) {
                restart();
            } else {
                if (mobUnderPlayer.getHealth() <= 0) {
                    levelMap.removeMob(mobUnderPlayer);
                    addExperience(mobUnderPlayer.getExperience());
                    mobUnderPlayer = null;
                    timeUnderPlayer = 0;
                }
            }
        }
    }
    
    public void restart() {
        itemUnderPlayer = null;
        mobUnderPlayer = null;
        dungeonLevel = -1;
        timeUnderPlayer = 0;
        isBackpackOpened = false;
        backpackItemsInRow = 3;
        levelMap = null;
        player = new Player(new TerminalRectangle(0, 0, 0, 0));
        changes.clear();
    }
    
    /**
     * Increases player experience
     */
    public void addExperience(int experience) {
        player.setExperience(player.getExperience() + experience);
        while (player.getExperience() >= player.getExperienceForNextLevel()) {
            player.setExperience(player.getExperience() - player.getExperienceForNextLevel());
            player.setLevel(player.getLevel() + 1);
            double proportionLevelEx;
            if (player.getLevel() % 100 == 99) {
                proportionLevelEx = Proportions.newHundredthLevelEx;
            } else if (player.getLevel() % 10 == 9) {
                proportionLevelEx = Proportions.newTenthLevelEx;
            } else {
                proportionLevelEx = Proportions.newLevelEx;
            }
            player.setExperienceForNextLevel((int) (player.getExperienceForNextLevel() * proportionLevelEx));
            player.setMaxHealth((int) (player.getMaxHealth() * Proportions.maxHealth));
            player.setHealth(player.getMaxHealth());
        }
        changes.add(PLAYER_INFO);
    }
    
    /**
     * Picks up item from map and puts it in backpack
     *
     * @return information about item or notification of overfilling the backpack
     */
    public List<String> pickupItem() {
        if (itemUnderPlayer != null) {
            // store it in backpack if its enough space in it
            if (player.getBackpackSize() == backpackSize) {
                return List.of("Your backpack is full!");
            }
            
            levelMap.getItems().remove(itemUnderPlayer);
            player.addToBackpack(itemUnderPlayer);
            List<String> pickedItemInfo = new ArrayList<>();
            pickedItemInfo.add("You picked up :");
            pickedItemInfo.addAll(itemUnderPlayer.getInfo());
            itemUnderPlayer = null;
            changes.add(ADD_REMOVE_ITEM);
            return pickedItemInfo;
        }
        return List.of();
    }
    
    /**
     * Changes the selected item in the backpack according to the pressed by player arrow button
     * (up, down, left or right)
     */
    public void setSelectedItemInBackpack(Direction direction) {
        int selectedItem = player.getSelectedItemIndex();
        if (direction == Direction.RIGHT && selectedItem + 1 < player.getBackpackSize()) {
            selectedItem++;
        } else if (direction == Direction.LEFT && selectedItem != 0) {
            selectedItem--;
        } else if (direction == Direction.UP && selectedItem - backpackItemsInRow >= 0) {
            selectedItem -= backpackItemsInRow;
        } else if (direction == Direction.DOWN && selectedItem + backpackItemsInRow < player.getBackpackSize()) {
            selectedItem += backpackItemsInRow;
        }
        player.setSelectedItemIndex(selectedItem);
        changes.add(SELECTED_INDEX_ITEM);
    }
    
    /**
     * Applies selected item in the backpack
     */
    public void applySelectedItem() {
        int selectedItem = player.getSelectedItemIndex();
        Item item = player.getSelectedInBackpackItem();
        switch (item.getItemType()) {
            case MEDICAL_AID -> applyMedicalAid(item);
            case ARMOR -> applyArmor(item, selectedItem);
            case WEAPON -> applyWeapon(item, selectedItem);
        }
        player.setSelectedItemIndex(Math.max(0, selectedItem - 1));
        if (player.getBackpackSize() == 0) {
            isBackpackOpened = false;
        }
        changes.add(PLAYER_INFO);
        changes.add(ADD_REMOVE_ITEM);
    }
    
    /**
     * Applies medical aid selected in the backpack according to the medical aid type
     */
    private void applyMedicalAid(Item item) {
        int addHealth = (int) (player.getMaxHealth() *
                               (item.getItemCharacteristic() == USUAL ?
                                ObjectEffect.usualAid :
                                ObjectEffect.legendaryAid)) + player.getHealth();
        player.setHealth(Math.min(addHealth, player.getMaxHealth()));
        player.removeFromBackpack(item);
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
            player.removeFromBackpack(item);
            player.setArmor(equippedArmor);
        } else {
            player.removeFromBackpack(item);
            if (!equippedArmor.getName().equals(ObjectNames.noArmor)) {
                player.addToBackpack(selectedItem, equippedArmor);
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
            player.removeFromBackpack(item);
            player.setWeaponValue(addWeapon);
        } else {
            player.removeFromBackpack(item);
            if (!equippedWeapon.getName().equals(ObjectNames.emptyHands)) {
                player.addToBackpack(selectedItem, equippedWeapon);
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
        Item item = player.getSelectedInBackpackItem();
        player.removeFromBackpack(item);
        TerminalRectangle freePosition = getNearestFreePlace().get();
        item.setPosition(freePosition);
        if (freePosition.equals(player.getPosition())) {
            itemUnderPlayer = item;
        }
        levelMap.addItem(item);
        int selectedItem = player.getSelectedItemIndex();
        player.setSelectedItemIndex(Math.max(0, selectedItem - 1));
        if (player.getBackpackSize() == 0) {
            isBackpackOpened = false;
        }
        changes.add(ADD_REMOVE_ITEM);
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
                         Checker.getNextPosition(Direction.RIGHT, playerPosition),
                         Checker.getNextPosition(Direction.LEFT, playerPosition),
                         Checker.getNextPosition(Direction.UP, playerPosition),
                         Checker.getNextPosition(Direction.DOWN, playerPosition),
                         Checker.getNextPosition(Direction.DOWN_RIGHT, playerPosition),
                         Checker.getNextPosition(Direction.DOWN_LEFT, playerPosition),
                         Checker.getNextPosition(Direction.UP_RIGHT, playerPosition),
                         Checker.getNextPosition(Direction.UP_LEFT, playerPosition))
                     .filter(position -> !Checker.isWallAtPosition(position, levelMap.getWalls()) &&
                                         Checker.isObjectAtPosition(position, levelMap.getItems()).isEmpty() &&
                                         Checker.isObjectAtPosition(position, levelMap.getMobs()).isEmpty())
                     .findFirst()
                     .or(() -> Optional.of(playerPosition));
    }
    
    /**
     * Makes all alive mobs to make action if they see player
     */
    public void makeAllMobsAlive() {
        ArrayList<Mob> oldMobs = new ArrayList<>(getMobs().stream().map(Mob::makeClone).toList());
        getMobs().forEach(mob -> mob.makeAction(player.getPosition(), levelMap.getWalls()));
        for (int i = 0; i < oldMobs.size(); i++) {
            if (!oldMobs.get(i).getPosition().equals(getMobs().get(i).getPosition())) {
                changes.add(MOB_CHANGED);
                oldMobs.clear();
                break;
            }
        }
    }
    
    public TerminalRectangle getPlayerPosition() {
        return player.getPosition();
    }
    
    public List<Mob> getMobs() {
        return levelMap.getMobs();
    }
    
    public List<Item> getItems() {
        return levelMap.getItems();
    }
    
    public List<String> getChangesAndClear() {
        ArrayList<String> newChanges = new ArrayList<>(changes);
        changes.clear();
        return newChanges;
    }
    
    public void setBackpackOpened(boolean value) {
        changes.add(value ? BACKPACK_OPENED_TRUE : BACKPACK_OPENED_FALSE);
        isBackpackOpened = value;
    }
}
