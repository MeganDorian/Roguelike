package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalRectangle;
import lombok.Getter;
import org.itmo.mse.game.objects.mob.Mob;

import java.util.ArrayList;
import java.util.List;

import static org.itmo.mse.generation.MobGeneration.generateMob;

public class MobLoader {
    
    @Getter
    private final List<Mob> mobs = new ArrayList<>();
    
    /**
     * Adds a randomly generated mob to the list of mobs with a passed position
     */
    public void getMobs(TerminalRectangle position) {
        //so far so good for the default map
        Mob mob = generateMob();
        mob.setPosition(position);
        mobs.add(mob);
    }
}
