package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import org.itmo.mse.constants.ItemType;

public class Item extends Object {
    
    private ItemType itemType;
    
    protected String description;
    
    public Item(TerminalRectangle position, TextCharacter character, String name, ItemType type,
                String description) {
        super(position, character, name);
        this.description = description;
        this.itemType = type;
    }
    
    @Override
    public List<String> getInfo() {
        return List.of(getName(), description);
    }
}
