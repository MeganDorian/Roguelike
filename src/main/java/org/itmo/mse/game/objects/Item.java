package org.itmo.mse.game.objects;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemClass;
import org.itmo.mse.constants.ItemType;

import java.util.List;

@Getter
public class Item extends Object {
    
    private final ItemCharacteristic itemCharacteristic;
    
    private final ItemType itemType;
    
    private final ItemClass itemClass;
    
    protected String description;
    @Setter
    private int value;
    
    public Item(TerminalRectangle position, TextCharacter character, String name, ItemCharacteristic characteristic,
                ItemType type, ItemClass itemClass, String description, int value) {
        super(character, name, position);
        this.description = description;
        this.itemCharacteristic = characteristic;
        this.itemType = type;
        this.itemClass = itemClass;
        this.value = value;
    }
    
    public Item(Item state) {
        super(state.getCharacter(), state.getName(), state.getPosition());
        this.description = state.description;
        this.itemCharacteristic = state.itemCharacteristic;
        this.itemType = state.itemType;
        this.itemClass = state.itemClass;
        this.value = state.value;
    }
    
    /**
     * Get info about item
     *
     * @return list of lines which includes full information about item to print
     */
    @Override
    public List<String> getInfo() {
        return List.of(getName(), description, itemCharacteristic.name(), (itemClass != null ? itemClass.name() : ""));
    }
}
