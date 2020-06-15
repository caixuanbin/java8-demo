package com.xbcai.java8demo.stream;

import com.xbcai.java8demo.stream.model.User;
import com.xbcai.java8demo.stream.utils.StreamUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 中间操作符
 */
public class MiddleDemo {
    /**
     * 过滤集合中不符合条件的元素
     */
    private static void filter(){
        List<String> strings = Arrays.asList("abc", "egb", "hsd", "abe");
        List<String> a = strings.stream().filter(item -> item.contains("a")).collect(Collectors.toList());
        System.out.println(a);
    }

    /**
     * 去除集合中重复的元素
     */
    private static void distinct(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        List<String> collect = strings.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("===============================================");
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"a"));
        userList.add(new User(1,"a"));
        userList.add(new User(2,"b"));
        List<User> collect1 = userList.stream().distinct().collect(Collectors.toList());
        System.out.println(collect1);
    }

    /**
     * 获取流中前N个元素
     */
    private static void limit(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        List<String> collect = strings.stream().limit(2).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 跳过流中前N个元素,即返回除了前N个元素剩下的所有元素
     */
    private static void skip(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        List<String> collect = strings.stream().skip(2).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 接受一个函数作为参数，这个函数会被应用到每个元素上，并将其映射成一个新的元素
     * 对流中所有元素做统一处理
     */
    private static void map(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        List<String> collect = strings.stream().map(item -> item.concat("_xbcai")).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 使用flatMap方法的效果是，各个数组并不是分别映射成一个流，而是映射成流的内容。所有使用map(Arrays::stream)时生成的单个流都被合并起来，即扁平化为一个流
     */
    private static void flatMap(){
        List<String> strings = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Stream<Character> objectStream = strings.stream().flatMap(str -> StreamUtils.getCharacterByString(str));
        List<Character> collect1 = objectStream.collect(Collectors.toList());
        System.out.println(collect1);
    }

    /**
     * map与flatMap区别
     */
    private static void map2flatMap(){
        //解析：flatmap
        //(1) 先把集合中每一个元素拆分成每一个流即[a,b,c],[a,b,c],[e,g,b]...[a,b,e]
        //(2) 然后把各个流进行扁平化操作合并成一个流[a,b,c,a,b,c...a,b,e]
        List<String> flatMap = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Stream<Character> characterStream = flatMap.stream().flatMap(str -> StreamUtils.getCharacterByString(str));
        characterStream.forEach(s-> System.out.println(s));
        //解析：map
        //将流中的元素遍历输出[[a,b,c],[a,b,c],...[a,b,e]],即返回流中流
        List<String> map = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        Stream<Stream<Character>> streamStream = map.stream().map(item -> StreamUtils.getCharacterByString(item));
        streamStream.forEach(s-> System.out.println(s));
    }

    /**
     * 对集合排序
     */
    private static void sorted(){
        //----------------------字母排序------------------------------
        List<String> list = Arrays.asList("abc", "abc","egb", "abc","hsd","hsd", "abe");
        List<String> collect = list.stream().sorted().collect(Collectors.toList());
        System.out.println(collect);
        //--------------------数字排序-------------------------------
        List<Integer> integerList = Arrays.asList(50,10,2,40,-9,8);
        List<Integer> collect1 = integerList.stream().sorted().collect(Collectors.toList());
        System.out.println(collect1);
        //-------------------对象排序-------------------------------
        List<User> userList = new ArrayList<>();
        userList.add(new User(11,"a"));
        userList.add(new User(10,"a"));
        userList.add(new User(21,"b"));
        userList.add(new User(31,"b"));
        List<User> collect2 = userList.stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList());
        System.out.println(collect2);


    }
    public static void main(String[] args) {
        sorted();
    }
}
