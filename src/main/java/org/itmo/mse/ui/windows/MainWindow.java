package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.itmo.mse.ui.Action;
import org.itmo.mse.utils.FileUtils;

/**
 * Main window, shows when game started
 */
public class MainWindow extends Window {
    
    private final static double PADDING_BOTTOM = 0.1;
    private final static double PADDING_START = 0.05;
    
    private TerminalPosition pressLineStartPosition;
    
    private final String start = "press enter to start game";
    
    private final String screenResize =
        "please resize your screen if you can't see this text in " + "full";
    
    /**
     * Prints game logo
     */
    public MainWindow() throws IOException, InterruptedException {
        screen.clear();
        checkScreenSize();
        printLogo();
    }
    
    public void waitToPressEnter() throws IOException, InterruptedException {
        Action action;
        do {
            action = enterPressed();
            if (action == Action.EXIT) {
                return;
            }
        } while (action != Action.PRESSED_ENTER);
    }
    
    private void checkScreenSize() throws IOException, InterruptedException {
        TerminalPosition center = new TerminalPosition(0, 0);
        while (terminal.getTerminalSize().getColumns() < size.getColumns() ||
               terminal.getTerminalSize().getRows() < size.getRows()) {
            eraseStringAtPosition(center, screenResize.length());
            Thread.sleep(1000);
            printStringAtPosition(screenResize, center);
            Thread.sleep(1000);
        }
    }
    
    /**
     * Imitates blinking of line "press enter to start game"
     *
     * @return exit if escape key pressed <br> pressed_enter if enter key pressed <br>
     * wait_to_press_enter if nothing pressed or any other key pressed
     */
    private Action enterPressed() throws IOException, InterruptedException {
        eraseStringAtPosition(pressLineStartPosition, start.length());
        Thread.sleep(1000);
        printStringAtPosition(start, pressLineStartPosition);
        Thread.sleep(1000);
        KeyStroke input = screen.pollInput();
        if (input != null) {
            if (input.getKeyType() == KeyType.Enter) {
                return Action.PRESSED_ENTER;
            }
            if (input.getKeyType() == KeyType.Escape) {
                terminal.close();
                return Action.EXIT;
            }
        }
        return Action.WAIT_TO_PRESS_ENTER;
    }
    
    private void printLogo() throws IOException {
        TextImage image = createLogoAsImage();
        
        int column = (int) (PADDING_BOTTOM * size.getColumns());
        int row = (int) (PADDING_START * size.getRows());
        
        image = image.resize(
            new TerminalSize(size.getColumns() - 2 * column, size.getRows() - 2 * row - 1),
            TextCharacter.fromCharacter(' ')[0]);
        
        screen.newTextGraphics().drawImage(new TerminalPosition(column, row), image);
        printStringAtPosition(start, pressLineStartPosition);
        screen.refresh();
    }
    
    private TextImage createLogoAsImage() throws IOException {
        int imageWidth;
        int imageHeight;
        TextImage image;
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(FileUtils.getFileFromResource("logo")))) {
            String[] params = reader.readLine().split(" ");
            imageHeight = Integer.parseInt(params[0]); // height of the logo
            imageWidth = Integer.parseInt(params[1]); // width of the logo
            image = new BasicTextImage(imageWidth, imageHeight);
            for (int j = 0; j < imageHeight; j++) {
                String line = reader.readLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != ' ') {
                        image.setCharacterAt(i, j, TextCharacter.fromCharacter(' ',
                                                                               TextColor.ANSI.DEFAULT,
                                                                               TextColor.ANSI.RED)[0]);
                    }
                }
            }
        }
        
        pressLineStartPosition =
            new TerminalPosition((size.getColumns() - start.length()) / 2, imageHeight + 5);
        return image;
    }
}
