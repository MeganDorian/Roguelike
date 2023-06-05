package org.itmo.mse.game.objects.items;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Object;

public class Item extends Object {
    
    private ItemType itemType;
    
    public Item(TerminalRectangle position) {
        super(position, SpecialCharacters.ITEM);
    }
    
    protected Item(TerminalRectangle position, TextCharacter character) {
        super(position, character);
    }
}
