package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalPosition;
import lombok.Getter;
import org.itmo.mse.utils.SpecialCharacters;

@Getter
public class Player extends Object {
    private int level;
    
    private int experience;
    
    private int health;
    
    private int damage;
    
    private int armor;
    
    public Player(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.getUSER());
    }
}
