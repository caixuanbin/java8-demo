package com.xbcai.java8demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimplesSingleton {
}


/**
 * 通过自定义注解诠释单例的注解
 */
 class ServiceAA {
    @SimpleInject
    ServiceBB b;
    public void action(){
        b.action();
    }
}

/**
 * 被依赖的BB 用注解SimplesSingleton标识为单例类
 */
@SimplesSingleton
class ServiceBB{
    public void action(){
        System.out.println("I am B");
    }
}

/**
 * ServiceBB类被SimplesSingleton注解，标识该类对象只会有一个，即单例，全局共享
 * 类如果有 @SimpleInject注解，代表通过依赖注入来创建被注解的对象
 */
class SimpleSingletonContainer{
    /**
     * 缓存创建过的对象
     */
    private static Map<Class<?>,Object> instances = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> cls) throws Exception{
        //通过注解判断该类是否要创建为单例的类
        boolean singleton = cls.isAnnotationPresent(SimplesSingleton.class);
        if(!singleton){
            return createInstance(cls);
        }
        Object obj = instances.get(cls);
        if(obj!=null){
            return cls.cast(obj);
        }
        synchronized (cls){
            obj = instances.get(cls);
            if(obj==null){
                obj = createInstance(cls);
                instances.put(cls,obj);
            }
        }
        // 将obj类型对象转换为cls类型的对象
        return cls.cast(obj);
    }

    private static <T> T createInstance(Class<T> cls) throws Exception{
        T obj = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f:fields){
            if(f.isAnnotationPresent(SimpleInject.class)){
                if(!f.isAccessible()){
                    f.setAccessible(true);
                }
                Class<?> fieldCls = f.getType();
                f.set(obj,createInstance(fieldCls));
            }
        }
        return obj;
    }

    public static void main(String[] args) throws Exception {
        ServiceAA a = SimpleSingletonContainer.getInstance(ServiceAA.class);
        a.action();


    }
}
