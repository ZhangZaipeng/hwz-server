package com.hwz.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hwz.platform.Conv;

public class DateTool {

    private static final SimpleDateFormat STANDARD_DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_DAY          = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat TIME_HOUR         = new SimpleDateFormat("hh");
    private static final SimpleDateFormat WX_TIME           = new SimpleDateFormat("yyyyMMddHHmmSS");
    private static final SimpleDateFormat DFT               = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private static final SimpleDateFormat DFTS              = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:SS");
    private static final SimpleDateFormat DFT1              = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Date getStringToDate(String s) throws ParseException {
        return STANDARD_DATE_FMT.parse(s);
    }

    public static Date getStringToDateOne(String s) throws ParseException {
        return DFT.parse(s);
    }

    public static String getStringTotime(long s) throws ParseException {
        return DFTS.format(new Date(s));
    }

    public static int getTimeDay(long tick) {
        return Integer.valueOf(TIME_DAY.format(new Date(tick)));
    }

    public static int getTimeHour(long tick) {
        return Integer.valueOf(TIME_HOUR.format(new Date(tick)));
    }
//123
    public static String getDateToString(Date s) throws ParseException {
        return STANDARD_DATE_FMT.format(s);
    }

    public static String getDateToStringOne(Date s) throws ParseException {
        return DFT.format(s);
    }

    public static long getWxTime(long tick) {
        return Conv.NL(WX_TIME.format(new Date(tick)));
    }

    public static long fromWxTime(String wxTime) throws ParseException {
        return Conv.NL(WX_TIME.parse(wxTime).getTime());
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date1 = null;
        try {
            date = format.parse("2016-1-1 00:00:00");
            System.out.println(date.getTime());
            date1 = format.parse("2016-1-1 01:00:00");
            System.out.println(date1.getTime());
            System.out.println(date1.getTime() - date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Date getStringToDateOne1(String s) throws ParseException {
        return DFT1.parse(s);
    }
}
