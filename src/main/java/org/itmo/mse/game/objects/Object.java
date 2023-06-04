package org.itmo.mse.game.objects;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public abstract class Object {
    @Setter
    private TerminalPosition start;
    @Setter
    private TerminalPosition end;
    
    private final TextCharacter character;
    
    public void print(TextGraphics graphics) {
        graphics.drawLine(getStart(), getEnd(), getCharacter());
    }
}
