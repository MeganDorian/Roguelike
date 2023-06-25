package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import org.itmo.mse.exceptions.IncorrectMapFormatException;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

public class Damage extends TimerTask implements Action {
    
    @Override
    public void run() {
        game.performDamage();
    }
    
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
        return List.of();
    }
}
