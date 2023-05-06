package org.mydi;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;

public class Injector {
    private final Properties properties;

    /**
     * @param properties Properties instance with defined implementations names.
     */
    public Injector(Properties properties) {
        this.properties = properties;
    }

    /**
     * Injects implementations defined in properties into fields annotated with @AutoInjectable attribute.
     * Properties should be defined with pattern
     * {TypeName}={ImplName}
     *
     * @param target Target class instance that should be filled with implementations.
     * @param <TClass> Type of target class.
     * @return Filled target class instance.
     * @throws NoSuchElementException Thrown if there is no definition of one or more field types.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <TClass> TClass inject(TClass target)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        var fields = getAutoInjectableFields(target);
        for (var field : fields) {
            field.setAccessible(true);
            var implName = getImplName(field);
            field.set(target, getImpl(implName));
            field.setAccessible(false);
        }
        return target;
    }

    private static <TClass> Field[] getAutoInjectableFields(TClass target) {
        var fields = target.getClass().getDeclaredFields();
        return Arrays.stream(fields).filter(field ->
            field.isAnnotationPresent(AutoInjectable.class)).toArray(Field[]::new);
    }

    private String getImplName(Field field) {
        var fieldTypeName = field.getType().getName();
        var implName = (String) properties.get(fieldTypeName);
        if (implName == null) {
            throw new NoSuchElementException();
        }
        return implName;
    }

    private Object getImpl(String implName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName(implName).newInstance();
    }
}
