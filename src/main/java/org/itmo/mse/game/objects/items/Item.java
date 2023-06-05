package org.itmo.mse.game.objects.items;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Object;

public class Item extends Object {
    
    private ItemType itemType;
    
    public Item(TerminalPosition start) {
        super(start, start, SpecialCharacters.ITEM);
    }
    
    protected Item(TerminalPosition start, TextCharacter character) {
        super(start, start, character);
    }
}
