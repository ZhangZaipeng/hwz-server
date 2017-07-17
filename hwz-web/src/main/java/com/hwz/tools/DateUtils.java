package com.hwz.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by zhangzaipeng on 2017/6/29 0029.
 */
public class DateUtils {
    /**
     * 时间格式
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YY_MM = "yyyy年M月";

    /**
     * 注意 dateStr 与 formatStr 一致
     * @param dateStr "2016-11-30 12:33:12"
     * @param formatStr "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date dateStrToDate(String dateStr, String formatStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        Date ret;
        try {
            ret = dateFormat.parse(dateStr);
            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date
     * @param formatStr
     * @return
     */
    public static String dateTodateStr(Date date, String formatStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        String res = dateFormat.format(date);
        return  res;
    }


    /** 获取当前年份 */
    public static int getCurrentYear(){
        Calendar current = Calendar.getInstance();
        return current.get(Calendar.YEAR);
    }

    /** 获取当前月份 */
    public static int getCurrentMonth() {
        Calendar current = Calendar.getInstance();
        return current.get(Calendar.MONTH)+1;
    }

    /** 获取当前日期号 */
    public static int getCurrentDay() {
        Calendar current = Calendar.getInstance();
        return current.get(Calendar.DAY_OF_MONTH);
    }

    /**
     *	检测日期格式是否正确
     * @param time 日期 不包含时间 /和-都可以
     * @return
     */
    public static boolean checkTimeFormat(String time) {
        Pattern p;
        if(time.contains("-"))
        {
            p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\-\\s]?((((0?" +"[13578])|(1[02]))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))" +"|(((0?[469])|(11))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +"(0?2[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12" +"35679])|([13579][01345789]))[\\-\\-\\s]?((((0?[13578])|(1[02]))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\-\\s]?((0?[" +"1-9])|(1[0-9])|(2[0-8]))))))");
        }else {
            p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))");
        }
        return p.matcher(time).matches();
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days=(time2-time1)/(1000*3600*24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(daysBetween(new Date(),new Date(1487241928590L)));
    }

}
