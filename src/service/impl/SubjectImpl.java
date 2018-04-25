package service.impl;

import service.Subject;

/**
 * Created by pengbo on 18-4-25.
 */
public class SubjectImpl implements Subject{
    @Override
    public void init() {
        System.out.println("init ...");
    }

    @Override
    public void sayHello(String name) {
        System.out.println("hello,"+name);
    }
}
