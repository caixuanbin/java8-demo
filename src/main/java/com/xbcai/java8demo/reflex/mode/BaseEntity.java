package com.xbcai.java8demo.reflex.mode;

import com.xbcai.java8demo.reflex.anno.NameAnno;

import java.util.Date;

/**
 * 基类
 */
public class BaseEntity {
    @NameAnno("ID")
    private String id;
    @NameAnno("时间")
    private Date createTime;
}
