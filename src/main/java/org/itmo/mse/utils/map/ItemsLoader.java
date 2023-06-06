package org.itmo.mse.utils.map;

import static org.itmo.mse.generation.ItemGeneration.generateItem;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;

public class ItemsLoader {
    
    @Getter
    private final List<Item> items = new ArrayList<>();
    
    public void getItems(TerminalRectangle position) {
        //so far so good for the default map
        try {
            Item item = generateItem();
            item.setPosition(position);
            items.add(item);
        } catch (IncorrectItemType ex) {
            //not doing anything
        }
    }
}
