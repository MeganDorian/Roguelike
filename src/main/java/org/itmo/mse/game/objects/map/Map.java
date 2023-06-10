package org.itmo.mse.game.objects.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Item;
import org.itmo.mse.game.objects.Mob;
import org.itmo.mse.game.objects.Object;

@Getter
public class Map extends Object {
    
    private final List<Wall> walls;
    
    private List<Mob> mobs;
    
    private final List<Item> items;
    
    private final TerminalPosition exit;
    private final TerminalPosition start;
    
    private Map(TerminalRectangle border, List<Wall> walls, List<Mob> mobs, List<Item> items,
                TerminalPosition exit, TerminalPosition start) {
        super(border, SpecialCharacters.WALL, ObjectNames.map);
        this.walls = walls;
        this.mobs = mobs;
        this.items = items;
        this.exit = exit;
        this.start = start;
    }
    
    public static MapBuilder builder() {
        return new MapBuilder();
    }
    
    /**
     * Print map
     *
     * @param graphics to draw object
     */
    @Override
    public void print(TextGraphics graphics) {
        walls.forEach(wall -> wall.print(graphics));
        items.forEach(item -> item.print(graphics));
        mobs.forEach(mob -> mob.print(graphics));
    }
    
    /**
     * Builder for build map
     */
    public static class MapBuilder {
        private TerminalRectangle position;
        
        private List<Wall> walls;
        private List<Mob> mobs;
        
        private List<Item> items;
        
        private TerminalPosition exit;
        
        private TerminalPosition start;
        
        /**
         * Set walls
         *
         * @param walls
         * @return
         */
        public MapBuilder walls(List<Wall> walls) {
            this.walls = walls;
            return this;
        }
        
        /**
         * Set mobs
         *
         * @param mobs
         * @return
         */
        public MapBuilder mobs(List<Mob> mobs) {
            this.mobs = mobs;
            return this;
        }
        
        /**
         * Set items
         * @param items
         * @return
         */
        public MapBuilder things(List<Item> items) {
            this.items = items;
            return this;
        }
        
        /**
         *  Set border
         *
         * @param position
         * @return
         */
        public MapBuilder border(TerminalRectangle position) {
            this.position = position;
            return this;
        }
        
        /**
         * Set exit
         *
         * @param exit
         * @return
         */
        public MapBuilder exit(TerminalPosition exit) {
            this.exit = exit;
            return this;
        }
        
        /**
         * Set start
         *
         * @param start
         * @return
         */
        public MapBuilder start(TerminalPosition start) {
            this.start = start;
            return this;
        }
        
        /**
         * Build map with the set parameters
         * @return
         */
        public Map build() {
            return new Map(position, walls, mobs, items, exit, start);
        }
    }
}
