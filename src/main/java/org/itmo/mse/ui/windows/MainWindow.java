package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.input.KeyStroke;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.itmo.mse.game.actions.Action;
import org.itmo.mse.utils.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.itmo.mse.constants.Proportions.paddingBottom;
import static org.itmo.mse.constants.Proportions.paddingStart;

/**
 * Main window, shows when game started
 */

public class MainWindow extends Window {
    
    private TerminalPosition pressLineStartPosition;
    
    private final Action startGame;
    
    private final String start = "press enter to start game";
    
    /**
     * Prints game logo
     */
    public MainWindow(Action startGame) throws IOException, InterruptedException {
        this.startGame = startGame;
        screen.clear();
        checkScreenSize();
        printLogo();
    }
    
    /**
     * Waiting for Enter to start the game or ESC to close
     */
    public void startGame() throws IOException, InterruptedException, IncorrectMapFormatException {
        while (true) {
            enterPressed();
            KeyStroke input = screen.pollInput();
            if (input == null) {
                continue;
            }
            switch (input.getKeyType()) {
                case Enter:
                    startGame.execute(textGraphics);
                case Escape:
                    terminal.close();
                    return;
            }
        }
    }
    
    /**
     * Checks that the screen is the right size for a comfortable game
     */
    private void checkScreenSize() throws IOException, InterruptedException {
        TerminalPosition center = new TerminalPosition(0, 0);
        String screenResize = "please resize your screen if you can't see this text in full";
        while (terminal.getTerminalSize().getColumns() < size.getColumns() ||
               terminal.getTerminalSize().getRows() < size.getRows()) {
            eraseAtPosition(center, screenResize.length());
            Thread.sleep(1000);
            printStringAtPosition(screenResize, center);
            Thread.sleep(1000);
        }
        screen.clear();
    }
    
    /**
     * Imitates blinking of line {@link #start}
     */
    private void enterPressed() throws IOException, InterruptedException {
        eraseAtPosition(pressLineStartPosition, start.length());
        Thread.sleep(1000);
        printStringAtPosition(start, pressLineStartPosition);
        Thread.sleep(1000);
    }
    
    /**
     * Print logo for screen
     */
    private void printLogo() throws IOException {
        TextImage image = createLogoAsImage();
        
        int column = (int) (paddingBottom * getSize().getColumns());
        int row = (int) (paddingStart * getSize().getRows());
        
        image = image.resize(new TerminalSize(getSize().getColumns() - 2 * column, getSize().getRows() - 2 * row - 1),
                             TextCharacter.fromCharacter(' ')[0]);
        
        screen.newTextGraphics().drawImage(new TerminalPosition(column, row), image);
        printStringAtPosition(start, pressLineStartPosition);
        screen.refresh();
    }
    
    /**
     * Create logo as image
     *
     * @return image
     */
    private TextImage createLogoAsImage() throws IOException {
        int imageWidth;
        int imageHeight;
        TextImage image;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getFileFromResource("logo")))) {
            String[] params = reader.readLine().split(" ");
            imageHeight = Integer.parseInt(params[0]); // height of the logo
            imageWidth = Integer.parseInt(params[1]); // width of the logo
            image = new BasicTextImage(imageWidth, imageHeight);
            for (int j = 0; j < imageHeight; j++) {
                String line = reader.readLine();
                readImage(image, line, j);
            }
        }
        
        pressLineStartPosition = new TerminalPosition((getSize().getColumns() - start.length()) / 2, imageHeight + 5);
        return image;
    }
    
    /**
     * Read image (for logo)
     */
    private void readImage(TextImage image, String line, int j) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                continue;
            }
            image.setCharacterAt(i, j, TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.RED)[0]);
        }
    }
}
