package org.itmo.mse.constants;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import static com.googlecode.lanterna.Symbols.*;

public interface SpecialCharacters {
    TextCharacter DELIMITER = TextCharacter.fromCharacter('-', TextColor.ANSI.YELLOW, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SELECTED_ITEM = TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.WHITE)[0];
    TextCharacter WALL = TextCharacter.fromCharacter('@', TextColor.ANSI.WHITE, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SPACE = TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter PLAYER = TextCharacter.fromCharacter('+', TextColor.ANSI.GREEN, TextColor.ANSI.DEFAULT)[0];
    TextCharacter MOB = TextCharacter.fromCharacter('#', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter PASSIVE_MOB = TextCharacter.fromCharacter('P', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter AGGRESSIVE_MOB = TextCharacter.fromCharacter('A', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SHY_MOB = TextCharacter.fromCharacter('S', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter ITEM = TextCharacter.fromCharacter('?', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter MEDICAL_AID = TextCharacter.fromCharacter(HEART,
                                                            TextColor.ANSI.RED_BRIGHT,
                                                            TextColor.ANSI.DEFAULT)[0];
    TextCharacter ARMOR = TextCharacter.fromCharacter(DIAMOND, TextColor.ANSI.MAGENTA, TextColor.ANSI.DEFAULT)[0];
    TextCharacter WEAPON = TextCharacter.fromCharacter(DOUBLE_LINE_CROSS,
                                                       TextColor.ANSI.CYAN,
                                                       TextColor.ANSI.DEFAULT)[0];
    
    static char getWallChar() {
        return WALL.getCharacterString().charAt(0);
    }
    
    static char getSpaceChar() {
        return SPACE.getCharacterString().charAt(0);
    }
    
    static char getUserChar() {
        return PLAYER.getCharacterString().charAt(0);
    }
    
    static char getMobChar() {
        return MOB.getCharacterString().charAt(0);
    }
    
    static char getItemChar() {
        return ITEM.getCharacterString().charAt(0);
    }
}
