package org.itmo.mse.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

public abstract class Window {
    protected Terminal terminal = null;
    
    protected TerminalScreen screen;
    
    protected static final TerminalSize size = new TerminalSize(130, 40);
    
    protected Window() throws IOException {
        if (terminal == null) {
            DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
            try {
                terminal = defaultTerminalFactory.setInitialTerminalSize(size).createTerminal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            terminal.setCursorVisible(false);
            screen = new TerminalScreen(terminal);
        }
    }
}