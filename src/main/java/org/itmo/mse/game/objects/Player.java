package org.itmo.mse.game.objects;

import static org.itmo.mse.constants.Proportions.backpackSize;
import static org.itmo.mse.constants.SpecialCharacters.MEDICAL_AID;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectDescription;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;

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
        
        // TODO generate base backpack
        for (int i = 0; i < backpackSize; i++) {
            backpack.getItems().add(
                new Item(position, MEDICAL_AID, ObjectNames.usualAids.get(2), ItemType.USUAL,
                         ObjectDescription.usualAid));
        }
    }
    
    @Override
    public List<String> getInfo() {
        return List.of(getName(), "", "LVL: " + level, "XP: " + experience, "HP: " + health,
                       "ATTACK: " + weapon, "ARMOR: " + armor);
    }
}
