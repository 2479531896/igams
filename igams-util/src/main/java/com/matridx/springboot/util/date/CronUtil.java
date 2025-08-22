package com.matridx.springboot.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} CronUtil
 * {@code @description} Java日期转换成Cron日期表达式工具类
 * {@code @date} 17:00 2022/12/30
 **/
public class CronUtil {
    /***
     * @param dateFormat : yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateByPattern(Date date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null ) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron ,  "00 30 15 12 30 ?" Spring3.0的版本后，cron表达式只能支持6个字段，也就是不支持年份的表达式
     * @param date  : 时间点
     */
    public static String getCron(Date  date){
        // String dateFormat="ss mm HH dd MM ? yyyy";
        String dateFormat="ss mm HH dd MM ?";
        return formatDateByPattern(date, dateFormat);
    }

    /***
     * convert String to cron ,  "00 30 15 12 30 ?" Spring3.0的版本后，cron表达式只能支持6个字段，也就是不支持年份的表达式
     * @param dateStr  : 日期字符串 yyyy-MM-dd HH:mm:ss(若增加其他日期格式，调整configStrDate方法内容即可)
     */
    public static String getCron(String  dateStr){
        // String dateFormat="ss mm HH dd MM ? yyyy";
        String dateFormat="ss mm HH dd MM ?";
        Date date = DateUtils.configStrDate(dateStr);
        return formatDateByPattern(date, dateFormat);
    }
}
