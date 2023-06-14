package org.itmo.mse.utils.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import lombok.Getter;

import java.util.HashMap;

public class WallsLoader {
    
    @Getter
    private final HashMap<TerminalPosition, TerminalPosition> walls = new HashMap<>();
    
    /**
     * Analyses the transmitted column for the presence of vertical walls and adds them to the list
     *
     * @return is exists vertical wall
     */
    private boolean addVerticalWall(TerminalPosition position, int column) {
        // if vertical wall already exists
        position = position.withRelativeRow(-1);
        if (walls.containsKey(position) && walls.get(position).getColumn() == column) {
            TerminalPosition start = walls.remove(position);
            walls.put(position.withRelativeRow(1), start);
            return true;
        }
        return false;
    }
    
    /**
     * Analyses the transmitted string for the presence of vertical walls and adds them to the list
     *
     * @return is exists horizontal wall
     */
    private boolean addHorizontalWall(TerminalPosition position, int height) {
        // if horizontal wall already exists
        position = position.withRelativeColumn(-1);
        if (walls.containsKey(position) && walls.get(position).getRow() == height) {
            TerminalPosition start = walls.remove(position);
            walls.put(position.withRelativeColumn(1), start);
            return true;
        }
        return false;
    }
    
    /**
     * Add new wall to the list or increase already added wall
     */
    public void getWalls(TerminalRectangle position, int i, int height) {
        if (!addVerticalWall(position.position, i) && !addHorizontalWall(position.position, height)) {
            walls.put(position.position, position.position);
        }
    }
}
