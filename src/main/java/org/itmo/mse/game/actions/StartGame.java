package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;
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
    public List<String> execute(TextGraphics graphics)
        throws IncorrectMapFormatException, IOException {
        Move move = new Move();
        Action interact = new Interact();
        Action backpackAction = new BackpackAction();
        gameWindow = new GameWindow(move, game, interact, backpackAction);
        gameWindow.play();
        return null;
    }
}
