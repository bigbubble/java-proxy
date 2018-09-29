/**
 * Created by pengbo on 18-4-27.
 */
public class RealObject {

    public void sayHello(String name){
        System.out.println("hello, " + name);
    }

    public void init(){
        System.out.println("init");
    }

    public final void invalid(){
        System.out.println("invalid");
    }
}
