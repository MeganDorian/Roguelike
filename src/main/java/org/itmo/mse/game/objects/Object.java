package org.itmo.mse.game.objects;


import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public abstract class Object {
    @Setter
    private TerminalRectangle position;
    
    private final TextCharacter character;
    
    private final String name;
    
    /**
     * Prints object at his position
     *
     * @param graphics to draw object
     */
    public void print(TextGraphics graphics) {
        graphics.drawRectangle(position.position, position.size, getCharacter());
    }
    
    public List<String> getInfo() {
        return null;
    }
}
