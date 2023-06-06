package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import org.itmo.mse.constants.MobStrategy;

public class Mob extends Object {
    
    private TerminalRectangle visionRange;
    
    private MobStrategy strategy;
    
    private int damage;
    
    private int health;
    
    private int experience;
    
    
    public Mob(TerminalRectangle position, TextCharacter specialCharacters, String name, MobStrategy strategy,
               int damage,
               int health, int experience) {
        super(position, specialCharacters, name);
        this.strategy = strategy;
        this.damage = damage;
        this.health = health;
        this.experience = experience;
    }
    
    @Override
    public List<String> getInfo() {
        return List.of();
    }
}
