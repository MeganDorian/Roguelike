package org.itmo.mse.utils.map;

import static org.itmo.mse.generation.MobGeneration.generateMob;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.game.objects.Mob;

public class MobLoader {
    
    @Getter
    private final List<Mob> mobs = new ArrayList<>();
    
    public void getMobs(TerminalRectangle position) {
        //so far so good for the default map
        Mob mob = generateMob();
        mob.setPosition(position);
        mobs.add(mob);
    }
}
