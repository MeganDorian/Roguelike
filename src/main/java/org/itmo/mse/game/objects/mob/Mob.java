package org.itmo.mse.game.objects.mob;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.map.Wall;

import java.util.List;

import static org.itmo.mse.constants.MobSpecifications.mobVisionDefaultValue;

@Getter
public class Mob extends Object {
    private final int damage;
    private final int experience;
    @Setter
    private TerminalRectangle visionRange;
    @Setter
    private MobStrategy strategy;
    @Setter
    private int health;
    
    
    public Mob(TerminalRectangle position, TextCharacter specialCharacters, String name, MobStrategy strategy,
               int damage, int health, int experience) {
        super(specialCharacters, name, position);
        this.strategy = strategy;
        this.damage = damage;
        this.health = health;
        this.experience = experience;
        visionRange = new TerminalRectangle(position.x - mobVisionDefaultValue / 2,
                                            position.y - mobVisionDefaultValue / 2,
                                            mobVisionDefaultValue + 1,
                                            mobVisionDefaultValue + 1);
    }
    
    @Override
    public Mob clone() {
        Mob mob = new Mob(getPosition(), getCharacter(), getName(), strategy, damage, health, experience);
        mob.visionRange = visionRange;
        return mob;
    }
    
    public void makeAction(TerminalRectangle playerPosition, List<Wall> walls) {
        TerminalRectangle newPosition = strategy.execute(visionRange, getPosition(), playerPosition, walls);
        setPosition(newPosition);
    }
    
    public void setPosition(TerminalRectangle position) {
        this.position = position;
        visionRange = new TerminalRectangle(position.x - mobVisionDefaultValue / 2,
                                            position.y - mobVisionDefaultValue / 2,
                                            mobVisionDefaultValue + 1,
                                            mobVisionDefaultValue + 1);
    }
    
    /**
     * Get info about mob
     *
     * @return info
     */
    @Override
    public List<String> getInfo() {
        return List.of(getName(), "ATTACK: " + damage, "HP: " + health, "XP: " + experience);
    }

//    @Override
//    public boolean equals(java.lang.Object obj) {
//        if (obj instanceof Mob) {
//            return ((Mob) obj).damage == damage &&
//                   ((Mob) obj).experience == experience &&
//                   ((Mob) obj).health == health &&
//                   ((Mob) obj).strategy == strategy &&
//                   ((Mob) obj).visionRange.equals(visionRange) &&
//                   ((Mob) obj).getPosition().equals(position);
//        }
//        return false;
//    }
}
