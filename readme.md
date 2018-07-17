# JDK代理

##### 实现方法 ：java.lang.reflect.Proxy#newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) throws IllegalArgumentException

###### 参数：
ClassLoader loader ： 定义代理类的类加载器 （the class loader to define the proxy class）  
Class<?>[] interfaces ： 代理类实现的接口列表 （the list of interfaces for the proxy class to implement）  
InvocationHandler h：每个代理实例都有一个关联的调用处理程序。当在代理实例上调用方法时，该方法调用被编码并调度到其invoke方法 （the invocation handler to dispatch method invocations to；[InvocationHandler 接口注释：Each proxy instance has an associated invocation handler. When a method is invoked on a proxy instance, the method invocation is encoded and dispatched to the {@code invoke} method of its invocation handler.]）

###### 实现原理

1.调用 java.lang.reflect.Proxy#newProxyInstance  
2.newProxyInstance方法内使用,生成类实例：Class<?> cl = getProxyClass0(loader, intfs);   
3.getProxyClass0调用proxyClassCache.get(loader, interfaces);(proxyClassCache 定义：private static final WeakCache<ClassLoader, Class<?>[], Class<?>>  proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory()); ProxyClassFactory是Proxy的一个静态内部类，实现了WeakCache的内部接口BiFunction的apply方法)  
4.proxyClassCache.get调用supplier.get();JDK对代理进行了缓存，如果已经存在相应的代理类，则直接返回，否则才会通过ProxyClassFactory来创建代理，调用ProxyClassFactory.apply()方法；  
5.ProxyClassFactory.apply()  
5.1 验证  
5.2 生成包名，对于非公共接口，代理类的包名与接口的相同, 对于公共接口的包名，默认为com.sun.proxy  
5.3 获取计数-默认情况下，代理类的完全限定名为：com.sun.proxy.$Proxy0，com.sun.proxy.$Proxy1……依次递增  
6.生成代理类字节码 byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, interfaces);  
<pre>
public static byte[] generateProxyClass(final String var0, Class[] var1) {
        ProxyGenerator var2 = new ProxyGenerator(var0, var1);
        final byte[] var3 = var2.generateClassFile();
        // 这里根据参数配置，决定是否把生成的字节码（.class文件）保存到本地磁盘，我们可以通过把相应的class文件保存到本地，再反编译来看看具体的实现，这样更直观
        //属性saveGeneratedFiles 可有配置系统属性“sun.misc.ProxyGenerator.saveGeneratedFiles”设置
        if(saveGeneratedFiles) {
            AccessController.doPrivileged(new PrivilegedAction() {
                public Void run() {
                    try {
                        int var1 = var0.lastIndexOf(46);
                        Path var2;
                        if(var1 > 0) {
                            Path var3x = Paths.get(var0.substring(0, var1).replace('.', File.separatorChar), new String[0]);
                            Files.createDirectories(var3x, new FileAttribute[0]);
                            var2 = var3x.resolve(var0.substring(var1 + 1, var0.length()) + ".class");
                        } else {
                            var2 = Paths.get(var0 + ".class", new String[0]);
                        }

                        Files.write(var2, var3, new OpenOption[0]);
                        return null;
                    } catch (IOException var4) {
                        throw new InternalError("I/O exception saving generated file: " + var4);
                    }
                }
            });
        }
</pre>
7. native方法，生成Class实例 return defineClass0(loader, proxyName,proxyClassFile, 0, proxyClassFile.length); //native方法 生成Class实例


#### 生成的代理类

<pre>
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sun.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import service.Subject;

public final class $Proxy0 extends Proxy implements Subject {
    private static Method m1;
    private static Method m3;
    private static Method m4;
    private static Method m0;
    private static Method m2;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return ((Boolean)super.h.invoke(this, m1, new Object[]{var1})).booleanValue();
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void init() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final void sayHello(String var1) throws  {
        try {
            super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final int hashCode() throws  {
        try {
            return ((Integer)super.h.invoke(this, m0, (Object[])null)).intValue();
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[]{Class.forName("java.lang.Object")});
            m3 = Class.forName("service.Subject").getMethod("init", new Class[0]);
            m4 = Class.forName("service.Subject").getMethod("sayHello", new Class[]{Class.forName("java.lang.String")});
            m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
            m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
</pre>
