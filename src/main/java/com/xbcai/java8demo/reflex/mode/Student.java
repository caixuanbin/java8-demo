package com.xbcai.java8demo.reflex.mode;

import com.xbcai.java8demo.reflex.anno.NameAnno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NameAnno("学生实体")
public class Student{
    @NameAnno("名字")
    private String name;
    @NameAnno("年龄")
    private int age;
    @NameAnno("分数")
    private Double score;
}