package Lesson0607Task01;

//Task
//    Home work Hillel
//    1. Создать аннотацию, которая принимает параметры для метода.
//      Написать код, который вызовет метод, помеченный этой аннотацией, и передаст параметры аннотации в вызываемый метод.
//Requirements
//    @Test(a=2, b=5)
//    public void test(int a, int b) {...}
//???????Чтобы включить программу чтения с экрана, нажмите Ctrl+Alt+Z. Для просмотра списка быстрых клавиш нажмите Ctrl+косая черта.???????
//???????Неопознанный барсук закрыл(а) документ???????

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Inherited
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@interface Test {
    int a();// default 0;
    int b();// default 0;
}

public class Task01 {

    @Test(a=2, b=5)
    public void test(int a, int b){
        System.out.println(a + " " + b);
    }

    public static void main(String[] args) /*throws NoSuchMethodException*/ {
        Task01 task01 = new Task01();
        final Class<?> task01Class = Task01.class;

        try {
            Method testMethod = task01Class.getMethod("test", int.class, int.class);
            task01.test(
                    testMethod.getAnnotation( Test.class ).a(),
                    testMethod.getAnnotation( Test.class ).b()
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
