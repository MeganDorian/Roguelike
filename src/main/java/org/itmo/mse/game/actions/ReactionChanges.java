package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.List;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

public class ReactionChanges implements Action {
    Game oldGameObject = null;
    
    @Override
    public List<String> execute(TextGraphics graphics)
        throws IncorrectMapFormatException, IOException {
        if(oldGameObject == null) {
            oldGameObject = new Game(game);
            return List.of("print");
        } else {
            if(!oldGameObject.equals(game)) {
                oldGameObject = new Game(game);
                return List.of("print");
            }
        }
        return List.of();
    }
    
}
