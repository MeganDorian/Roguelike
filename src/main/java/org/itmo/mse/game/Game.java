package org.itmo.mse.game;

import com.googlecode.lanterna.TerminalPosition;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.game.objects.Player;
import org.itmo.mse.game.objects.map.Map;

@Getter
public class Game {
    
    private int currentLevel = 1;
    
    @Setter
    private Map levelMap;
    
    @Setter
    private Player player;
    
    public Game() {
        player = new Player(new TerminalPosition(0, 0), new TerminalPosition(0, 0));
    }
}
