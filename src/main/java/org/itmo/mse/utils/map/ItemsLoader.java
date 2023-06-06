package org.itmo.mse.utils.map;

import static org.itmo.mse.constants.SpecialCharacters.MEDICAL_AID;

import com.googlecode.lanterna.TerminalRectangle;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectDescription;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.game.objects.Item;

public class ItemsLoader {
    
    @Getter
    private final List<Item> items = new ArrayList<>();
    
    public void getItems(TerminalRectangle position) {
        // if there going to be different things
        items.add(
            new Item(position, MEDICAL_AID, ObjectNames.usualAids.get(2), ItemCharacteristic.USUAL,
                     ItemType.MEDICAL_AID, ObjectDescription.usualAid));
    }
}
