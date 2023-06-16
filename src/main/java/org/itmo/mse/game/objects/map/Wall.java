package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalRectangle;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Object;

public class Wall extends Object {
    public Wall(TerminalRectangle position) {
        super(SpecialCharacters.WALL, ObjectNames.wall, position);
    }
}
