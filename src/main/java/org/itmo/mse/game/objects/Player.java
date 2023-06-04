package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalPosition;
import lombok.Getter;
import org.itmo.mse.utils.SpecialCharacters;

@Getter
public class Player extends Object {
    private int level = 1;
    
    private int experience = 0;
    
    private int health = 100;
    
    private int damage = 0;
    
    private int armor = 0;
    
    public Player(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.USER);
    }
}
