package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Mob;
import org.itmo.mse.game.objects.Object;
import org.itmo.mse.game.objects.items.Item;

@Getter
public class Map extends Object {
//    private final int width;
//
//    private final int height;
    
    private final List<Wall> walls;
    
    private final List<Mob> mobs;
    
    private final List<Item> items;
    
    private final TerminalPosition exit;
    
    private Map(TerminalRectangle border, List<Wall> walls, List<Mob> mobs, List<Item> items,
                TerminalPosition exit) {
        super(border, SpecialCharacters.WALL);
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
        private TerminalRectangle position;
        
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
        
        public MapBuilder border(TerminalRectangle position) {
            this.position = position;
            return this;
        }
        
        public MapBuilder exit(TerminalPosition exit) {
            this.exit = exit;
            return this;
        }
        
        public Map build() {
            return new Map(position, walls, mobs, items, exit);
        }
    }
}
