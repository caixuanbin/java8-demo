package com.xbcai.java8demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * 修饰类中字段，表达依赖关系
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SimpleInject {
}

/**
 * 通过自定义注解诠释注入的例子
 */
 class ServiceA {
    @SimpleInject
    ServiceB b;
    public void action(){
        b.action();
    }
}

/**
 * 被依赖的B
 */
class ServiceB{
    public void action(){
        System.out.println("I am B");
    }
}

/**
 * 该类每次都创建一个对象，并且判断该对象的字段是否有对于的SimpleInject注解，
 * 有的话就获取该字段的类型，创建字段类型对应的对象，然后赋值给该字段，也就是依赖注入
 */
class SimpleContainer{
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

    public static void main(String[] args) throws Exception {
        ServiceA a = SimpleContainer.getInstance(ServiceA.class);
        a.action();


    }
}
