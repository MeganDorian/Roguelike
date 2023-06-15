package org.itmo.mse.game.actions;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Setter;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.itmo.mse.constants.ChangeNames.*;

public class CheckForChanged implements Action {
    
    @Setter
    Game oldGameObject = null;
    
    @Override
    public List<String> execute(TextGraphics graphics) throws IncorrectMapFormatException, IOException {
        if (oldGameObject == null) {
            oldGameObject = new Game(game);
        } else {
            if (!oldGameObject.equals(game)) {
                if (oldGameObject.getDungeonLevel() != game.getDungeonLevel()) {
                    oldGameObject = new Game(game);
                    return List.of(DUNGEON_LEVEL);
                }
                List<String> changes = new ArrayList<>();
                changes.addAll(checkPlayerPositionChanged());
                changes.addAll(checkBackpackChanged());
                changes.add(checkLevelMapChanged());
                changes.removeIf(s -> s.equals(""));
                oldGameObject = new Game(game);
                return changes;
            }
        }
        return List.of();
    }
    
    private List<String> checkPlayerPositionChanged() {
        List<String> playerChanges = new ArrayList<>();
        playerChanges.add(!oldGameObject.getPlayerPosition().equals(game.getPlayerPosition()) ? PLAYER_POSITION : "");
        playerChanges.add(!oldGameObject.getPlayer().getInfo().equals(game.getPlayer().getInfo()) ? PLAYER_INFO : "");
        return playerChanges;
    }
    
    private List<String> checkBackpackChanged() {
        List<String> backpackChanges = new ArrayList<>();
        if (oldGameObject.getPlayer().getSelectedItemIndex() != game.getPlayer().getSelectedItemIndex()) {
            backpackChanges.add(SELECTED_INDEX_ITEM);
        }
        backpackChanges.add(oldGameObject.isBackpackOpened() != game.isBackpackOpened() ?
                            BACKPACK_OPENED_TRUE :
                            BACKPACK_OPENED_FALSE);
        if (!oldGameObject.getPlayer().getBackpackItems().equals(game.getPlayer().getBackpackItems())) {
            backpackChanges.add(ADD_REMOVE_ITEM);
        }
        return backpackChanges;
    }
    
    private String checkLevelMapChanged() {
        if (game.getLevelMap() != null &&
            (oldGameObject.getLevelMap() == null || !game.getMobs().equals(oldGameObject.getMobs()))) {
            return DEATH_MOB;
        }
        return "";
    }
    
}
