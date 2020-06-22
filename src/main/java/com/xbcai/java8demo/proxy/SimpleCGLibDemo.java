package com.xbcai.java8demo.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib动态代理
 * cglib的实现机制与Java sdk不同，它是通过继承实现的，它也是动态创建一个类，但是这个类的父类是被代理的类
 * 代理类重写了父类的所有public非final方法，改为调用Callback中的相关方法
 */
public class SimpleCGLibDemo {
    /**
     * 被代理的类
     */
    static class RealService{
        public void sayHello(){
            System.out.println("hello");
        }
    }

    /**
     * 代理类
     */
    static class SimpleInterceptor implements MethodInterceptor{
        /**
         *
         * @param object 代理对象
         * @param method 被代理类的方法
         * @param args 参数
         * @param methodProxy 由它调用invokeSuper方法调用被代理类的方法
         */
        @Override
        public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("entering "+method.getName());
            Object result = methodProxy.invokeSuper(object, args);
            System.out.println("leaving "+method.getName());
            return result;
        }
    }

    /**
     * 动态创建代理类
     * @param cls 被代理的类对象
     * @param <T> 被代理的类的类型
     */
    private static <T> T getProxy(Class<T> cls){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new SimpleInterceptor());
        return cls.cast(enhancer.create());
    }

    public static void main(String[] args) {
        RealService proxy = getProxy(RealService.class);
        proxy.sayHello();
    }
}
