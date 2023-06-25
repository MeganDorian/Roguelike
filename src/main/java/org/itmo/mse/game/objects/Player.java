package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.generation.ItemGeneration;

import java.util.List;

import static org.itmo.mse.constants.ObjectNames.emptyHands;
import static org.itmo.mse.constants.ObjectNames.noArmor;

@Getter
@Setter
public class Player extends Object {
    private final Backpack backpack = new Backpack();
    private int level = 1;
    private int experienceForNextLevel = 10;
    private int experience = 0;
    private int health = 2;
    private int maxHealth = 10;
    private Item weapon = new Item(null, null, emptyHands, ItemCharacteristic.USUAL, ItemType.WEAPON, null, "", 1);
    private Item armor = new Item(null, null, noArmor, ItemCharacteristic.USUAL, ItemType.ARMOR, null, "", 0);
    
    public Player(TerminalRectangle position) {
        super(SpecialCharacters.PLAYER, ObjectNames.player, position);
        try {
            backpack.getItems().add(ItemGeneration.generateItem(ItemType.WEAPON));
            backpack.getItems().add(ItemGeneration.generateItem(ItemType.ARMOR));
            backpack.getItems().add(ItemGeneration.generateItem(ItemType.MEDICAL_AID));
        } catch (IncorrectItemType ex) {
            //TODO display a generation error message or just leave an empty rucksack?
        }
    }
    
    public Player(Player state) {
        super(state.getCharacter(),
              state.getName(),
              new TerminalRectangle(state.getPosition().x,
                                    state.getPosition().y,
                                    state.getPosition().width,
                                    state.getPosition().height));
        this.backpack.getItems().addAll(state.backpack.getItems());
        this.level = state.getLevel();
        this.experienceForNextLevel = state.experienceForNextLevel;
        this.experience = state.experience;
        this.health = state.health;
        this.maxHealth = state.maxHealth;
        this.weapon = new Item(state.weapon);
        this.armor = new Item(state.armor);
        setSelectedItemIndex(state.backpack.getSelectedItemIndex());
    }
    
    /**
     * Get info about player
     *
     * @return info
     */
    @Override
    public List<String> getInfo() {
        return List.of(getName(),
                       "",
                       "LVL: " + level,
                       "XP: " + experience,
                       "HP: " + health,
                       "ATTACK: " +
                       weapon.getValue() +
                       (weapon.getItemClass() != null ? " " + weapon.getItemClass() : ""),
                       "ARMOR: " +
                       armor.getValue() +
                       " " +
                       (armor.getItemClass() != null ? " " + armor.getItemClass() : ""));
    }
    
    public int getBackpackSize() {
        return backpack.size();
    }
    
    public Item getSelectedInBackpackItem() {
        return backpack.get(backpack.getSelectedItemIndex());
    }
    
    public void setSelectedItemIndex(int index) {
        backpack.setSelectedItemIndex(index);
    }
    
    public int getSelectedItemIndex() {
        return backpack.getSelectedItemIndex();
    }
    
    public List<Item> getBackpackItems() {
        return backpack.getItems();
    }
    
    public void addToBackpack(Item newItem) {
        backpack.getItems().add(newItem);
    }
    
    public void addToBackpack(int index, Item newItem) {
        backpack.getItems().add(index, newItem);
    }
    
    public void removeFromBackpack(Item newItem) {
        backpack.getItems().remove(newItem);
    }
    
    public int getWeaponValue() {
        return weapon.getValue();
    }
    
    public void setWeaponValue(int value) {
        weapon.setValue(value);
    }
    
    public int getArmorValue() {
        return armor.getValue();
    }
    
    public void setArmorValue(int value) {
        armor.setValue(value);
    }
}
