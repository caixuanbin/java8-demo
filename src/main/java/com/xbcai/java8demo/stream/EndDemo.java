package com.xbcai.java8demo.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 终止操作符
 */
public class EndDemo {
    /**
     * 检查是否至少匹配一个元素，返回boolean
     *
     */
    private static void anyMatch(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        boolean bc = strings.stream().anyMatch(s -> s.contains("bc"));
        System.out.println(bc);
    }
    /**
     * 检查集合中是否都满足条件
     */
    private static void allMatch(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        boolean bc = strings.stream().allMatch(s -> s.contains("bc"));
        System.out.println(bc);
    }

    /**
     * 检查集合中是否都不满足条件
     */
    private static void noneMatch(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        boolean bc = strings.stream().noneMatch(s -> s.contains("bc11"));
        System.out.println(bc);
    }

    /**
     * 返回集合中任意一个元素,要集合并行流才有效果
     */
    private static void findAny(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Optional<String> any = strings.parallelStream().findAny();
        System.out.println(any.orElse("没有"));
    }

    /**
     * 返回集合中第一个元素
     */
    private static void findFirst(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Optional<String> any = strings.stream().findFirst();
        System.out.println(any.orElse("没有"));
    }

    /**
     * 返回集合中第一个元素
     */
    private static void forEach(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        strings.stream().forEach(System.out::println);
    }

    /**
     * 收集器，将流转换为其他形式
     */
    private static void collect(){
        //------------------set----------------------------
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Set<String> collect = strings.stream().collect(Collectors.toSet());
        System.out.println(collect);
        //------------------map----------------------------
        // 自定义設置map的key,和value，当key相同的时候，保存最新的newValue
        Map<String, String> collect1 = strings.stream().collect(Collectors.toMap(k -> k, v -> v, (oldValue, newValue) -> newValue));
        System.out.println(collect1);
    }

    /**
     * 可以将流中元素反复计算，得到一个值
     */
    private static void reduce(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        //这里的result 是不断累加的结果，item是集合里面的元素
        Optional<String> reduce = strings.stream().reduce((result, item) -> result + item);
        System.out.println(reduce.orElse("没有值"));
    }

    /**
     * 获取集合中元素个数
     */
    private static void count(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        long count = strings.stream().count();
        System.out.println(count);
    }


    public static void main(String[] args) {
        reduce();
    }
}
