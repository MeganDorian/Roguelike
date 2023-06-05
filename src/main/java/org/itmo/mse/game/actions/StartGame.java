package org.itmo.mse.game.actions;

import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.ui.windows.GameWindow;

/**
 * User pressed enter
 */
@Getter
@RequiredArgsConstructor
public class StartGame implements Action {
    private GameWindow gameWindow;
    
    @Override
    public void execute() throws IncorrectMapFormatException, IOException {
        gameWindow = new GameWindow(game);
        gameWindow.play();
    }
}
