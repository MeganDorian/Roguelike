package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.List;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;

public class Mob extends Object {
    
    private TerminalRectangle visionRange;
    
    public Mob(TerminalRectangle position) {
        super(position, SpecialCharacters.MOB, ObjectNames.mob);
    }
    
    @Override
    public List<String> getInfo() {
        return List.of();
    }
}
