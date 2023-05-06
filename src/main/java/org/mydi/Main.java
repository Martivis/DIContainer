package org.mydi;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        try {
            var properties = PropertiesLoader.load("src/main/resources/config.properties");
            var injector = new Injector(properties);

            var bean = new SomeBean();
            injector.inject(bean);
            bean.foo();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}