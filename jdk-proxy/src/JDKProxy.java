import service.Subject;
import service.impl.SubjectImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy implements InvocationHandler{

    private Object target;

    public JDKProxy(Object target){
        this.target = target;
    }

    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        JDKProxy jdkProxy = new JDKProxy(new SubjectImpl());
        Subject subjectProxy = (Subject)jdkProxy.bind();
        subjectProxy.init();
        subjectProxy.sayHello("world");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        System.out.println("BeforeAdvice");
        Object retVal = null;
        try {
            System.out.println("AroundAdvice");
            retVal = method.invoke(target, objects);
            System.out.println("AroundAdvice");
            System.out.println("AfterReturningAdvice");
        }
        catch (Throwable e) {
            System.out.println("AfterThrowingAdvice");
        }
        finally {
            System.out.println("After Advice");
        }
        return retVal;
    }

    public Object bind(){
       return Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{Subject.class}, this);
    }
}
