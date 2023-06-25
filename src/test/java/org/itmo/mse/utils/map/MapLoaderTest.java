package org.itmo.mse.utils.map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.itmo.mse.exceptions.IncorrectMapFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapLoaderTest {
    
    static Class<?> mapLoaderClass;
    
    @BeforeAll
    public static void setVar()
        throws ClassNotFoundException {
        mapLoaderClass = Class.forName("org.itmo.mse.utils.map.MapLoader");
    }
    
    @Test
    public void getPlayerPositionTest()
        throws NoSuchMethodException {
        Method getPlayerPosition =
            mapLoaderClass.getDeclaredMethod("getPlayerPosition", String.class);
        getPlayerPosition.setAccessible(true);
        assertDoesNotThrow(() -> getPlayerPosition.invoke(MapLoader.class, "@@@@"));
        assertDoesNotThrow(() -> getPlayerPosition.invoke(MapLoader.class, "@@+@"));
        IncorrectMapFormatException thrown = (IncorrectMapFormatException) assertThrows(InvocationTargetException.class,
            () -> getPlayerPosition.invoke(MapLoader.class,
            "@@+@")).getCause();
        assertEquals("It can't be two players on map", thrown.getMessage());
    }
}
