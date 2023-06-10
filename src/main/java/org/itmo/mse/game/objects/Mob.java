package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.MobStrategy;

@Getter
public class Mob extends Object {
    
    private TerminalRectangle visionRange;
    
    private MobStrategy strategy;
    
    private final int damage;
    
    @Setter
    private int health;
    
    private final int experience;
    
    
    public Mob(TerminalRectangle position, TextCharacter specialCharacters, String name, MobStrategy strategy,
               int damage,
               int health, int experience) {
        super(position, specialCharacters, name);
        this.strategy = strategy;
        this.damage = damage;
        this.health = health;
        this.experience = experience;
    }
    
    /**
     * Get info about mob
     *
     * @return info
     */
    @Override
    public List<String> getInfo() {
        return List.of(getName(),"ATTACK: " + damage, "HP: " + health, "XP: " + experience);
    }
}
