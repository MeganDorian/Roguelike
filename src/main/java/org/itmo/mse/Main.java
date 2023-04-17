package org.itmo.mse;

import java.io.IOException;
import org.itmo.mse.ui.Action;
import org.itmo.mse.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow();
            Action isEnterPressed;
            do {
                isEnterPressed = mainWindow.enterPressed();
                if (isEnterPressed == Action.EXIT) {
                    return;
                }
            } while (isEnterPressed != Action.PRESSED_ENTER);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
