package org.itmo.mse.game.objects;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Backpack {
    
    private final List<Item> items = new ArrayList<>(9);
    
    public int size() {
        return items.size();
    }
    
    public Item get(int i) {
        return items.get(i);
    }
}
