package com.xbcai.java8demo.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * jdk8日期测试例子
 */
public class DateTest {
    /**
     * Instant 表示时刻
     */
    private static void testInstant(){
        System.out.println("================testInstant============================");
        Instant now = Instant.now();
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        System.out.println(now);
        System.out.println(instant);
    }

    /**
     * 将date转换为Instants时刻
     */
    @SuppressWarnings("all")
    private static void testToInstant(){
        System.out.println("====================testToInstant=======================");
        Instant instant = Instant.ofEpochMilli(new Date().getTime());
        System.out.println(instant);
    }

    /**
     * 将Instant转换为date
     */
    private static void testToDate(){
        System.out.println("====================testToDate=======================");
        Date date = new Date(Instant.ofEpochMilli(System.currentTimeMillis()).toEpochMilli());
        System.out.println(date);
    }

    /**
     * LocalDateTime获取日期时分秒
     */
    private static void testLocalDateTime(){
        System.out.println("================testLocalDateTime=========================");
        LocalDateTime ldt = LocalDateTime.now();
        //获取当前日期和时间
        System.out.println(ldt);
        //构建年月日时分秒
        LocalDateTime of = LocalDateTime.of(2017, 7, 11, 20, 45, 5);
        System.out.println(of);

        System.out.println("获取年："+of.getYear());
        System.out.println("获取月："+of.getMonthValue());
        System.out.println("获取日："+of.getDayOfMonth());
        System.out.println("获取小时："+of.getHour());
        System.out.println("获取分钟："+of.getMinute());
        System.out.println("获取秒："+of.getSecond());
        System.out.println("获取礼拜几："+of.getDayOfWeek());
    }

    /**
     * 将localDateTime转为时刻Instant，这里时转换一个LocalDateTime为北京的时刻；
     */
    private static void testLocalDateTimeToInstant(){
        System.out.println("=========================testLocalDateTimeToInstant====================");
        LocalDateTime ldt = LocalDateTime.now();
        Instant instant = ldt.toInstant(ZoneOffset.of("+08:00"));
        System.out.println(instant);
    }

    /**
     * 测试LocalDate、LocalTime构造
     */
    private static void testLocalDateAndLocalTime(){
        System.out.println("==============================testLocalDateAndLocalTime=====================");
        //构造年月日
        LocalDate ld = LocalDate.of(2007,12,11);
        //获取当前时刻系统默认时区解读的日期
        LocalDate now = LocalDate.now();
        //构造时分秒
        LocalTime lt = LocalTime.of(21,10,21);
        //获取当前时刻系统默认时区解读的时间
        LocalTime time = LocalTime.now();
        System.out.println(ld+","+now+","+lt+","+time);
    }

    /**
     * 测试LocalDateTime与LocalDate和LocalTime相互转换
     */
    private static void testLocalDateTimeAndLocalDateAndLocalTime(){
        System.out.println("=============================testLocalDateTimeAndLocalDateAndLocalTime=================================");
        LocalDateTime ldt = LocalDateTime.of(2017, 7, 11, 11, 33, 44);
        //获取年月日
        LocalDate ld = ldt.toLocalDate();
        //获取时分秒
        LocalTime lt = ldt.toLocalTime();
        //将LocalDate拼接上时分秒得到LocalDateTime
        LocalDateTime localDateTime = ld.atTime(21, 21, 21);
        //将LocalTime拼接上年月日得到LocalDateTime
        LocalDateTime localDateTime1 = lt.atDate(LocalDate.of(2008, 8, 8));
        System.out.println(localDateTime+","+localDateTime1);
    }

    /**
     * ZonedDateTime 表示特定时区的日期和时间
     * LocalDateTime.now()也时获取默认时区的当前日期和时间，但它内部不会记录时区信息，只会单纯记录年月日时分秒等信息，而ZonedDateTime除了记录日历信息，还会记录时区
     */
    private static void testZonedDateTime(){
        System.out.println("===========================testZonedDateTime==========================================");
        //获取系统默认时区的当前日期和时间
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }

    /**
     * 测试日期的格式化，它是线程安全的
     *
     */
    private static void testDateTimeFormatter(){
        System.out.println("=============================testDateTimeFormatter========================");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.of(2018, 11, 13, 12, 33, 44);
        //将日期格式化为字符串输出
        String format = formatter.format(ldt);
        System.out.println(format);
        String str = "2018-11-13 12:33:44";
        //将字符串时间格式化为日期类型的时间输出
        LocalDateTime parse = LocalDateTime.parse(str, formatter);
        System.out.println(parse);
    }

    /**
     * 修改时间
     * ChronoField时一个枚举，里面定义了很多表示日历的字段
     */
    private static void testModifyTime(){
        System.out.println("=====================================testModifyTime===========================");
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println("修改之前："+ldt);
        //在指定日期的基础上修改时分秒
        ldt = ldt.withHour(21).withMinute(21).withSecond(21).withNano(0);
        System.out.println("修改之后："+ldt);
        //修改时分
        ldt = ldt.toLocalDate().atTime(15, 20);
        System.out.println("修改之后："+ldt);
        //3小时5分钟后
        ldt = ldt.plusHours(3).plusMinutes(5);
        System.out.println("修改之后："+ldt);
        //今天0点
        ldt = ldt.with(ChronoField.MILLI_OF_DAY,0);
        System.out.println("修改之后："+ldt);
        //修改为下周二上午10点整
        ldt = ldt.plusWeeks(1).with(ChronoField.DAY_OF_WEEK,2).with(ChronoField.MILLI_OF_DAY,0).withHour(10);
        System.out.println("修改之后："+ldt);
        //LocalTime.MIN表示“00:00”
        LocalDateTime ldt2 = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.out.println(ldt2);
        //指定分秒和上面的LocalDateTime.of(LocalDate.now(), LocalTime.MIN)等同
        LocalDateTime localDateTime = LocalDate.now().atTime(0, 0);
        System.out.println(localDateTime);
    }

    /**
     * 获取下周二上午十点整
     * 如果当前时周一，则下周二就是明天，而其他情况则是下周
     */
    private static void testNextWeekTwo(){
        System.out.println("=====================================testNextWeekTwo=================================");
        LocalDate ld = LocalDate.now();
        if(!ld.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            ld = ld.plusWeeks(1);
        }
        LocalDateTime localDateTime = ld.with(ChronoField.DAY_OF_WEEK, 2).atTime(10, 0);
        System.out.println(localDateTime);
        //还有一种更加简洁的办法，效果和上面是一样的 TemporalAdjusters中还有很多便利的办法
        LocalDate now = LocalDate.now();
        LocalDateTime localDateTime1 = now.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).atTime(10, 0);
        System.out.println(localDateTime1);
    }

    /**
     * 明天最后一刻
     */
    private static void testTomorrowLastTime(){
        System.out.println("=========================testTomorrowLastTime======================================");
        LocalDateTime of = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX);
        System.out.println(of);
        //或者这样
        LocalDateTime localDateTime = LocalTime.MAX.atDate(LocalDate.now().plusDays(1));
        System.out.println(localDateTime);
    }

    /**
     * 本月最后一刻
     */
    private static void testCurrentMonthLastTime(){
        System.out.println("==============================testCurrentMonthLastTime=======================================");
        LocalDateTime localDateTime = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        System.out.println(localDateTime);
    }

    /**
     * 下个月第一个周一的下午5点整
     */
    private static void testNextMonthMondayTest(){
        System.out.println("===========================testNextMonthMondayTest================================");
        LocalDateTime localDateTime = LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).atTime(17, 0);
        System.out.println(localDateTime);
    }

    /**
     * 时间段的计算
     * Period 表示日期之间的差，用年月日表示，不能表示时间
     * Duration 表示时间差，用时分秒等表示，也可以用天表示，一天严格等于24小时，不能用年月日表示
     */
    private static void testCalc(){
        System.out.println("=================================testCalc==================================");
        //计算两个日期差
        LocalDate ld1 = LocalDate.of(2016, 3, 24);
        LocalDate ld2 = LocalDate.of(2017, 7, 12);
        Period p = Period.between(ld1, ld2);
        System.out.println(p.getYears()+"年"+p.getMonths()+"月"+p.getDays()+"天");
        //根据生日计算年龄
        LocalDate born= LocalDate.of(1990, 06, 20);
        int years = Period.between(born, LocalDate.now()).getYears();
        System.out.println(years);
        //计算迟到的分钟数，假如9点上班
        long l = Duration.between(LocalTime.of(9, 0), LocalTime.now()).toMinutes();
        System.out.println(l);
    }

    /**
     * 将Date按默认时区转换为LocalDateTime
     */
    private static void testDateToLocalDateTime(){
        System.out.println("============================testDateToLocalDateTime==================================");
        Date date =  new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        System.out.println(localDateTime);
    }

    /**
     * 将Calendar转换为ZonedDateTime
     */
    private static void testCalendarToZonedDateTime(){
        System.out.println("===============================testCalendarToZonedDateTime===============================");
        Calendar cl = Calendar.getInstance();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(cl.getTimeInMillis()), cl.getTimeZone().toZoneId());
        System.out.println(zonedDateTime);
    }



    public static void main(String[] args) {
        testInstant();
        testToInstant();
        testToDate();
        testLocalDateTime();
        testLocalDateTimeToInstant();
        testLocalDateAndLocalTime();
        testLocalDateTimeAndLocalDateAndLocalTime();
        testZonedDateTime();
        testDateTimeFormatter();
        testModifyTime();
        testNextWeekTwo();
        testTomorrowLastTime();
        testCurrentMonthLastTime();
        testNextMonthMondayTest();
        testCalc();
        testDateToLocalDateTime();
        testCalendarToZonedDateTime();
    }
}
