package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import org.itmo.mse.exceptions.IncorrectMapFormatException;

public class Damage extends TimerTask implements Action {
    
    @Override
    public void run() {
        game.causingDamage();
    }
    
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
    public List<String> execute(TextGraphics graphics)
        throws IncorrectMapFormatException, IOException {
        return List.of();
    }
}
