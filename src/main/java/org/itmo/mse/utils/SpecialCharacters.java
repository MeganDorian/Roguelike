package org.itmo.mse.utils;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SpecialCharacters {
    @Getter
    private static final TextCharacter WALL =
        TextCharacter.fromCharacter('@', TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)[0];
    @Getter
    private final TextCharacter SPACE =
        TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)[0];
    @Getter
    private final TextCharacter USER =
        TextCharacter.fromCharacter('+', TextColor.ANSI.GREEN, TextColor.ANSI.DEFAULT)[0];
    @Getter
    private final TextCharacter MOB =
        TextCharacter.fromCharacter('#', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    @Getter
    private final TextCharacter THING =
        TextCharacter.fromCharacter('?', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.DEFAULT)[0];
    
    public char getWallChar() {
        return WALL.getCharacterString().charAt(0);
    }
    
    public char getSpaceChar() {
        return SPACE.getCharacterString().charAt(0);
    }
    
    public char getUserChar() {
        return USER.getCharacterString().charAt(0);
    }
    
    public char getMobChar() {
        return MOB.getCharacterString().charAt(0);
    }
    
    public char getThingChar() {
        return THING.getCharacterString().charAt(0);
    }
}
