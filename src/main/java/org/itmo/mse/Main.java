package org.itmo.mse;

import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.actions.Action;
import org.itmo.mse.game.actions.StartGame;
import org.itmo.mse.ui.windows.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            Action start = new StartGame();
            new MainWindow(start).startGame();
        } catch (IOException | InterruptedException | IncorrectMapFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
