package com.xbcai.java8demo.proxy;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk通用动态代理类例子
 */
public class GeneralJDKProxyDemo {
    /**
     * 被代理类接口
     */
    static interface IServiceA{
        public void sayHello();
    }

    /**
     * 被代理类的实现类
     */
    static class ServiceAImpl implements IServiceA{
        @Override
        public void sayHello() {
            System.out.println("ServiceAImpl-->sayHello");
        }
    }

    /**
     * 另外一个被代理类的接口
     */
    static interface IServiceB{
        public void fly();
    }

    /**
     * 另外一个被代理类的接口实现类
     */
    static class ServiceBImpl implements IServiceB{
        @Override
        public void fly() {
            System.out.println("ServiceBImpl-->fly");
        }
    }

    /**
     * 代理类
     */
    @AllArgsConstructor
    static class SimpleInvocationHandler implements InvocationHandler{
        private Object realObj;
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("entering "+realObj.getClass().getSimpleName()+"::"+method.getName());
            Object result = method.invoke(realObj, args);
            System.out.println("leaving "+realObj.getClass().getSimpleName()+"::"+method.getName());
            return result;
        }
    }

    /**
     * 动态创建代理类对象
     * @param intf 被代理的类对象
     * @param realObj 被代理的对象
     * @param <T> 被代理的对象
     */
    private static <T> T getProxy(Class<T> intf,T realObj) {
        Object o = Proxy.newProxyInstance(intf.getClassLoader(), new Class<?>[]{intf}, new SimpleInvocationHandler(realObj));
        //动态强转换为被代理的对象类型
        return intf.cast(o);
    }

    public static void main(String[] args) {
        IServiceA a = new ServiceAImpl();
        IServiceB b = new ServiceBImpl();
        //动态创建IServiceA类的代理类
        IServiceA proxya = getProxy(IServiceA.class, a);
        //动态创建IServiceB类的代理类
        IServiceB proxyb = getProxy(IServiceB.class, b);
        proxya.sayHello();
        proxyb.fly();

    }
}
