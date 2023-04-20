package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import org.itmo.mse.objects.map.Map;
import org.itmo.mse.ui.Action;
import org.itmo.mse.ui.PopupWindow;
import org.itmo.mse.utils.MapLoader;

public class GameWindow extends Window {
    
    private TextGraphics graphics;
    private Map levelMap;
    
    public GameWindow() throws IOException {
        screen.clear();
        screen.refresh();
        graphics = screen.newTextGraphics();
        printLevel(1);
    }
    
    public void printLevel(int level) throws IOException {
//        graphics.putString(1, 1, "Level: " + level);
        
        printMap(1);
        screen.refresh();
    }
    
    private void printMap(int level) throws IOException {
        if (level == 1) {
            // load tutorial level from file
            loadLevelFromFile("first_lvl", true);
        }
    }
    
    private void loadLevelFromFile(String fileName, boolean isFirst) throws IOException {
        levelMap = MapLoader.loadFromFile(fileName, isFirst);
    }
    
    public Action play() throws IOException {
        KeyStroke input = screen.pollInput();
        
        if (input != null && input.getKeyType() == KeyType.Escape) {
            return showExitPopup();
        }
        return Action.WAIT_TO_PRESS_ENTER;
    }
    
    private Action showExitPopup() throws IOException {
        final Action[] action = new Action[1];
        Label title = new Label("Are you sure? You will lose all your progress");
        title.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING,
                                                        GridLayout.Alignment.BEGINNING, true, false,
                                                        2, 1));
        Button yes = new Button("Yes", () -> action[0] = Action.EXIT);
        Button no = new Button("No", () -> action[0] = Action.CONTINUE_PLAYING);

        PopupWindow.builder().setTitle("Exit to main menu").setNumberOfColumns(2)
                   .addComponent(title).addComponent(yes).addComponent(no).show();

        return action[0];
    }
}
