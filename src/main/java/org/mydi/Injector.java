package org.mydi;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;

public class Injector {
    private final Properties properties;

    public Injector(Properties properties) {
        this.properties = properties;
    }

    public <TClass> void inject(TClass target)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        var fields = getAutoInjectableFields(target);
        for (var field : fields) {
            field.setAccessible(true);
            var implName = getImplName(field);
            field.set(target, getImpl(implName));
            field.setAccessible(false);
        }
    }

    private static <TClass> Field[] getAutoInjectableFields(TClass target) {
        var fields = target.getClass().getDeclaredFields();
        return Arrays.stream(fields).filter(field ->
            field.isAnnotationPresent(AutoInjectable.class)).toArray(Field[]::new);
    }

    private String getImplName(Field field) {
        var fieldName = field.getType().getName();
        return (String) properties.get(fieldName);
    }

    private Object getImpl(String implName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName(implName).newInstance();
    }
}
