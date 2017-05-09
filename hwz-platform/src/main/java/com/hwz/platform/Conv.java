package com.hwz.platform;


import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类型转换工具类
 * Created by luoyf on 2016/3/29.
 */
public class Conv {
    public static int NI(Object o) {
        return NI(o, 0);
    }

    public static int NI(Object o, int df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof BigInteger) {
            return ((BigInteger) o).intValue();
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).intValue();
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static long NL(Object o) {
        return NL(o, 0L);
    }

    public static long NL(Object o, long df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Long) {
            return (Long) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof BigInteger) {
            return ((BigInteger) o).longValue();
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).longValue();
        }
        try {
            return Long.parseLong(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static String NS(Object o) {
        return NS(o, "");
    }

    public static String NS(Object o, String df) {
        try {
            return o.toString();
        } catch (Exception e) {
            return df;
        }
    }

    public static short NShort(Object o) {
        return NShort(o, (short) 0);
    }

    public static short NShort(Object o, short df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof BigInteger) {
            return ((BigInteger) o).shortValue();
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).shortValue();
        }
        try {
            return Short.parseShort(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static boolean NB(Object o) {
        return NB(o, false);
    }

    public static boolean NB(Object o, boolean df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if (o instanceof Short || o instanceof Long || o instanceof Byte || o instanceof Integer) {
            return (NI(o, 0) != 0);
        }
        if (o instanceof String) {
            String v = NS(o);
            return (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes"));
        }
        try {
            return Boolean.parseBoolean(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static float NFloat(Object o) {
        return NFloat(o, 0F);
    }

    public static float NFloat(Object o, float df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Float) {
            return (Float) o;
        }
        if (o instanceof Short || o instanceof Long || o instanceof Byte || o instanceof Integer) {
            return NI(o, 0);
        }
        try {
            return Float.parseFloat(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static double NDB(Object o) {
        return NDB(o, 0D);
    }

    public static double NDB(Object o, double df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof Float) {
            return (Float) o;
        }
        if (o instanceof Short || o instanceof Long || o instanceof Byte || o instanceof Integer) {
            return NI(o, 0);
        }
        try {
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    public static BigDecimal NDec(Object o) {
        return NDec(o, BigDecimal.ZERO);
    }

    public static BigDecimal NDec(Object o, BigDecimal df) {
        if (o == null) {
            return df;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        if (o instanceof Long || o instanceof Integer || o instanceof Byte || o instanceof Short) {
            return BigDecimal.valueOf(NL(o));
        }
        if (o instanceof Float || o instanceof Double) {
            return BigDecimal.valueOf(NDB(o));
        }
        try {
            return BigDecimal.valueOf(NDB(o));
        } catch (Exception e) {
            return df;
        }
    }

    public static Date NDT(Object o) {
        return NDT(o, new Date());
    }

    public static Date NDT(Object o, Date df) {
        if (o == null) {
            return df;
        }
        if (o instanceof Date) {
            return (Date) o;
        }
        if (o instanceof Long) {
            return new Date((Long) o);
        }
        if (o instanceof Integer) {
            String v = o.toString();
            if (v.length() == 8) {
                return new Date(
                        Integer.parseInt(v.substring(0, 4)),
                        Integer.parseInt(v.substring(5, 6)),
                        Integer.parseInt(v.substring(7, 8)));
            }
        }
        try {
            return DateUtils.parseDate(o.toString());
        } catch (Exception e) {
            return df;
        }
    }

    private static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    public static String DateTimeStr(Date date) {
        return FORMAT_DATE_TIME.format(date);
    }

    public static String TimeStr(Date date) {
        return FORMAT_TIME.format(date);
    }

    public static String DateStr(Date date) {
        return FORMAT_DATE.format(date);
    }

    public static void main(String[] args) {
        Object o1 = System.currentTimeMillis();
        Object o2 = "20160223";
        Object o3 = "2016/02/23T08:09:01";
        Object o4 = "2016-02-23 08:09:01";
        System.out.println(DateTimeStr(NDT(o1)));
        System.out.println(DateTimeStr(NDT(o2)));
        System.out.println(DateTimeStr(NDT(o3)));
        System.out.println(DateTimeStr(NDT(o4)));
    }
}
