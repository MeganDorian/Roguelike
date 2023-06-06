package org.itmo.mse.generation;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.Random;

public abstract class Generation {
    
    static Random rand = new Random();
    
    static TerminalRectangle generatePosition() {
        //TODO
        return new TerminalRectangle(0, 0, 1, 1);
    }
    
}
