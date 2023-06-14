package org.itmo.mse.generation;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import org.itmo.mse.constants.*;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;

import java.util.List;

import static org.itmo.mse.constants.ItemCharacteristic.LEGENDARY;
import static org.itmo.mse.constants.ItemCharacteristic.USUAL;

public class ItemGeneration extends Generation {
    
    /**
     * Generates item parameters of a given type
     *
     * @param type -- type item from enum ItemType
     * @return random item of a given type
     * @throws IncorrectItemType -- if generation for the type passed is not specified
     */
    public static Item generateItem(ItemType type) throws IncorrectItemType {
        TerminalRectangle position = generatePosition();
        TextCharacter character;
        List<String> allNames;
        ItemCharacteristic characteristic;
        ItemClass itemClass = null;
        int value = 1;
        String description;
        
        //generate characteristic item
        if (rand.nextInt(100) < Proportions.legendaryItem * 100) {
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
                        itemClass = ItemClass.LIGHT;
                        value = ObjectEffect.light;
                    } else {
                        allNames = ObjectNames.usualMediumArmor;
                        description = ObjectDescription.usualMediumArmor;
                        itemClass = ItemClass.MEDIUM;
                        value = ObjectEffect.medium;
                    }
                } else {
                    allNames = ObjectNames.legendaryArmor;
                    description = ObjectDescription.legendaryHardArmor;
                    itemClass = ItemClass.HEAVY;
                    value = ObjectEffect.heavy;
                }
            }
            case WEAPON -> {
                character = SpecialCharacters.WEAPON;
                if (characteristic == USUAL) {
                    if (rand.nextInt(100) < 50) {
                        allNames = ObjectNames.usualLightWeapon;
                        description = ObjectDescription.usualLightWeapon;
                        itemClass = ItemClass.LIGHT;
                        value = ObjectEffect.light;
                    } else {
                        allNames = ObjectNames.usualMediumWeapon;
                        description = ObjectDescription.usualMediumWeapon;
                        itemClass = ItemClass.MEDIUM;
                        value = ObjectEffect.medium;
                    }
                } else {
                    allNames = ObjectNames.legendaryWeapon;
                    description = ObjectDescription.legendaryHardWeapon;
                    itemClass = ItemClass.HEAVY;
                    value = ObjectEffect.heavy;
                }
            }
            default -> throw new IncorrectItemType("Item type \"" + type + "\" not found!");
        }
        
        return new Item(position,
                        character,
                        allNames.get(rand.nextInt(allNames.size())),
                        characteristic,
                        type,
                        itemClass,
                        description,
                        value);
    }
    
    /**
     * Generates item parameters
     *
     * @return random item
     * @throws IncorrectItemType -- if generation for the type passed is not specified
     */
    public static Item generateItem() throws IncorrectItemType {
        int generateTypeItem = rand.nextInt(100);
        if (generateTypeItem < Proportions.aids * 100) {
            return generateItem(ItemType.MEDICAL_AID);
        } else if (generateTypeItem < (Proportions.aids + Proportions.armor) * 100) {
            return generateItem(ItemType.ARMOR);
        } else {
            return generateItem(ItemType.WEAPON);
        }
    }
    
}
