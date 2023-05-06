package org.mydi;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {
    @Test
    public void inject_privateField()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchFieldException {
        // arrange
        Properties properties = new Properties();
        properties.put(TestInterface.class.getName(), TestImpl.class.getName());

        var injector = new Injector(properties);

        // act
        var bean = injector.inject(new TestClass());

        // assert
        var field = bean.getClass().getDeclaredField("testInterface");
        field.setAccessible(true);
        var actual = field.get(bean);
        var expected = new TestImpl();

        assertEquals(expected, actual);
    }

    @Test
    public void inject_notAnnotatedField()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchFieldException {
        // arrange
        Properties properties = new Properties();
        properties.put(TestInterface.class.getName(), TestImpl.class.getName());

        var injector = new Injector(properties);

        // act
        var bean = injector.inject(new TestClass());

        // assert
        var field = bean.getClass().getDeclaredField("testInterface2");
        field.setAccessible(true);
        var actual = field.get(bean);

        assertEquals(null, actual);
    }

    @Test
    public void inject_undefined()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchFieldException {
        // arrange
        Properties properties = new Properties();

        var injector = new Injector(properties);

        // act
        assertThrows(NoSuchElementException.class, () -> injector.inject(new TestClass()));
    }
}