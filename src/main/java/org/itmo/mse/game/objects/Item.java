package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalPosition;
import org.itmo.mse.utils.SpecialCharacters;

public class Item extends Object {
    public Item(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.THING);
    }
}
