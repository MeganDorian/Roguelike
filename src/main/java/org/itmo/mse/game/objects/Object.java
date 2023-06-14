package org.itmo.mse.game.objects;


import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.itmo.mse.constants.SpecialCharacters;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class Object {
    @Setter
    protected TerminalRectangle position;
    
    private final TextCharacter character;
    
    private final String name;
    
    /**
     * Prints object at his position
     *
     * @param graphics to draw object
     */
    public void print(TextGraphics graphics) {
        graphics.fillRectangle(position.position, position.size, SpecialCharacters.SPACE);
        graphics.drawRectangle(position.position, position.size, character);
    }
    
    /**
     * Get info about object
     *
     * @return info
     */
    public List<String> getInfo() {
        return List.of();
    }
}
