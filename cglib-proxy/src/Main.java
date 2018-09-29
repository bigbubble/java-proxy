import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealObject.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println("before advice");
                Object returnVal;
                try {
                    System.out.println("around advice");
                    returnVal = method.invoke(proxy, args);
                    System.out.println("around advice");
                    return returnVal;
                }catch (Throwable throwable){
                    throw throwable;
                }finally {
                    System.out.println("after advice");
                }
            }
        });

        RealObject realObject = (RealObject) enhancer.create();
        
         try {
            //io block
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        realObject.sayHello("me");
        realObject.init();
    }
}
