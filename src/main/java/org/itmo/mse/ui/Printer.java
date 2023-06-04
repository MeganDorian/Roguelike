package org.itmo.mse.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import java.io.IOException;
import org.itmo.mse.game.objects.Object;

public abstract class Printer {
    
    protected static TerminalScreen screen;
    
    protected final TextGraphics textGraphics = screen.newTextGraphics();
    
    public void printObject(Object object) throws IOException {
        object.print(textGraphics);
        screen.refresh();
    }
    
    protected void printStringAtPosition(String string, TerminalPosition position)
        throws IOException {
        textGraphics.putString(position, string);
        screen.refresh();
    }
    
    protected void eraseStringAtPosition(TerminalPosition position, int length) throws IOException {
        textGraphics.drawLine(position, position.withRelativeColumn(length - 1), ' ');
        screen.refresh();
    }
}
