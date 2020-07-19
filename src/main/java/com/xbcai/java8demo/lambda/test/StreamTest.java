package com.xbcai.java8demo.lambda.test;

import com.xbcai.java8demo.lambda.model.Student;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流的测试用例
 */
public class StreamTest {
    private static Student[] stus = new Student[]{new Student("x",88),new Student("y",65),
            new Student("z",99),new Student("w",99),new Student("a",100)};

    /**
     * 返回列表中分数大于90分的学生列表
     */
    public static void testBase(){
        System.out.println("===========================testBase===================================");
        List<Student> collect = Arrays.stream(stus).filter(t -> t.getScore() > 90).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 根据学生列表，返回学生的名字列表
     */
    public static void testStudentListToString(){
        System.out.println("===========================testStudentListToString===================================");
        List<String> collect = Arrays.stream(stus).map(Student::getName).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 根据学生列表，返回大于90分的学生的名字列表
     */
    public static void testStudentListBig90ToString(){
        System.out.println("===========================testStudentListBig90ToString===================================");
        List<String> collect = Arrays.stream(stus).filter(t->t.getScore()>90).map(Student::getName).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 返回字符串列表中长度小于3的字符串，转换为大写，值保留唯一的
     */
    public static void testDistinct(){
        System.out.println("===========================testDistinct===================================");
        List<String> strings = Arrays.asList(new String[]{"abc", "ABc", "edf", "hello", "def"});
        List<String> collect = strings.stream().filter(s -> s.length() <= 3).map(String::toUpperCase).distinct().collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 过滤得到90分以上的学生，然后按照分数从高到低排序，分数一样的按名字排序
     */
    public static void testSorted(){
        System.out.println("===========================testSorted===================================");
        List<Student> collect = Arrays.asList(stus).stream().filter(t -> t.getScore() >= 90).sorted(Comparator.comparing(Student::getScore).reversed().thenComparing(Student::getName)).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 将学生列表按照分数排序，返回第二到第三名
     */
    public static void testSkipAndLimit(){
        System.out.println("===========================testSorted===================================");
        List<Student> collect = Arrays.asList(stus).stream().sorted(Comparator.comparing(Student::getScore).reversed()).skip(1).limit(2).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * peek它返回的流和之前的流时一样的，没有变化，但它提供了一个Consumer,会将流中的每一个元素传给该Consumer。这个方法的主要目的是支持调试，可以使用该方法观察流水线中流转的元素
     */
    public static void testPeek(){
        System.out.println("===========================testPeek===================================");
        List<String> collect = Arrays.asList(stus).stream().filter(t -> t.getScore() > 90).peek(System.out::println).map(Student::getName).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 将学生分数类型转换为double然后求和
     */
    public static void testMapToDouble(){
        System.out.println("====================================testMapToDouble==========================================");
        double sum = Arrays.asList(stus).stream().mapToDouble(Student::getScore).sum();
        System.out.println(sum);
    }

    /**
     * flatMap它接受一个函数mapper，对流中的每一个元素，mapper会将该元素转换为一个流Stream，然后把新生成流的每一个元素传递给下一个操作
     *
     * 这里的mapper将一行字符串按空白符分隔为一个单词流，Arrays.stream 可以将一个数组转换为一个流，输出为[hello, abc, 老马, 编程]
    */
    public static void testFlatMap(){
        System.out.println("========================================testFlatMap=============================================");
        List<String> strings = Arrays.asList(new String[]{"hello abc", "老马 编程"});
        List<String> collect1 = strings.stream().flatMap(line -> Arrays.stream(line.split("\\s+"))).collect(Collectors.toList());
        System.out.println(collect1);
    }

    /**
     * 将学生按分数降序排序获取分数最大的学生返回
     */
    public static void testMax(){
        System.out.println("=======================================testMax================================================");
        Student student = Arrays.asList(stus).stream().max(Comparator.comparing(Student::getScore)).get();
        System.out.println(student);
    }

    /**
     * 判断学生是否全部及格
     */
    public static void testAllMatch(){
        System.out.println("=======================================testAllMatch================================================");
        boolean b = Arrays.asList(stus).stream().allMatch(t -> t.getScore() >= 60);
        System.out.println(b);
    }

    /**
     * 获取学生列表大于70的学生然后随机返回一个学生
     */
    public static void testFindAny(){
        System.out.println("=======================================testFindAny================================================");
        Optional<Student> any = Arrays.asList(stus).stream().filter(t -> t.getScore() >= 70).findAny();
        System.out.println(any.orElse(null));
    }

    /**
     * 输出大于90分的学生列表
     * 在并行流中，forEach不保证处理的顺序，而forEachOrdered会保证按照流中元素的出现顺序进行处理
     */
    public static void testForEach(){
        System.out.println("=======================================testForEach================================================");
        Arrays.asList(stus).stream().filter(t->t.getScore()>90).forEach(System.out::println);
    }

    /**
     * 获取大于90分的学生列表，返回指定类型的数组
     */
    public static void testToArray(){
        System.out.println("=======================================testToArray================================================");
        //下面两行是等价的
        Student[] students1 = Arrays.asList(stus).stream().filter(t -> t.getScore() > 90).toArray((s)-> new Student[s]);
        Student[] students2 = Arrays.asList(stus).stream().filter(t -> t.getScore() > 90).toArray(Student[]::new);
        System.out.println(Arrays.asList(students1));
        System.out.println(Arrays.asList(students2));
    }

    /**
     * 获取分数最高的学生
     */
    public static void testReduce(){
        System.out.println("=======================================testReduce================================================");
        Student student = Arrays.asList(stus).stream().reduce((accu, t) -> {
            if (accu.getScore() >= t.getScore()) {
                return accu;
            } else {
                return t;
            }
        }).get();
        System.out.println(student);
    }

    /**
     * 输出10个随机数
     */
  public static void testGenerate(){
      System.out.println("=======================================testGenerate================================================");
      Stream.generate(()->Math.random()).limit(10).forEach(System.out::println);
  }

    /**
     * 输出100个递增的奇数
     */
  public static void testIterate(){
      System.out.println("=======================================testIterate================================================");
      Stream.iterate(1,t->t+2).limit(100).forEach(System.out::println);
  }


    public static void main(String[] args) {
        testBase();
        testStudentListToString();
        testStudentListBig90ToString();
        testDistinct();
        testSorted();
        testSkipAndLimit();
        testPeek();
        testMapToDouble();
        testFlatMap();
        testMax();
        testAllMatch();
        testFindAny();
        testForEach();
        testToArray();
        testReduce();
        testGenerate();
        testIterate();
    }
}
