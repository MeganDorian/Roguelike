package org.itmo.mse.game.actions;

import static org.itmo.mse.constants.Change.ADD_REMOVE_ITEM;
import static org.itmo.mse.constants.Change.BACKPACK_OPENED_FALSE;
import static org.itmo.mse.constants.Change.BACKPACK_OPENED_TRUE;
import static org.itmo.mse.constants.Change.DUNGEON_LEVEL;
import static org.itmo.mse.constants.Change.PLAYER_INFO;
import static org.itmo.mse.constants.Change.PLAYER_POSITION;
import static org.itmo.mse.constants.Change.SELECTED_INDEX_ITEM;

import com.googlecode.lanterna.graphics.TextGraphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

public class ReactionChanges implements Action {
    
    @Setter
    Game oldGameObject = null;
    
    @Override
    public List<String> execute(TextGraphics graphics)
        throws IncorrectMapFormatException, IOException {
        if(oldGameObject == null) {
            oldGameObject = new Game(game);
        } else {
            if(!oldGameObject.equals(game)) {
                if(oldGameObject.getDungeonLevel() != game.getDungeonLevel()) {
                    oldGameObject = new Game(game);
                    return List.of(DUNGEON_LEVEL.name());
                }
                List<String> changes = new ArrayList<>();
                if(!oldGameObject.getPlayer().getPosition().equals(game.getPlayer().getPosition())) {
                    changes.add(PLAYER_POSITION.name());
                }
                if(oldGameObject.getPlayer().getBackpack().getSelectedItemIndex() !=
                   game.getPlayer().getBackpack().getSelectedItemIndex()) {
                    changes.add(SELECTED_INDEX_ITEM.name());
                }
                if(oldGameObject.isBackpackOpened() != game.isBackpackOpened()) {
                    if(game.isBackpackOpened()) {
                        changes.add(BACKPACK_OPENED_TRUE.name());
                    } else {
                        changes.add(BACKPACK_OPENED_FALSE.name());
                    }
                }
                if(!oldGameObject.getPlayer().getBackpack().getItems().equals(
                    game.getPlayer().getBackpack().getItems())) {
                    changes.add(ADD_REMOVE_ITEM.name());
                }
                if(!oldGameObject.getPlayer().getInfo().equals(game.getPlayer().getInfo())) {
                    changes.add(PLAYER_INFO.name());
                }
                oldGameObject = new Game(game);
                return changes;
            }
        }
        return List.of();
    }
    
}
