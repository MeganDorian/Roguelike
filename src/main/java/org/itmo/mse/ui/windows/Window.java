package org.itmo.mse.ui.windows;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import java.io.IOException;
import org.itmo.mse.ui.Printer;

public abstract class Window extends Printer {
    
    protected static final int columnSize = 150;
    protected static final int rowsSize = 40;
    
    protected static final TerminalSize size = new TerminalSize(columnSize, rowsSize);
    
    static {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            terminal =
                defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(120, 30)).createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected Window() throws IOException {
        terminal.setCursorVisible(false);
    }
}