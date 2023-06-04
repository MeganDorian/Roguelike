package org.itmo.mse.utils;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public interface SpecialCharacters {
    TextCharacter DELIMITER_HORIZONTAL =
        TextCharacter.fromCharacter('-', TextColor.ANSI.YELLOW, TextColor.ANSI.DEFAULT)[0];
    TextCharacter DELIMITER =
        TextCharacter.fromCharacter('.', TextColor.ANSI.YELLOW, TextColor.ANSI.DEFAULT)[0];
    TextCharacter WALL =
        TextCharacter.fromCharacter('@', TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SPACE =
        TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter USER =
        TextCharacter.fromCharacter('+', TextColor.ANSI.GREEN, TextColor.ANSI.DEFAULT)[0];
    TextCharacter MOB =
        TextCharacter.fromCharacter('#', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter THING =
        TextCharacter.fromCharacter('?', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.DEFAULT)[0];
    
    static char getWallChar() {
        return WALL.getCharacterString().charAt(0);
    }
    
    static char getSpaceChar() {
        return SPACE.getCharacterString().charAt(0);
    }
    
    static char getUserChar() {
        return USER.getCharacterString().charAt(0);
    }
    
    static char getMobChar() {
        return MOB.getCharacterString().charAt(0);
    }
    
    static char getThingChar() {
        return THING.getCharacterString().charAt(0);
    }
}
