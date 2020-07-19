package com.xbcai.java8demo.lambda.test;

import com.xbcai.java8demo.lambda.model.Student;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * lamdba测试
 */
public class LambdaTest {

    /**
     * 测试静态方法
     */
    public static void testStaticMethod(){
        System.out.println("==============testStaticMethod=========================");
        //下面两条语句是等价的
        Supplier<String> name1 = Student::getCollegeName;
        Supplier<String> name2 = ()->Student.getCollegeName();
        System.out.println(name1.get());
        System.out.println(name2.get());
    }

    /**
     * 测试实例方法
     */
    public static void testFunction(){
        System.out.println("==============testFunction=========================");
        Student stu = new Student("xbcai",99);
        //下面两条语句是等价的，主要是测试实例方法
        Function<Student,String> f1 = Student::getName;
        Function<Student,String> f2 = (Student t)->t.getName();
        System.out.println(f1.apply(stu));
        System.out.println(f2.apply(stu));
    }

    /**
     * 测试BiConsumer方法
     */
    public static void testBiConsumer(){
        System.out.println("==============testBiConsumer=========================");
        Student stu = new Student("xbcai",99);
        //下面两条语句是等价的，主要是测试实例方法
        BiConsumer<Student,String> c1=Student::setName;
        BiConsumer<Student,String> c2 = (t,name)->t.setName(name);
        c1.accept(stu,"wo ai n");
        System.out.println(stu);
        c2.accept(stu,"我爱你");
        System.out.println(stu);
    }

    /**
     * 测试构造方法
     */
    public static void testBiFunction(){
        System.out.println("==============testBiFunction=========================");
        //下面两条语句是等价的，主要是构造方法
        BiFunction<String,Integer,Student> s1 = (name,score)->new Student(name,score);
        BiFunction<String,Integer,Student> s2 = Student::new;
        Student xbcai1 = s1.apply("xbcai", 99);
        Student xbcai2 = s2.apply("蔡玄彬", 100);
        System.out.println(xbcai1);
        System.out.println(xbcai2);

    }

    /**
     * 测试排序
     */
    public static void testComparator(){
        System.out.println("==============testComparator=========================");
        Student[] stus = new Student[]{new Student("x",88),new Student("y",65),
        new Student("z",99),new Student("w",99)};
        //下面两条语句等价，按分数升序排序
        //Arrays.sort(stus,(s1,s2)->s1.getScore()-s2.getScore());
        //Arrays.sort(stus, Comparator.comparing(Student::getScore));
        //将学生列表按照分数倒序排，分数一样的按照名字进行排序
        Arrays.sort(stus,Comparator.comparing(Student::getScore).reversed().thenComparing(Student::getName));
        System.out.println(Arrays.asList(stus));
    }

    public static void main(String[] args) {
        testStaticMethod();
        testFunction();
        testBiConsumer();
        testBiFunction();
        testComparator();
    }

}
