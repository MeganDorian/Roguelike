package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalPosition;
import org.itmo.mse.constants.SpecialCharacters;

public class Mob extends Object {
    public Mob(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.MOB);
    }
}
