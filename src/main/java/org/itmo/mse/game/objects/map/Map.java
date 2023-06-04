package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.game.objects.Mob;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.utils.SpecialCharacters;

@Getter
public class Map extends Object {
    private final int width;
    
    private final int height;
    
    private final List<Wall> walls;
    
    private final List<Mob> mobs;
    
    private final List<Item> items;
    
    private final TerminalPosition exit;
    
    private Map(TerminalPosition start, TerminalPosition end, List<Wall> walls, List<Mob> mobs,
                List<Item> items, TerminalPosition exit) {
        super(start, end, SpecialCharacters.WALL);
        width = end.getColumn();
        height = start.getRow();
        this.walls = walls;
        this.mobs = mobs;
        this.items = items;
        this.exit = exit;
    }
    
    public static MapBuilder builder() {
        return new MapBuilder();
    }
    
    @Override
    public void print(TextGraphics graphics) {
        walls.forEach(wall -> wall.print(graphics));
        items.forEach(item -> item.print(graphics));
        mobs.forEach(mob -> mob.print(graphics));
    }
    
    public static class MapBuilder {
        private TerminalPosition start;
        
        private TerminalPosition end;
        
        private List<Wall> walls;
        private List<Mob> mobs;
        
        private List<Item> items;
        
        private TerminalPosition exit;
        
        public MapBuilder walls(List<Wall> walls) {
            this.walls = walls;
            return this;
        }
        
        public MapBuilder mobs(List<Mob> mobs) {
            this.mobs = mobs;
            return this;
        }
        
        public MapBuilder things(List<Item> items) {
            this.items = items;
            return this;
        }
        
        public MapBuilder start(TerminalPosition start) {
            this.start = start;
            return this;
        }
        
        public MapBuilder end(TerminalPosition end) {
            this.end = end;
            return this;
        }
        
        public MapBuilder exit(TerminalPosition exit) {
            this.exit = exit;
            return this;
        }
        
        public Map build() {
            return new Map(start, end, walls, mobs, items, exit);
        }
    }
}
