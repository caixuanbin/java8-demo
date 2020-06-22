package com.xbcai.java8demo.proxy.aop;

import com.xbcai.java8demo.annotation.SimpleInject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 切面注解
 * @author xbcai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {
    Class<?>[] value();
}

class ServiceA{
    @SimpleInject
    ServiceB b;
    public void callB(){
        b.action();
    }

}
class ServiceB{
    public void action(){
        System.out.println("I am B");
    }
}

/**
 * 日志切面类
 * 这里对ServiceA、ServiceB进行日志增强
 */
@Aspect({ServiceA.class,ServiceB.class})
class ServiceLogAspect{
    public static void before(Object object, Method method,Object[] args){
        System.out.println("entering "+method.getDeclaringClass().getSimpleName()+"::"+method.getName()+",args:"+ Arrays.toString(args));
    }

    /**
     *
     * @param object 代理对象
     * @param method 被代理类的方法
     * @param args 参数
     * @param result 方法执行的结果
     */
    public static void after(Object object,Method method,Object[] args,Object result){
        System.out.println("leaving "+method.getDeclaringClass().getSimpleName()+"::"+method.getName()+",args:"+ Arrays.toString(args));

    }
    public static void exception(Object object,Method method,Object[] args,Throwable e){

    }

}

/**
 * 异常切面类
 * 这里对ServiceB类异常增强
 */
@Aspect({ServiceB.class})
class ExceptionAspect{


    /**
     *
     * @param object 代理对象
     * @param method 被代理类的方法
     * @param args 参数
     * @param e 异常信息
     */
    public static void exception(Object object,Method method,Object[] args,Throwable e){
        System.out.println("exception when calling:"+method.getName()+","+Arrays.toString(args));
    }


}

/**
 * 注入容器
 */
class CGLibContainer{
    @SuppressWarnings("all")
    public static <T> T getInstance(Class<T> cls) throws Exception{
        T obj = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f: fields){
            if(f.isAnnotationPresent(SimpleInject.class)){
                if(!f.isAccessible()){
                    f.setAccessible(true);
                }
                Class<?> fieldCls = f.getType();
                //依赖注入对象
                f.set(obj,getInstance(fieldCls));
            }
        }
        return obj;
    }

    public static void main(String[] args) throws Exception{
        ServiceA a = (ServiceA)CGLibContainer.getInstance(ServiceA.class);
        a.callB();
    }
}
