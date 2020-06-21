package com.xbcai.java8demo.annotation;

import lombok.AllArgsConstructor;

import java.util.Date;
@AllArgsConstructor
public class Teacher {
    @Label("姓名")
    String name;
    @Label("出生日期")
    @Format(pattern = "yyyy/MM/dd")
    Date born;
    @Label("分数")
    double score;
}
