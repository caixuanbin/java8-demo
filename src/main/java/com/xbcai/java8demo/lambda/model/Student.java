package com.xbcai.java8demo.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Student {
    private String name;
    private int score;
    private String grade;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Student(String name, int score, String grade) {
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    public static String getCollegeName(){
        return "xbcai";
    }
}
