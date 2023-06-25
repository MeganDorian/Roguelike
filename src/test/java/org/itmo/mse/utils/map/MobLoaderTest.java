package org.itmo.mse.utils.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class MobLoaderTest {
    
    MobLoader mobLoader = new MobLoader();
    
    @Test
    public void getMobsTest() {
        Random random = new Random();
        TerminalRectangle position = new TerminalRectangle(Math.abs(random.nextInt()),
            Math.abs(random.nextInt()), Math.abs(random.nextInt()), Math.abs(random.nextInt()));
        assertEquals(0, mobLoader.getMobs().size());
        mobLoader.getMobs(position);
        assertEquals(1, mobLoader.getMobs().size());
        assertEquals(position, mobLoader.getMobs().get(0).getPosition());
    }
}
