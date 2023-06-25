package org.itmo.mse.utils.map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.googlecode.lanterna.TerminalPosition;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WallsLoaderTest {
    
    static Class<?> wallsLoaderClass;
    static Object wallsLoaderObject;
    static HashMap<TerminalPosition, TerminalPosition> walls;
    
    @BeforeAll
    public static void setData() throws ClassNotFoundException, NoSuchMethodException,
                         InstantiationException,
        IllegalAccessException, InvocationTargetException {
        wallsLoaderClass = Class.forName("org.itmo.mse.utils.map.WallsLoader");
        wallsLoaderObject = wallsLoaderClass.newInstance();
        walls = (HashMap<TerminalPosition, TerminalPosition>) wallsLoaderClass
            .getDeclaredMethod("getWalls").invoke(wallsLoaderObject);
        walls.put(new TerminalPosition(15, 0), new TerminalPosition(0, 0));
        walls.put(new TerminalPosition(0, 7), new TerminalPosition(0, 2));
    }
    
    @Test
    public void addVerticalWallFalseTest()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addVerticalWall =
            wallsLoaderClass.getDeclaredMethod("addVerticalWall", TerminalPosition.class,
                int.class);
        addVerticalWall.setAccessible(true);
        assertFalse((Boolean) addVerticalWall.invoke(wallsLoaderObject,
            new TerminalPosition(0, 8), 1));
    }
    
    @Test
    public void addVerticalWallTrueTest()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addVerticalWall =
            wallsLoaderClass.getDeclaredMethod("addVerticalWall", TerminalPosition.class,
                int.class);
        addVerticalWall.setAccessible(true);
        assertTrue((Boolean) addVerticalWall.invoke(wallsLoaderObject,
            new TerminalPosition(0, 8), 0));
    }
    
    @Test
    public void addHorizontalWallFalseTest()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addHorizontalWall =
            wallsLoaderClass.getDeclaredMethod("addHorizontalWall", TerminalPosition.class,
                int.class);
        addHorizontalWall.setAccessible(true);
        assertFalse((Boolean) addHorizontalWall.invoke(wallsLoaderObject,
            new TerminalPosition(16, 0), 1));
    }
    
    @Test
    public void addHorizontalWallTrueTest()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addHorizontalWall =
            wallsLoaderClass.getDeclaredMethod("addHorizontalWall", TerminalPosition.class,
                int.class);
        addHorizontalWall.setAccessible(true);
        assertTrue((Boolean) addHorizontalWall.invoke(wallsLoaderObject,
            new TerminalPosition(16, 0), 0));
    }
    
}
