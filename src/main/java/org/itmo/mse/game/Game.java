package org.itmo.mse.game;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.input.KeyType;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;
import org.itmo.mse.game.objects.map.Wall;

@Getter
public class Game {
    
    private int dungeonLevel = 1;
    
    @Setter
    private Map levelMap;
    
    @Setter
    private Player player;
    
    public Game() {
        player = new Player(new TerminalRectangle(0, 0, 0, 0));
    }
    
    public void updatePlayerPosition(KeyType direction) {
    
    }
    
    private boolean checkIfWallAtPosition(TerminalPosition position) {
        for (Wall wall : levelMap.getWalls()) {
        
        }
        return false;
    }
}
