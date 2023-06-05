package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalPosition;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Object;

public class Wall extends Object {
    public Wall(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.WALL);
    }
}
