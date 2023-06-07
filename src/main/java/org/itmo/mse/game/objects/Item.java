package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemClass;
import org.itmo.mse.constants.ItemType;

@Getter
public class Item extends Object {
    
    private final ItemCharacteristic itemCharacteristic;
    
    private final ItemType itemType;
    
    private final ItemClass itemClass;
    
    protected String description;
    @Setter
    private int value;
    
    public Item(TerminalRectangle position, TextCharacter character, String name,
                ItemCharacteristic characteristic, ItemType type, ItemClass itemClass,
                String description, int value) {
        super(position, character, name);
        this.description = description;
        this.itemCharacteristic = characteristic;
        this.itemType = type;
        this.itemClass = itemClass;
        this.value = value;
    }
    
    @Override
    public List<String> getInfo() {
        return List.of(getName(), description, itemCharacteristic.name(),
                       (itemClass != null ? itemClass.name() : ""));
    }
}
