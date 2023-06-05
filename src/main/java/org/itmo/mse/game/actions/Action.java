package org.itmo.mse.game.actions;

import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

public interface Action {
    Game game = new Game();
    
    void execute() throws IncorrectMapFormatException, IOException;
    
    default Game getGame() {
        return game;
    }
}
