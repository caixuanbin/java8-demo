package com.xbcai.java8demo.lambda.test;

import com.xbcai.java8demo.lambda.model.Student;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流的测试用例
 */
public class StreamTest {
    private static Student[] stus = new Student[]{new Student("x",88,"一年级"),new Student("y",65,"一年级"),
            new Student("z",99,"二年级"),new Student("z",102,"二年级"),new Student("a",100,"三年级 ")};

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

    /**
     * 将学生列表转换为map集合，key是学生名字，value是学生实体
     * 这里的key不能重复，如果有重复值会抛出异常
     */
  public static void testToMap(){
      System.out.println("=======================================testToMap================================================");
      Map<String, Student> collect = Arrays.asList(stus).stream().collect(Collectors.toMap(Student::getName, t -> t));
      System.out.println(collect);
  }

    /**
     * 将学生列表转换为map集合，key是学生名字，value是学生实体,如果有重复的key则用新的值赋值给该key对应的value
     * 这里的key可以重复,如果需要合并相同的key对应的值，也可以按照实际情况去处理只需要改造(oldValue,value)->value 这个就行
     * 对于hashMap是没有任何顺序的，如果希望保持元素原来出现的顺序，可以替换为linkedHashMap，如果希望收集的结果排序，可以使用treeMap
     */
  public static void testToMap2(){
      System.out.println("=======================================testToMap2================================================");
      Map<String, Student> collect = Arrays.asList(stus).stream().collect(Collectors.toMap(Student::getName, t -> t,(oldValue,value)->value));
      //下面这个是保持元素原来的出现的顺序
      LinkedHashMap<String, Student> collect1 = Arrays.asList(stus).stream().collect(Collectors.toMap(Student::getName, t -> t, (oldValue, value) -> value, LinkedHashMap::new));
      System.out.println(collect);
      System.out.println(collect1);
  }

    /**
     * 将字符串连接起来用逗号分隔，并且在结果集加前缀和后缀
     */
  public static void testJoining(){
      System.out.println("=======================================testJoining================================================");
      String collect = Stream.of("acf", "laoma", "老是").collect(Collectors.joining(",", "{", "}"));
      System.out.println(collect);
  }

    /**
     * 将学生列表按年级分组，键是年级，值为学生列表
     */
  public static void testGroupingBy(){
      System.out.println("=======================================testGroupingBy================================================");
      Map<String, List<Student>> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade));
      System.out.println(collect);
  }

    /**
     * 将学生按年级分组，统计每个年级的学生个数
     */
  public static void testGroupingBy2(){
      System.out.println("=======================================testGroupingBy2================================================");
      Map<String, Long> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
      System.out.println(collect);
  }

    /**
     * 统计一个单词流中每个单词的个数，按出现的顺序排序
     */
  public static void testGroupingBy3(){
      System.out.println("=======================================testGroupingBy3================================================");
      LinkedHashMap<String, Long> collect = Stream.of("hello", "world", "abc", "hello").collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
      System.out.println(collect);
  }

    /**
     * 获取每一个年级分数最高的一个学生
     * 因为maxBy处理的流可能是空流，所以这个收集器收集的结果是Optional<Student>
     */
  public static void testGroupingBy4(){
      System.out.println("=======================================testGroupingBy4================================================");
      Map<String, Optional<Student>> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade, Collectors.maxBy(Comparator.comparing(Student::getScore))));
      System.out.println(collect);
  }

    /**
     * 获取每一个年级分数最高的一个学生
     * 直接得到学生,前提是指定流不可能为空
     */
    public static void testGroupingBy5(){
        System.out.println("=======================================testGroupingBy5================================================");
        Map<String, Student> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade,Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Student::getScore)),Optional::get)));
        System.out.println(collect);
    }

    /**
     * 按年级统计学生分数信息
     * 这里的输出信息如下：包括个数、最大值、最小值、和、平均值
     * {一年级=DoubleSummaryStatistics{count=2, sum=153.000000, min=65.000000, average=76.500000, max=88.000000},
     * 二年级=DoubleSummaryStatistics{count=2, sum=201.000000, min=99.000000, average=100.500000, max=102.000000},
     * 三年级 =DoubleSummaryStatistics{count=1, sum=100.000000, min=100.000000, average=100.000000, max=100.000000}}
     */
    public static void testGroupingBy6(){
        System.out.println("=======================================testGroupingBy6================================================");
        Map<String, DoubleSummaryStatistics> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade,
                Collectors.summarizingDouble(Student::getScore)));
        System.out.println(collect);
    }

    /**
     * 对学生按年级分组，得到学生名称列表
     * 应用场景：对每一个分组内的元素，我们感兴趣的可能不是元素本身，二十它的某部分信息，这个时候就可以使用mapping
     * 这里的输出为 {一年级=[x, y], 二年级=[z, z], 三年级 =[a]}
     */
    public static void testGroupingByMapping(){
        System.out.println("=======================================testGroupingByMapping================================================");
        Map<String, List<String>> collect = Arrays.asList(stus).stream().collect(Collectors.groupingBy(Student::getGrade, Collectors.mapping(Student::getName, Collectors.toList())));
        System.out.println(collect);
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
        //testToMap();
        testToMap2();
        testJoining();
        testGroupingBy();
        testGroupingBy2();
        testGroupingBy3();
        testGroupingBy4();
        testGroupingBy5();
        testGroupingBy6();
        testGroupingByMapping();
    }
}
