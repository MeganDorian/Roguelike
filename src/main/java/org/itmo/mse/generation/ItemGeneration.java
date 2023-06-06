package org.itmo.mse.generation;

import static org.itmo.mse.constants.ItemCharacteristic.LEGENDARY;
import static org.itmo.mse.constants.ItemCharacteristic.USUAL;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import java.util.Random;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectDescription;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;

public class ItemGeneration {
    
    static Random rand = new Random();
    
    /**
     * Generates item parameters of a given type
     *
     * @param type -- type item from enum ItemType
     * @return random item of a given type
     * @throws IncorrectItemType -- if generation for the type passed is not specified
     */
    public static Item generateItem(ItemType type) throws IncorrectItemType {
        TerminalRectangle position = MapGeneration.generatePosition();
        TextCharacter character;
        List<String> allNames;
        ItemCharacteristic characteristic;
        String description;
        
        //generate characteristic item
        if(rand.nextInt(100) < Proportions.legendaryItem * 100) {
            characteristic = LEGENDARY;
        } else {
            characteristic = USUAL;
        }
        
        //generate parameters item from type and characteristic
        switch (type) {
            case MEDICAL_AID -> {
                character = SpecialCharacters.MEDICAL_AID;
                if (characteristic == USUAL) {
                    allNames = ObjectNames.usualAids;
                    description = ObjectDescription.usualAid;
                } else {
                    allNames = ObjectNames.legendaryAids;
                    description = ObjectDescription.legendaryAid;
                }
            }
            case ARMOR -> {
                character = SpecialCharacters.ARMOR;
                if (characteristic == USUAL) {
                    if (rand.nextInt(100) < 50) {
                        allNames = ObjectNames.usualLightArmor;
                        description = ObjectDescription.usualLightArmor;
                    } else {
                        allNames = ObjectNames.usualMediumArmor;
                        description = ObjectDescription.usualMediumArmor;
                    }
                } else {
                    allNames = ObjectNames.legendaryArmor;
                    description = ObjectDescription.legendaryHardArmor;
                }
            }
            case WEAPON -> {
                character = SpecialCharacters.WEAPON;
                if (characteristic == USUAL) {
                    if (rand.nextInt(100) < 50) {
                        allNames = ObjectNames.usualLightWeapon;
                        description = ObjectDescription.usualLightWeapon;
                    } else {
                        allNames = ObjectNames.usualMediumWeapon;
                        description = ObjectDescription.usualMediumWeapon;
                    }
                } else {
                    allNames = ObjectNames.legendaryWeapon;
                    description = ObjectDescription.legendaryHardWeapon;
                }
            }
            default -> throw new IncorrectItemType("Item type \"" + type + "\" not found!");
        }
        
        return new Item(position, character, allNames.get(rand.nextInt(allNames.size())),
            characteristic, type, description);
    }
}
