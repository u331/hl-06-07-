package Lesson0607Task02;

//Task
//2. Написать класс TextContainer, который содержит в себе строку. С помощью механизма аннотаций указать
//      1) в какой файл должен сохраниться текст
//      2) метод, который выполнит сохранение.
//    Написать класс Saver, который сохранит поле класса TextContainer в указанный файл.
//Requirements
//  @SaveTo(path=“c:\\file.txt”)
//  class Container {
//      String text = “...”;
//      @Saver
//      public void save(..) {...}
//  }

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Task02 {
    public static void main(String[] args) {
        Saver saver = new Saver("TextContainer");
    }
}


@Inherited
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@interface SaveTo {
    String fileName();
}

// There is no possibility to have Class and Annotation with same name "Saver".
// So, had to call The Annotation "SaverMethod"
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@interface SaverMethod {}

@SaveTo(fileName = "src/main/resources/task02FileA.txt")
class TextContainer{
    private String text = "...";
    private String fileName = "src/main/resources/task02FileB.txt";

    @SaverMethod
    public void save() {
        try {
            FileWriter out = new FileWriter(fileName);
            out.write(text);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @SaverMethod
    public void doSmth() {
        System.out.println("doSmth");
    }
}

class Saver{
    Package pack = this.getClass().getPackage();
    String textContainerClassName = "TextContainer1";
    Class<?> cl = null;
    Method saverMethod = null;
    Object obj = null;
    String fileName = null;
    int saverCount = 0;

    Saver(String textContainerClassName){
        this.textContainerClassName = textContainerClassName;
        annoHandler();
     }

    Saver() {
        this("TextContainer");
    }

    public void annoHandler(){
        Field field = null;

        try {
            cl = Class.forName(pack.getName() + "." + textContainerClassName);
            fileName = cl.getAnnotation(SaveTo.class).fileName();//1
            obj = cl.newInstance();

            field = cl.getDeclaredField("fileName");
            field.setAccessible(true);
            field.set(obj, fileName);
            field.setAccessible(false);

            for (Method method : cl.getDeclaredMethods()) {
                if(method.isAnnotationPresent(SaverMethod.class)){
                    saverCount++;
                    if (saverCount > 1) throw new SaverQuantityError(cl, "More than one Saver methods");
                    saverMethod = method; //2
                }
            }
            if (saverCount < 1) throw new SaverQuantityError(cl, "There are no Saver methods");
            saverMethod.invoke(obj);
        } catch (SaverQuantityError e) {
            e.errorDescription();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}

class SaverQuantityError extends Exception{
    Class<?> clazz;
    String message;

    public SaverQuantityError(Class<?> clazz, String message) {
        this.clazz = clazz;
        this.message = message;
    }

    public SaverQuantityError(Class<?> clazz) {
        this.clazz = clazz;
        this.message = "There are no Saver methods";
    }

    public void errorDescription(){
            System.err.println(message + " in the " + clazz.getName() + " class");
    }
}
