package org.itmo.mse.game.objects;

import static org.itmo.mse.generation.ItemGeneration.generateItem;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectItemType;

@Getter
public class Player extends Object {
    private int level = 1;
    
    private int experience = 0;
    
    private int health = 10;
    
    private int weapon = 0;
    
    private int armor = 1;
    
    private Backpack backpack = new Backpack();
    
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
                       "ATTACK: " + weapon, "ARMOR: " + armor);
    }
}
