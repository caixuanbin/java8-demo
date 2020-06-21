package com.xbcai.java8demo.annotation;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 注解的例子应用(通过对一个teacher类的字段注解，来输出按注解格式化的对象)
 */
public class AnnotationsDemo {
    /**
     * 按照注解格式化对象
     * @param obj 要格式的对象
     * @return 格式化后输出的对象
     */
    private static String format(Object obj) throws Exception{
        Class<?> cls = obj.getClass();
        StringBuilder sb = new StringBuilder();
        for(Field f : cls.getDeclaredFields()){
            if(!f.isAccessible()){
                f.setAccessible(true);
            }
            Label label = f.getAnnotation(Label.class);
            String name = label!=null?label.value():f.getName();
            Object value = f.get(obj);
            if(value!=null && f.getType()== Date.class){
                value = formatDate(f,value);
            }
            sb.append(name).append(":").append(value).append("\n");
        }
        return sb.toString();
    }

    /**
     * 格式化日期字段
     * @param f 要格式化的字段
     * @param value 格式化的值
     * @return 返回格式化后的日期
     */
    private static Object formatDate(Field f,Object value){
        Format format = f.getAnnotation(Format.class);
        if(format!=null){
            SimpleDateFormat sdf = new SimpleDateFormat(format.pattern());
            sdf.setTimeZone(TimeZone.getTimeZone(format.pattern()));
            return sdf.format(value);
        }
        return value;
    }
    private static void testSimpleAnnotation() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Teacher teacher = new Teacher("张三",sdf.parse("1990-12-12"),80.9);
        System.out.println(format(teacher));
    }

    public static void main(String[] args) throws Exception{
        testSimpleAnnotation();
    }
}
