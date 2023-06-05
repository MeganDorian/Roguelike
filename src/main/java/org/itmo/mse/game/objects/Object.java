package org.itmo.mse.game.objects;


import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public abstract class Object {
    @Setter
    private TerminalRectangle position;
    
    private final TextCharacter character;
    
    public void print(TextGraphics graphics) {
        graphics.drawRectangle(position.position, position.size, getCharacter());
    }
}
