package Lesson0607Task03;

//Task
//    3. Написать код, который сериализирует и десериализирует в/из файла все значения полей класса,
//        которые отмечены аннотацией @Save.
//Requirements
//    сначяла постарайтесь решить сами, еслы не выйдет - решение по задаче выложено, скопируйте и разберытесь

//import org.omg.CORBA.Object;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;


public class Task03 {

    public static void main(String[] args) {
        String fileName = "src/main/resources/task03File.txt";
        Cat bob = new Cat("Bob");

        MyClass obj1 = new MyClass(11,true,'m', "Hello", bob);
        Serial.toFile(obj1, fileName);
        MyClass obj2 = (MyClass) Serial.fromFile(MyClass.class, fileName);

        System.out.println(obj2.getInt1());
        System.out.println(obj2.isBoolean1());
        System.out.println(obj2.getCharacter1());
        System.out.println(obj2.getString1());
        System.out.println(obj2.getCat1());

    }

}

@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
@interface Save {}

class MyClass implements Serializable {

    @Save
    private int int1;
//    @Save
    private boolean boolean1;
//    @Save
    private Character character1;
    @Save
    private String string1;
    @Save
    private Cat cat1;

    public MyClass(){}

    public MyClass(int int1, boolean boolean1, Character character1, String string1, Cat cat1) {
        this.int1 = int1;
        this.boolean1 = boolean1;
        this.character1 = character1;
        this.string1 = string1;
        this.cat1 = cat1;
    }

    public int getInt1() {
        return int1;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public Character getCharacter1() {
        return character1;
    }

    public String getString1() {
        return string1;
    }

    public Cat getCat1() {
        return cat1;
    }
}

class Cat implements Serializable{
    private String name;

    public Cat() {
        this("Unknown");
    }

    public Cat(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}

class Serial{
    private static HashMap<String, Object> map;
    private static Field[] fields;

    public static void toFile(Serializable obj, String fileName){
        map = new HashMap<>();
        Class<?> cl = obj.getClass();
        fields = cl.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Save.class)){
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(map);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object fromFile(Class<?> cl, String fileName){
        map = new HashMap<>();
//        java.lang.Object obj = null;
        Object obj = null;

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            map = (HashMap<String, Object>) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            obj = cl.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fields = cl.getDeclaredFields();
        for (Field field : fields) {
            if ( field.isAnnotationPresent(Save.class) && map.containsKey(field.getName()) ){
                field.setAccessible(true);
                try {
                    field.set(obj, map.get(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }
        return obj;
    }

}



