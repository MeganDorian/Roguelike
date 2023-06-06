package org.itmo.mse.game.objects;

import static org.itmo.mse.constants.Proportions.backpackSize;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Backpack {
    
    private final List<Item> items = new ArrayList<>(backpackSize);
    
    @Setter
    private int selectedItem = 0;
    
    public int size() {
        return items.size();
    }
    
    public Item get(int i) {
        return items.get(i);
    }
}
