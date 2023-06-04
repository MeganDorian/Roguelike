package org.itmo.mse;

import java.io.IOException;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.ui.windows.GameWindow;
import org.itmo.mse.ui.windows.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            new MainWindow().waitToPressEnter();
            new GameWindow().play();
        } catch (IOException | InterruptedException | IncorrectMapFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
