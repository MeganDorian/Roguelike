package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.Game;
import org.itmo.mse.ui.Action;
import org.itmo.mse.utils.map.MapLoader;

public class GameWindow extends Window {
    private final Game game;
    
    public GameWindow() throws IOException, IncorrectMapFormatException {
        game = new Game();
        screen.clear();
        screen.refresh();
        printLevel(1);
    }
    
    private void printLevel(int level) throws IOException, IncorrectMapFormatException {
        textGraphics.putString(1, 1, "LEVEL: " + level);
        if (level == 1) {
            // load tutorial level from file
            loadLevelFromFile("first_lvl", true);
        } else {
            // generate new map
        }
        printObject(game.getLevelMap());
        printObject(game.getPlayer());
        screen.refresh();
    }
    
    private void loadLevelFromFile(String fileName, boolean isFirst)
        throws IOException, IncorrectMapFormatException {
        game.setLevelMap(MapLoader.loadFromFile(fileName, isFirst, game.getPlayer()));
    }
    
    public void play() throws IOException {
        Action action;
        do {
            action = processLevel();
        } while (action != Action.EXIT);
    }
    
    private Action processLevel() throws IOException {
        KeyStroke input = screen.pollInput();
        
        if (input != null && input.getKeyType() == KeyType.Escape) {
//            return showExitPopup();
        }
        return Action.WAIT_TO_PRESS_ENTER;
    }

//    private Action showExitPopup() throws IOException {
//        final Action[] action = new Action[1];
//        Label title = new Label("Are you sure? You will lose all your progress");
//        title.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING,
//                                                        GridLayout.Alignment.BEGINNING, true, false,
//                                                        2, 1));
//        Button yes = new Button("Yes", () -> action[0] = Action.EXIT);
//        Button no = new Button("No", () -> action[0] = Action.CONTINUE_PLAYING);
//
//        PopupWindow.builder().setTitle("Exit to main menu").setNumberOfColumns(2)
//                   .addComponent(title).addComponent(yes).addComponent(no).show();
//
//        return action[0];
//    }
}
