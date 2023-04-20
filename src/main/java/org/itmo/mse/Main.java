package org.itmo.mse;

import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.ui.Action;
import org.itmo.mse.ui.windows.GameWindow;
import org.itmo.mse.ui.windows.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow();
            Action action;
            do {
                action = mainWindow.enterPressed();
                if (action == Action.EXIT) {
                    return;
                }
            } while (action != Action.PRESSED_ENTER);
            GameWindow gameWindow = new GameWindow();
            do {
                action = gameWindow.play();
            } while (action != Action.EXIT);
        } catch (IOException | InterruptedException | IncorrectMapFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
