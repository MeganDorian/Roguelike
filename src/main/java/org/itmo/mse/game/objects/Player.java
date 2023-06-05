package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalPosition;
import lombok.Getter;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.items.MedicalAid;

@Getter
public class Player extends Object {
    private int level = 1;
    
    private int experience = 0;
    
    private int health = 10;
    
    private int attack = 0;
    
    private int armor = 1;
    
    private Backpack backpack = new Backpack();
    
    public Player(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.USER);
        
        // TODO generate base backpack
        for (int i = 0; i < 9; i++) {
            backpack.getItems().add(new MedicalAid(start));
        }
    }
}
