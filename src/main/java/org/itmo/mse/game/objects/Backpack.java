package org.itmo.mse.game.objects;

import static org.itmo.mse.constants.Proportions.backpackSize;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Backpack {
    
    private final List<Item> items = new ArrayList<>(backpackSize);
    
    public int size() {
        return items.size();
    }
    
    public Item get(int i) {
        return items.get(i);
    }
}
