package com.xbcai.java8demo.reflex.test;

import com.xbcai.java8demo.reflex.anno.NameAnno;
import com.xbcai.java8demo.reflex.enumeration.GlobalEnum;
import com.xbcai.java8demo.reflex.mode.Student;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 反射的例子
 */
public class ReflexTest {
    /**
     * class的类型信息及声明信息
     */
    private static void testClass(){
        String[] stringArray = new String[]{"a","b"};
        Class<? extends String[]> aClass = stringArray.getClass();
        //判断是否是数组
        System.out.println(aClass.isArray());
        //判断是否基本类型
        System.out.println(aClass.isPrimitive());
        //是否接口
        System.out.println(aClass.isInterface());
        //是否是枚举
        System.out.println(aClass.isEnum());
        //是否是注解
        System.out.println(aClass.isAnnotation());
        //是否是匿名内部类
        System.out.println(aClass.isAnonymousClass());
        //是否是成员类，成员类定义再方法外，不是匿名类
        System.out.println(aClass.isMemberClass());
        //获取修饰符，返回值可通过modifier类进行解读
        System.out.println(aClass.getModifiers());
        //获取父类，如果为Object，父类为null
        System.out.println(aClass.getSuperclass());
        //对于类，为自己声明实现的所有接口，对于接口，为直接扩展的接口，不包括通过父类继承的
        System.out.println(aClass.getInterfaces());
        //自己声明的注解
        System.out.println(aClass.getDeclaredAnnotations());
        //所有的注解，包括继承得到的
        System.out.println(aClass.getAnnotations());
        //获取指定类型的注解，包括继承得到的
        System.out.println(aClass.getDeclaredAnnotation(NameAnno.class));
        //检查指定类型的注解，包括继承得到的
        System.out.println(aClass.isAnnotationPresent(NameAnno.class));
        //获取类全称名称带包的
        System.out.println(String.class.getName());
        //返回名称不带包信息
        System.out.println(String.class.getSimpleName());
        //返回更为友好的名称不带包的
        System.out.println(String.class.getCanonicalName());
        //返回该类所在的包
        System.out.println(Student.class.getPackage());
    }

    /**
     * 类的加载
     */
    private static void testLoadClass() throws Exception{
        //基本类型不支持forName 方法
        Class<?> aClass = Class.forName(Student.class.getName());
        System.out.println(aClass);
        String name ="[Ljava.lang.String;";
        Class cls = Class.forName(name);
        System.out.println(cls==String[].class);
    }

    /**
     * 反射与数组
     */
    private static void testReflexAndArray(){
        String[] arr = new String[]{"a","b","c"};
        //获取数组类型
        System.out.println(arr.getClass().getComponentType());
        //创建指定元素类型、指定长度的数组
        System.out.println(Array.newInstance(int.class, 10));
        //创建多维数组
        System.out.println(Array.newInstance(String.class,10,10));
        //获取数组指定的索引位置index处的值
        System.out.println(Array.get(arr,2));
        //修改数组array指定索引位置的值为value
        Array.set(arr,2,"xbcai");
        System.out.println(arr[2]);
        //返回数组的长度
        System.out.println(Array.getLength(arr));

    }

    /**
     * 基本类型没有getClass方法，但也都有对象的Class对象，类型参数为对象的包装类型
     */
    private static void testBaseType(){
        Class<Integer> integerClass = int.class;
        Class<Byte> byteClass = byte.class;
        Class<Character> characterClass = char.class;
        Class<Double> doubleClass = double.class;
        Class<GlobalEnum> globalEnumClass = GlobalEnum.class;
        System.out.println(integerClass);
        System.out.println(byteClass);
        System.out.println(characterClass);
        System.out.println(doubleClass);
        System.out.println(globalEnumClass);
    }

    /**
     * 类的字段信息
     */
    private static void testClassField() throws Exception{
        //返回所有的public字段，包括其父类的，如果没有字段，返回空数组
        Arrays.asList(Student.class.getFields()).forEach(System.out::println);
        //返回本类声明的所有字段，包括非public的，但不包括父类的
        Arrays.asList(Student.class.getDeclaredFields()).forEach(System.out::println);
        //返回本类或父类中指定名称的public字段，找不到抛出异常
        System.out.println(Student.class.getField("name"));
        //返回本类或父类中指定名称的包括非public字段，找不到抛出异常
        System.out.println(Student.class.getDeclaredField("name"));

    }

    /**
     * 字段的操作
     */
    private static void testField() throws Exception{
        Student s = new Student("zhangsan",2,33.0);
        Field[] fields = s.getClass().getDeclaredFields();
        for (Field item : Arrays.asList(fields)) {
            //获取该字段的名称
            System.out.print(item.getName() + "|");
            //获取当前程序是否有该字段的访问权限
            System.out.print(item.isAccessible() + "|");
            //設置为true标识忽略Java的访问检查机制，以允许读写非public的字段
            item.setAccessible(true);
            //获取指定对象中该字段的值
            System.out.print(item.get(s)+"|");
            if(item.getName().equals("name")){
                //将指定对象s中该字段的值設置为xbcai
                item.set(s,"xbcai");
                System.out.print(item.get(s));
            }
            if(item.getName().equals("score")){
                //以基本类型操作字段值，其他类型类似
                //item.setDouble(s,99.99);
            }
            //返回字段的修饰符
            System.out.print(item.getModifiers()+"|");
            //返回字段的类型
            System.out.print(item.getType()+"|");
            //查询字段的注解信息
            System.out.println(item.getAnnotation(NameAnno.class)+"|");
            //获取注解信息，这里注解永远都只会返回最多一个，因为是操作了字段属性
            Annotation[] declaredAnnotations = item.getDeclaredAnnotations();
            for (int i=0;i<declaredAnnotations.length;i++){
                System.out.println(declaredAnnotations[i]+",length:"+declaredAnnotations.length);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        testField();
    }
}
