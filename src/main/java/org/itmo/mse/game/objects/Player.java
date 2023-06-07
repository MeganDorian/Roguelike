package org.itmo.mse.game.objects;

import static org.itmo.mse.constants.ObjectNames.emptyHands;
import static org.itmo.mse.constants.ObjectNames.noArmor;
import static org.itmo.mse.generation.ItemGeneration.generateItem;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectItemType;

@Getter
@Setter
public class Player extends Object {
    private int level = 1;
    
    private int experienceForNextLevel = 10;
    private int experience = 0;
    
    private int health = 2;
    private int maxHealth = 10;
    private Item weapon =
        new Item(null, null, emptyHands, ItemCharacteristic.USUAL, ItemType.WEAPON, null, "", 1);
    
    private Item armor =
        new Item(null, null, noArmor, ItemCharacteristic.USUAL, ItemType.ARMOR, null, "", 0);
    
    private final Backpack backpack = new Backpack();
    
    public Player(TerminalRectangle position) {
        super(position, SpecialCharacters.PLAYER, ObjectNames.player);
        try {
            backpack.getItems().add(generateItem(ItemType.WEAPON));
            backpack.getItems().add(generateItem(ItemType.ARMOR));
            backpack.getItems().add(generateItem(ItemType.MEDICAL_AID));
        } catch (IncorrectItemType ex) {
            //TODO display a generation error message or just leave an empty rucksack?
        }
    }
    
    @Override
    public List<String> getInfo() {
        return List.of(getName(), "", "LVL: " + level, "XP: " + experience, "HP: " + health,
                       "ATTACK: " + weapon.getValue() +
                       (weapon.getItemClass() != null ? " " + weapon.getItemClass() : ""),
                       "ARMOR: " + armor.getValue() + " " +
                       (armor.getItemClass() != null ? " " + armor.getItemClass() : ""));
    }
}
