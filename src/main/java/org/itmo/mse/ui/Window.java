package org.itmo.mse.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

public abstract class Window {
    protected Terminal terminal = null;
    
    protected TerminalScreen screen;
    
    protected TerminalSize size = new TerminalSize(150, 40);
    
    protected Window() throws IOException {
        if (terminal == null) {
            DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
            try {
                if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                    terminal = defaultTerminalFactory.setInitialTerminalSize(size).createTerminal();
                } else {
                    terminal = defaultTerminalFactory.createTerminal();
                    size = terminal.getTerminalSize();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            terminal.setCursorVisible(false);
            screen = new TerminalScreen(terminal);
        }
    }
}