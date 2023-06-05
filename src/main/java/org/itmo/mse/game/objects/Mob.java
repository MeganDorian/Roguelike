package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.constants.SpecialCharacters;

public class Mob extends Object {
    public Mob(TerminalRectangle position) {
        super(position, SpecialCharacters.MOB);
    }
}
