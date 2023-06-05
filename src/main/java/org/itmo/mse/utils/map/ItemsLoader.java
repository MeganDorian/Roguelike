package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalPosition;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.game.objects.items.Item;

public class ItemsLoader {
    
    @Getter
    private final List<Item> items = new ArrayList<>();
    
    public void getItems(TerminalPosition position) {
        // if there going to be different things
        items.add(new Item(position));
    }
}
