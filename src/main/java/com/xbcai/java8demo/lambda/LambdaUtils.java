package com.xbcai.java8demo.lambda;

import com.xbcai.java8demo.lambda.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * lambda表达式通用工具类
 */
public class LambdaUtils {
    /**
     * 通用容器List集合过滤方法
     * @param lists 集合元素
     * @param pred 过滤条件--谓词过滤器
     * @param <E> 容器元素类型
     * @return 返回过滤元素集合
     */
    public static <E> List<E> filter(List<E> lists, Predicate<E> pred){
        List<E> retList = new ArrayList<>();
        for(E e:lists){
            if(pred.test(e)){
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 转换容器元素内容，然后按照想要的转换方式输出（创建了对象）
     * @param list 容器元素
     * @param mapper 转换的规则
     * @param <T> 原容器元素类型
     * @param <R> 转换后的元素类型
     * @return 返回转换后的集合元素
     */
    public static <T,R> List<R> map(List<T> list, Function<T,R> mapper){
        List<R> retList = new ArrayList<>(list.size());
        for(T e:list){
            retList.add(mapper.apply(e));
        }
        return retList;
    }

    /**
     * 修改原对象的值，也可以用作消费
     * @param lists 容器列表
     * @param consumer 消费规则
     * @param <E> 容器元素
     */
    public static <E> void foreach(List<E> lists, Consumer<E> consumer){
        for (E e:lists){
            consumer.accept(e);
        }
    }

    public static void main(String[] args) {
        //测试集合过滤器方法
        List<Student> studentList = Arrays.asList(new Student[]{new Student("xbcai", 30),new Student("xbcai2",40),new Student("xbcai3",90)});
        List<Student> filter = filter(studentList, t -> t.getScore() > 50);
        System.out.println(filter);
        //测试容器转换方法,将学生列表转换为只有学生名字的列表返回
        List<String> map = map(studentList, Student::getName);
        //将学生列表名字转换为大写输出
        List<Student> map1 = map(studentList, t -> new Student(t.getName().toUpperCase(), t.getScore()));
        System.out.println(map);
        System.out.println(map1);
        //直接更改原集合中的元素，让学生姓名变大写
        foreach(studentList,t->t.setName(t.getName().toUpperCase()));
        System.out.println(studentList);
    }
}
