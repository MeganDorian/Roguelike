package org.itmo.mse.game.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static org.itmo.mse.constants.Proportions.backpackSize;

@Getter
public class Backpack {
    
    private final List<Item> items = new ArrayList<>(backpackSize);
    
    @Setter
    private int selectedItemIndex = 0;
    
    /**
     * Get the number of items in the backpack
     *
     * @return number of items
     */
    public int size() {
        return items.size();
    }
    
    /**
     * Get item from the backpack by index
     *
     * @param i -- index
     * @return item by index
     */
    public Item get(int i) {
        return items.get(i);
    }
}
