package org.itmo.mse.generation;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import lombok.experimental.UtilityClass;
import org.itmo.mse.constants.*;
import org.itmo.mse.exceptions.IncorrectItemType;
import org.itmo.mse.game.objects.Item;

import java.util.List;

import static org.itmo.mse.constants.ItemCharacteristic.LEGENDARY;
import static org.itmo.mse.constants.ItemCharacteristic.USUAL;

@UtilityClass
public class ItemGeneration extends Generation {
    private List<String> allNames;
    private String description;
    
    private ItemCharacteristic characteristic;
    
    /**
     * Generates item parameters
     *
     * @return random item
     * @throws IncorrectItemType -- if generation for the type passed is not specified
     */
    public Item generateItem() throws IncorrectItemType {
        int generateTypeItem = rand.nextInt(100);
        if (generateTypeItem < Proportions.aids * 100) {
            return generateItem(ItemType.MEDICAL_AID);
        } else if (generateTypeItem < (Proportions.aids + Proportions.armor) * 100) {
            return generateItem(ItemType.ARMOR);
        } else {
            return generateItem(ItemType.WEAPON);
        }
    }
    
    /**
     * Generates item parameters of a given type
     *
     * @param type -- type item from enum ItemType
     * @return random item of a given type
     * @throws IncorrectItemType -- if generation for the type passed is not specified
     */
    public Item generateItem(ItemType type) throws IncorrectItemType {
        //generate characteristic item
        characteristic = rand.nextInt(100) < Proportions.legendaryItem * 100 ? LEGENDARY : USUAL;
        
        //generate parameters item from type and characteristic
        return switch (type) {
            case MEDICAL_AID -> generateMedicalAid(type);
            case ARMOR -> generateArmor(type);
            case WEAPON -> generateWeapon(type);
            default -> throw new IncorrectItemType("Item type \"" + type + "\" not found!");
        };
    }
    
    
    private Item generateMedicalAid(ItemType type) {
        TerminalRectangle position = generatePosition();
        TextCharacter character = SpecialCharacters.MEDICAL_AID;
        if (characteristic == USUAL) {
            allNames = ObjectNames.usualAids;
            description = ObjectDescription.usualAid;
        } else {
            allNames = ObjectNames.legendaryAids;
            description = ObjectDescription.legendaryAid;
        }
        return new Item(position,
                        character,
                        allNames.get(rand.nextInt(allNames.size())),
                        characteristic,
                        type,
                        null,
                        description,
                        1);
    }
    
    private Item generateUsualArmor(ItemType type, TerminalRectangle position) {
        ItemClass itemClass;
        int value;
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
        return new Item(position,
                        SpecialCharacters.ARMOR,
                        allNames.get(rand.nextInt(allNames.size())),
                        characteristic,
                        type,
                        itemClass,
                        description,
                        value);
    }
    
    private Item generateArmor(ItemType type) {
        TerminalRectangle position = generatePosition();
        if (characteristic == USUAL) {
            return generateUsualArmor(type, position);
        }
        return new Item(position,
                        SpecialCharacters.ARMOR,
                        ObjectNames.legendaryArmor.get(rand.nextInt(allNames.size())),
                        characteristic,
                        type,
                        ItemClass.HEAVY,
                        ObjectDescription.legendaryHardArmor,
                        ObjectEffect.heavy);
    }
    
    private Item generateUsualWeapon(ItemType type, TerminalRectangle position) {
        ItemClass itemClass;
        int value;
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
        return new Item(position,
                        SpecialCharacters.ARMOR,
                        allNames.get(rand.nextInt(allNames.size())),
                        characteristic,
                        type,
                        itemClass,
                        description,
                        value);
    }
    
    private Item generateWeapon(ItemType type) {
        TerminalRectangle position = generatePosition();
        if (characteristic == USUAL) {
            return generateUsualWeapon(type, position);
        } else {
            return new Item(position,
                            SpecialCharacters.WEAPON,
                            ObjectNames.legendaryWeapon.get(rand.nextInt(ObjectNames.legendaryAids.size())),
                            characteristic,
                            type,
                            ItemClass.HEAVY,
                            ObjectDescription.legendaryHardWeapon,
                            ObjectEffect.heavy);
        }
    }
}
