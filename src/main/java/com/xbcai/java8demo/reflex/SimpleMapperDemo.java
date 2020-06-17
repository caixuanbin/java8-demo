package com.xbcai.java8demo.reflex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 利用反射实现一个简单的通用序列化/反序列化
 */
public class SimpleMapperDemo {
    /**
     * 将对象序列化成字符串
     * @param obj 对象
     * @return
     */
    public static String objectToString(Object obj){
        try {
            Class<?> cls = obj.getClass();
            StringBuilder sb = new StringBuilder();
            sb.append(cls.getName()+"\n");
            for (Field f:cls.getDeclaredFields()) {
                if(!f.isAccessible()){
                    f.setAccessible(true);
                }
                sb.append(f.getName()+"#"+f.get(obj).toString()+"\n");
            }
            return sb.toString();
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串序列化为对象
     * @param str 要序列化成对象的字符串
     * @return
     */
    public static Object stringToObject(String str){
        try {
            String[] lines= str.split("\n");
            if(lines.length<1){
                throw new IllegalAccessException(str);
            }
            Class<?> cls = Class.forName(lines[0]);
            Object obj = cls.newInstance();
            if(lines.length>1){
                for(int i=1;i<lines.length;i++){
                    String[] fv = lines[i].split("#");
                    if(fv.length!=2){
                        throw new IllegalAccessException(lines[i]);
                    }
                    Field field = cls.getDeclaredField(fv[0]);
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    setFieldValue(field,obj,fv[1]);

                }
            }
            return obj;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void setFieldValue(Field f,Object obj, String value) throws Exception{
        Class<?> type = f.getType();
        if(type==int.class){
            f.setInt(obj,Integer.parseInt(value));
        }else if(type==byte.class){
            f.setByte(obj,Byte.parseByte(value));
        }else if(type==short.class){
            f.setShort(obj,Short.parseShort(value));
        }else if(type==long.class){
            f.setLong(obj,Long.parseLong(value));
        }else if(type==double.class){
            f.setDouble(obj,Double.parseDouble(value));
        }else if(type==char.class){
            f.setChar(obj,value.charAt(0));
        }else if(type==boolean.class){
            f.setBoolean(obj,Boolean.parseBoolean(value));
        }else if(type==String.class){
            f.set(obj,value);
        }else{
            //假定该类型有一个以String类型为参数的构造方法
            Constructor<?> ctor = type.getConstructor(new Class[]{String.class});
            f.set(obj,ctor.newInstance(value));

        }
    }

    public static void main(String[] args) {
        Student student = new Student("张三",18,88.99);
        String s = SimpleMapperDemo.objectToString(student);
        System.out.println(s);
        Student stu = (Student)SimpleMapperDemo.stringToObject(s);
        System.out.println(stu);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Student{
    String name;
    int age;
    Double score;
}
