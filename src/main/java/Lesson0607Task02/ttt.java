package Lesson0607Task02;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class AnnoRunner {
    public static void main(String[] args) {
        AccountOperationManager account = new AccountOperationManagerImpl();
// "регистрация класса" для включения аннотаций в обработку.
        AccountOperationManager securityAccount =
                SecurityFactory.createSecurityObject(account);
        securityAccount.depositInCash(10128336, 6);
        securityAccount.withdraw(64092376, 2);
        securityAccount.convert(200);
        securityAccount.transfer(64092376, 300);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface BankingAnnotation {
    SecurityLevelEnum securityLevel() default SecurityLevelEnum.NORMAL;
}

enum SecurityLevelEnum {
    LOW, NORMAL, HIGH
}

class AccountOperationManagerImpl implements AccountOperationManager {
    @BankingAnnotation(securityLevel = SecurityLevelEnum.HIGH)
    public double depositInCash(int accountNumber, int amount) {
// зачисление на депозит
        return 0; // stub
    }
    @BankingAnnotation(securityLevel = SecurityLevelEnum.HIGH)
    public boolean withdraw(int accountNumber, int amount) {
// снятие суммы, если не превышает остаток
        return true; // stub
    }
    @BankingAnnotation(securityLevel = SecurityLevelEnum.LOW)
    public boolean convert(double amount) {
// конвертировать сумму
        return true; // stub
    }
    @BankingAnnotation
    public boolean transfer(int accountNumber, double amount) {
// перевести сумму на счет
        return true; // stub
    }
}

class SecurityLogic {
    public void onInvoke(SecurityLevelEnum level, Method method, Object[] args) {
        StringBuilder argsString = new StringBuilder();
        for (Object arg : args) {
            argsString.append(arg.toString() + " :");
        }
        argsString.setLength(argsString.length() - 1);
        System.out.println(String.format(
                "Method %S was invoked with parameters : %s", method.getName(),
                argsString.toString()));
        switch (level) {
            case LOW:
                System.out.println("Низкий уровень проверки безопасности: " + level);
                break;
            case NORMAL:
                System.out.println("Обычный уровень проверки безопасности: " + level);
                break;
            case HIGH:
                System.out.println("Высокий уровень проверки безопасности: " + level);
                break;
        }
    }
}

class SecurityFactory {
    public static AccountOperationManager createSecurityObject(AccountOperationManager targetObject) {
        return (AccountOperationManager) Proxy.newProxyInstance(
                targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(),
                new SecurityInvokationHandler(targetObject));
    }
    private static class SecurityInvokationHandler implements InvocationHandler {
        private Object targetObject = null;
        public SecurityInvokationHandler(Object targetObject) {
            this.targetObject = targetObject;
        }
        public Object invoke(Object proxy, Method method, Object[ ] args)
                throws Throwable {
            SecurityLogic logic = new SecurityLogic();
            Method realMethod = targetObject.getClass().getMethod(
                    method.getName(),
                    (Class[]) method.getGenericParameterTypes());
            BankingAnnotation annotation = realMethod
                    .getAnnotation(BankingAnnotation.class);
            if (annotation != null) {
// доступны и аннотация и параметры метода
                logic.onInvoke(annotation.securityLevel(), realMethod, args);
                try {
                    return method.invoke(targetObject, args);
                } catch (InvocationTargetException e) {
                    System.out.println(annotation.securityLevel());
                    throw e.getCause();
                }
            } else {
/* в случае если аннотирование метода обязательно следует
генерировать исключение на факт ее отсутствия */
/* throw new InvocationTargetException(null, "method "
+ realMethod + " should be annotated "); */
// в случае если допустимо отсутствие аннотации у метода
                return null;
            }
        }
    }
}

interface AccountOperationManager {
    double depositInCash(int accountNumber, int amount);
    boolean withdraw(int accountNumber, int amount);
    boolean convert(double amount);
    boolean transfer(int accountNumber, double amount);
}


public class ttt {



}
