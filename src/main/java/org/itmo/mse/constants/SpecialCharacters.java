package org.itmo.mse.constants;

import static com.googlecode.lanterna.Symbols.DIAMOND;
import static com.googlecode.lanterna.Symbols.DOUBLE_LINE_CROSS;
import static com.googlecode.lanterna.Symbols.FACE_BLACK;
import static com.googlecode.lanterna.Symbols.FACE_WHITE;
import static com.googlecode.lanterna.Symbols.HEART;
import static com.googlecode.lanterna.Symbols.INVERSE_BULLET;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public interface SpecialCharacters {
    TextCharacter DELIMITER =
        TextCharacter.fromCharacter('-', TextColor.ANSI.YELLOW, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SELECTED_ITEM =
        TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.WHITE)[0];
    TextCharacter WALL =
        TextCharacter.fromCharacter('@', TextColor.ANSI.WHITE, TextColor.ANSI.DEFAULT)[0];
    TextCharacter SPACE =
        TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter PLAYER =
        TextCharacter.fromCharacter('+', TextColor.ANSI.GREEN, TextColor.ANSI.DEFAULT)[0];
    TextCharacter MOB =
        TextCharacter.fromCharacter('#', TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter PASSIVE_MOB =
        TextCharacter.fromCharacter(INVERSE_BULLET, TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter AGGRESSIVE_MOB =
        TextCharacter.fromCharacter(FACE_BLACK, TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter COWARDLY_MOB =
        TextCharacter.fromCharacter(FACE_WHITE, TextColor.ANSI.RED, TextColor.ANSI.DEFAULT)[0];
    TextCharacter ITEM =
        TextCharacter.fromCharacter('?', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter MEDICAL_AID =
        TextCharacter.fromCharacter(HEART, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.DEFAULT)[0];
    TextCharacter ARMOR =
        TextCharacter.fromCharacter(DIAMOND, TextColor.ANSI.MAGENTA, TextColor.ANSI.DEFAULT)[0];
    TextCharacter WEAPON = TextCharacter.fromCharacter(DOUBLE_LINE_CROSS, TextColor.ANSI.CYAN,
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
