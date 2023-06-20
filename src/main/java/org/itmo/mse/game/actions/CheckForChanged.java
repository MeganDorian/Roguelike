package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import org.itmo.mse.exceptions.IncorrectMapFormatException;

import java.io.IOException;
import java.util.List;

public class CheckForChanged implements Action {
    
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
        return game.getChangesAndClear();
    }
}
