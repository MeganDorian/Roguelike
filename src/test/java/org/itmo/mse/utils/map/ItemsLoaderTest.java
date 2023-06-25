package org.itmo.mse.utils.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class ItemsLoaderTest {
    
    ItemsLoader itemsLoader = new ItemsLoader();
    
    @Test
    public void getItemsTest() {
        Random random = new Random();
        TerminalRectangle position = new TerminalRectangle(Math.abs(random.nextInt()),
            Math.abs(random.nextInt()), Math.abs(random.nextInt()), Math.abs(random.nextInt()));
        assertEquals(0, itemsLoader.getItems().size());
        itemsLoader.getItems(position);
        assertEquals(1, itemsLoader.getItems().size());
        assertEquals(position, itemsLoader.getItems().get(0).getPosition());
    }
}
