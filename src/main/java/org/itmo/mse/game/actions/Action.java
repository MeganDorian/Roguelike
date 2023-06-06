package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

public interface Action {
    Game game = new Game();
    
    List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException;

}
