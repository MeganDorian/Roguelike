package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalPosition;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.utils.SpecialCharacters;

public class Wall extends Object {
    public Wall(TerminalPosition start, TerminalPosition end) {
        super(start, end, SpecialCharacters.WALL);
    }
}
