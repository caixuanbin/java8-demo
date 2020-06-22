package com.xbcai.java8demo.proxy;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理例子
 */
public class SimpleJDKDynamicProxyDemo {
    /**
     * 公共接口
     */
    static interface IService{
        public void sayHello();
    }

    /**
     * 真正被代理的类
     */
    static class RealService implements IService{
        @Override
        public void sayHello(){
            System.out.println("Hello");
        }
    }

    /**
     * 业务处理类，用来代理目标对象执行业务逻辑，对代理接口所有方法的调用都会转给该类的invoke方法
     */
    @AllArgsConstructor
    static class SimpleInvocationHandler implements InvocationHandler{
        /**
         * 被代理的对象
         */
        private Object realObj;

        /**
         *
         * @param proxy 表示代理对象本身，它不是被代理的对象，这个参数一般用处不大
         * @param method 表示正在被调用的方法
         * @param args 表示方法的参数
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("entering "+method.getName());
            Object result = method.invoke(realObj, args);
            System.out.println("leaving "+method.getName());
            return result;
        }
    }

    public static void main(String[] args) {
        IService realService = new RealService();
        //动态创建代理对象，可以强制转换为interfaces数组中的某个接口类型
        IService cast = (IService)Proxy.newProxyInstance(IService.class.getClassLoader(), new Class<?>[]{IService.class}, new SimpleInvocationHandler(realService));
        cast.sayHello();
    }
}
