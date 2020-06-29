package com.xbcai.java8demo.proxy.aop;

import com.xbcai.java8demo.annotation.SimpleInject;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 以cglib实现的aop例子
 */
public class AopDemo {
    public static void main(String[] args) throws Exception{
        ServiceA a = CGLibContainer.getInstance(ServiceA.class);
        a.callB();
    }
}
/**
 * 切面注解
 * @author xbcai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Aspect {
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
    public static void before(Object object, Method method, Object[] args){
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
    /**
     * 存每个类的每一个切点的方法列表
     */
    static Map<Class<?>, Map<InterceptPoint, List<Method>>> interceptMethodsMap = new HashMap<>();
    /**
     * 在类的初始化过程中分析每个带有@aspect注解的类，这些类一般可以通过扫描所有的类得到，为简化起见，这里将它写在代码里面
     */
    static Class<?>[] aspects = new Class<?>[]{ServiceLogAspect.class,ExceptionAspect.class};
    static {
        init();
    }
    private static void init(){
        for(Class<?> cls:aspects){
            Aspect aspect = cls.getAnnotation(Aspect.class);
            if(aspect!=null){
                Method before = getMethod(cls,"before",new Class<?>[]{Object.class,Method.class,Object[].class});
                Method after = getMethod(cls,"after",new Class<?>[]{Object.class,Method.class,Object[].class,Object.class});
                Method exception = getMethod(cls,"exception",new Class<?>[]{Object.class,Method.class,Object[].class,Throwable.class});
                Class<?>[] intercepttedArr = aspect.value();
                for(Class<?> interceptted:intercepttedArr){
                    addInterceptMethod(interceptted,InterceptPoint.BEFORE,before);
                    addInterceptMethod(interceptted,InterceptPoint.AFTER,after);
                    addInterceptMethod(interceptted,InterceptPoint.EXCEPTION,exception);
                }
            }
        }
    }

    /**
     * 获取指定类的指定方法
     * @param cls 指定类
     * @param name 指定方法
     * @param paramTypes 方法参数
     */
    private static Method getMethod(Class<?> cls, String name, Class<?>[] paramTypes) {
        try {
            return cls.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 这里是将切面的方法加入到目标类的切点方法列表中
     * @param cls 目标类
     * @param point 切点
     * @param method 切面方法
     */
    private static void addInterceptMethod(Class<?> cls,InterceptPoint point,Method method){
        if(method==null){
            return;
        }
        Map<InterceptPoint, List<Method>> map = interceptMethodsMap.get(cls);
        if(map==null){
            map = new HashMap<>();
            interceptMethodsMap.put(cls,map);
        }
        List<Method> methods = map.get(point);
        if(methods==null){
            methods = new ArrayList<>();
            map.put(point,methods);
        }
        methods.add(method);
    }

    /**
     * 根据类型创建对象,如果类型cls不需要增强，则直接低矮哦用cls.newInstance(),否则使用cglib创建动态代理
     * @param cls 类型对象
     * @param <T> 类型
     * @return 返回创建好的对象
     */
    private static <T> T createInstance(Class<T> cls) throws Exception{
        if(!interceptMethodsMap.containsKey(cls)){
            return cls.newInstance();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new AspectInterceptor());
        return cls.cast(enhancer.create());
    }
    @SuppressWarnings("all")
    public static <T> T getInstance(Class<T> cls) throws Exception{
        T obj = createInstance(cls);
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
}

/**
 * 切点（调用前、调用后、出现异常）
 */
 enum InterceptPoint{
    BEFORE,AFTER,EXCEPTION
}

class AspectInterceptor implements MethodInterceptor{
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        List<Method> beforeMethods = getInterceptMethods(object.getClass().getSuperclass(),InterceptPoint.BEFORE);
        for(Method m :beforeMethods){
            m.invoke(null,new Object[]{object,method,args});
        }
        try{
            //调用原始方法
            Object result = proxy.invokeSuper(object, args);
            //执行after方法
            List<Method> afterMethods = getInterceptMethods(object.getClass().getSuperclass(),InterceptPoint.AFTER);
            for(Method m:afterMethods){
                m.invoke(null,new Object[]{object,method,args,result});
            }
            return result;
        }catch (Exception e){
            //执行exception方法
            List<Method> exceptionMethods = getInterceptMethods(object.getClass().getSuperclass(),InterceptPoint.EXCEPTION);
            for (Method m:exceptionMethods){
                m.invoke(null,new Object[]{object,method,args,e});
            }
            throw e;
        }
    }

    /**
     * 获取目标类的拦截点的方法列表，也就是获取了切面类里面对应的方法
     * @param cls 目标类对象
     * @param point 拦截点
     */
    static List<Method> getInterceptMethods(Class<?> cls,InterceptPoint point){
        Map<InterceptPoint, List<Method>> map = CGLibContainer.interceptMethodsMap.get(cls);
        if(map==null){
            return Collections.emptyList();
        }
        List<Method> methods = map.get(point);
        if(methods==null){
            return Collections.emptyList();
        }
        return methods;
    }
}