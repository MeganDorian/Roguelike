package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.ui.windows.GameWindow;

import java.io.IOException;
import java.util.List;

/**
 * User pressed enter
 */
@Getter
@RequiredArgsConstructor
public class StartGame implements Action {
    private GameWindow gameWindow;
    
    /**
     * Start game window
     */
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
        Move move = new Move();
        Action interact = new Interact();
        Action backpackAction = new BackpackAction();
        Action dropItem = new Drop();
        Action reaction = new CheckForChanged();
        gameWindow = new GameWindow(move, game, interact, backpackAction, dropItem, reaction);
        gameWindow.play();
        return null;
    }
}
