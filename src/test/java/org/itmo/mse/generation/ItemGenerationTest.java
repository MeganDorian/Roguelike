package org.itmo.mse.generation;

import static org.itmo.mse.constants.ItemClass.LIGHT;
import static org.itmo.mse.constants.ItemType.ARMOR;
import static org.itmo.mse.constants.ItemType.MEDICAL_AID;
import static org.itmo.mse.constants.ItemType.WEAPON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TextCharacter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;
import org.itmo.mse.constants.ItemCharacteristic;
import org.itmo.mse.constants.ItemClass;
import org.itmo.mse.constants.ItemType;
import org.itmo.mse.constants.ObjectDescription;
import org.itmo.mse.constants.ObjectEffect;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.Item;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ItemGenerationTest {
    Class<?> itemGenerator;
    
    public ItemGenerationTest() throws ClassNotFoundException {
        itemGenerator = Class.forName("org.itmo.mse.generation.ItemGeneration");
    }
    
    @ParameterizedTest
    @MethodSource("methodsAndParametersType")
    public void generationByType(String method, ItemType param, int count)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method generatorDeclaredMethod = itemGenerator.getDeclaredMethod(method, param.getClass());
        generatorDeclaredMethod.setAccessible(true);
        for(int i = 0; i < count; i++){
            checkItemByType((Item) generatorDeclaredMethod.invoke(ItemGeneration.class,
                param), param);
        }
    }
    
    @ParameterizedTest
    @MethodSource("methodsAndParametersTypeAndPosition")
    public void generationByTypeAndPosition(String method, ItemType param,
                                            TerminalRectangle position, int count)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method generatorDeclaredMethod = itemGenerator.getDeclaredMethod(method, param.getClass(),
            position.getClass());
        generatorDeclaredMethod.setAccessible(true);
        for(int i = 0; i < count; i++){
            checkItemByTypeAndPosition((Item) generatorDeclaredMethod.invoke(ItemGeneration.class,
                param, position), param, position);
        }
    }
    
    private void checkItemByType(Item item, ItemType itemType) {
        String description = null;
        List<String> allNames = null;
        TextCharacter character = null;
        switch (itemType) {
            case WEAPON -> {
                character = SpecialCharacters.WEAPON;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    if (item.getItemClass() == LIGHT) {
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
            case ARMOR -> {
                character = SpecialCharacters.ARMOR;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    if(item.getItemClass() == LIGHT) {
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
            case MEDICAL_AID -> {
                character = SpecialCharacters.MEDICAL_AID;
                if (item.getItemCharacteristic() == ItemCharacteristic.USUAL) {
                    allNames = ObjectNames.usualAids;
                    description = ObjectDescription.usualAid;
                } else {
                    allNames = ObjectNames.legendaryAids;
                    description = ObjectDescription.legendaryAid;
                }
            }
        }
        assertEquals(item.getItemType(), itemType);
        assertEquals(description, item.getDescription());
        assertEquals(character, item.getCharacter());
        assertTrue(allNames.contains(item.getName()));
    }
    
    private void checkItemByTypeAndPosition(Item item, ItemType itemType,
                                            TerminalRectangle position) {
        checkItemByType(item, itemType);
        assertEquals(position, item.getPosition());
    }
    
    private static Stream<? extends Arguments> methodsAndParametersType() {
        return Stream.of(Arguments.of("generateWeapon", WEAPON, 10),
            Arguments.of("generateArmor", ARMOR, 10),
            Arguments.of("generateMedicalAid", MEDICAL_AID, 10),
            Arguments.of("generateItem", WEAPON, 10),
            Arguments.of("generateItem", ARMOR, 10),
            Arguments.of("generateItem", MEDICAL_AID, 10));
    }
    
    private static Stream<? extends Arguments> methodsAndParametersTypeAndPosition() {
        return Stream.of(Arguments.of("generateUsualWeapon", WEAPON,
                new TerminalRectangle(10, 10, 20, 30), 10),
            Arguments.of("generateUsualArmor", ARMOR,
                new TerminalRectangle(10, 10, 20, 30), 10));
    }
}
