package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.game.objects.Mob;

public class MobLoader {
    
    @Getter
    private final List<Mob> mobs = new ArrayList<>();
    
    public void getMobs(TerminalRectangle position) {
        // if there going to be different mobs
        mobs.add(new Mob(position));
    }
}
