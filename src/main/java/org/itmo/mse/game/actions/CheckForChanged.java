package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Setter;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

import java.io.IOException;
import java.util.List;

public class CheckForChanged implements Action {
    
    @Setter
    Game oldGameObject = null;
    
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
        return game.getChangesAndClear();
    }
}
