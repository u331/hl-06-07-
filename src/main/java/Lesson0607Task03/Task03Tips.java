package Lesson0607Task03;

//public class Main {
//    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
//
//        TestConteiner testConteiner = new TestConteiner();
//        Class<?> classClass = testConteiner.getClass();
//
//        if (!classClass.isAnnotationPresent(SaveTo.class)) {
//            System.out.println("Class is not annotated");
//        } else {
//            Method[] methods = classClass.getMethods();
//            for (Method method : methods) {
//                if (method.isAnnotationPresent(Saver.class)) {
//                    SaveTo saveTo = classClass.getAnnotation(SaveTo.class);
//                    method.invoke(testConteiner, testConteiner.text, saveTo.PATH());
//                } else {
//                    System.out.println("method is not annotated");
//                }
//            }
//        }
//    }
//}
//
//---------------------------
//
//@Inherited
//@Retention(value = RetentionPolicy.RUNTIME)
//public @interface SaveTo {
//    String PATH(); // = "/home/roman/ROMA/JAVA/ProgKievUa/HW1 Refl 13.10/Task2/file.txt";
//}
//-----------------
//@Target(value = ElementType.METHOD)
//@Retention(value = RetentionPolicy.RUNTIME)
//public @interface Saver {
//}
//----------------
//@SaveTo(PATH = "/home/roman/ROMA/JAVA................................./file.txt")
//public class TestConteiner {
//    String text = "text from textContainer";
//
//    @Saver
//    public void save(String text1, String path) throws IOException {
//        FileWriter w = new FileWriter(path);
//        try {
//            w.write(text1);
//        } finally {
//            w.close();
//        }
//    }
//}

public class Task03Tips {
}
