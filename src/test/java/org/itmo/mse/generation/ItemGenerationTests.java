package org.itmo.mse.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemClass;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectDescription;
import org.itmo.mse.constants.ObjectEffect;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class ItemGenerationTests {
    
    @Test
    @RepeatedTest(10)
    public void generateItemWithoutParameters() throws IncorrectItemType {
        Item item = ItemGeneration.generateItem();
        ItemType type = item.getItemType();
        //check is item
        assertTrue(type == ItemType.MEDICAL_AID
                   || type == ItemType.ARMOR
                   || type == ItemType.WEAPON);
        
        //consistency check
        TextCharacter textCharacterForCheck = null;
        List<String> allNames = null;
        String description = null;
        
        switch (type) {
            case MEDICAL_AID -> {
                textCharacterForCheck = SpecialCharacters.MEDICAL_AID;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    allNames = ObjectNames.usualAids;
                    description = ObjectDescription.usualAid;
                } else {
                    allNames = ObjectNames.legendaryAids;
                    description = ObjectDescription.legendaryAid;
                }
            }
            case ARMOR -> {
                textCharacterForCheck = SpecialCharacters.ARMOR;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    if(item.getItemClass() == ItemClass.LIGHT) {
                        allNames = ObjectNames.usualLightArmor;
                        description = ObjectDescription.usualLightArmor;
                        assertEquals(item.getValue(), ObjectEffect.light);
                    } else {
                        allNames = ObjectNames.usualMediumArmor;
                        description = ObjectDescription.usualMediumArmor;
                        assertEquals(item.getItemClass(), ItemClass.MEDIUM);
                        assertEquals(item.getValue(), ObjectEffect.medium);
                    }
                } else {
                    allNames = ObjectNames.legendaryArmor;
                    description = ObjectDescription.legendaryHardArmor;
                    assertEquals(item.getItemClass(), ItemClass.HEAVY);
                    assertEquals(item.getValue(), ObjectEffect.heavy);
                }
            }
            case WEAPON -> {
                textCharacterForCheck = SpecialCharacters.WEAPON;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    if(item.getItemClass() == ItemClass.LIGHT) {
                        allNames = ObjectNames.usualLightWeapon;
                        description = ObjectDescription.usualLightWeapon;
                        assertEquals(item.getValue(), 5);
                    } else {
                        allNames = ObjectNames.usualMediumWeapon;
                        description = ObjectDescription.usualMediumWeapon;
                        assertEquals(item.getItemClass(), ItemClass.MEDIUM);
                        assertEquals(item.getValue(), 10);
                    }
                } else {
                    allNames = ObjectNames.legendaryWeapon;
                    description = ObjectDescription.legendaryHardWeapon;
                    assertEquals(item.getItemClass(), ItemClass.HEAVY);
                    assertEquals(item.getValue(), ObjectEffect.heavy);
                }
            }
            default -> fail();
        }
        assertEquals(item.getCharacter(), textCharacterForCheck);
        assertTrue(allNames.contains(item.getName()));
        assertEquals(item.getDescription(), description);
    }
}
