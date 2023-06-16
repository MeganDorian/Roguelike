package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalRectangle;
import lombok.Getter;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.generation.ItemGeneration;

import java.util.ArrayList;
import java.util.List;

public class ItemsLoader {
    
    @Getter
    private final List<Item> items = new ArrayList<>();
    
    /**
     * Get random item and set the position transferred
     */
    public void getItems(TerminalRectangle position) {
        //so far so good for the default map
        try {
            Item item = ItemGeneration.generateItem();
            item.setPosition(position);
            items.add(item);
        } catch (IncorrectItemType ex) {
            //not doing anything
        }
    }
}
