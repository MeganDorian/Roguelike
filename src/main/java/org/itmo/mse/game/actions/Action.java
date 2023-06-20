package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

import java.io.IOException;
import java.util.List;

public interface Action {
    Game game = new Game();
    
    /**
     * Action interface for different objects
     */
    List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException;
    
}
