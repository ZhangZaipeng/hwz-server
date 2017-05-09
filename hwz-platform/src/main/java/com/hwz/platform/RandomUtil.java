package com.hwz.platform;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    static Random r       = new Random();
    static String ssource = "0123456789";
    static char[] src     = ssource.toCharArray();

    // 产生随机字符串
    private static String randString(int length) {
        char[] buf = new char[length];
        int rnd;
        for (int i = 0; i < length; i++) {
            rnd = Math.abs(r.nextInt()) % src.length;

            buf[i] = src[rnd];
        }
        return new String(buf);
    }

    // 调用该方法，产生随机字符串,
    // 参数i: 为字符串的长度
    public static String runVerifyCode(int i) {
        String VerifyCode = randString(i);
        return VerifyCode;
    }

    /**
     * 随机码生成
     * 
     * @author Mo
     * @param length 随机码长度
     * @return
     */
    public static String random1(int length) {
        /*
         * 这里直接数字代替，没用uuid.length()
         */
        String uuid = UUID.randomUUID().toString().replace("-", "");

        int len = uuid.length();

        /* 定义随机码字符串变量，初始化为"" */
        String random = "";

        /*
         * 循环截取UUID len/length 每次循环截取的字符串长度 len%length 如果出现32长度除不尽的情况，取余数
         */
        int subLen = len / length;
        int remainder = len % length;

        /* 定义substring的两个参数 */
        int start = 0, end = 0;
        for (int i = 0; i < length; i++) {
            /*
             * 计算start和end的值 这里涉及两种方法，一种是除不尽的时候，将截取长度分散到头部，一种是分散到尾部 uuid的前部分是时间戳构成的，因此前部分截取越少，重复率越底 固本方法采用了将多余的部分分散到尾部
             */
            /* 分散到尾部，如length为7的时候4,4,4,4,5,5,5 */
            // end = start + (length-i <= remainder ? 1 : 0)+subLen;
            /* 分散到头部，如length为7 的时候5,5,5,5,4,4,4 */
            end = start + (i < remainder ? 1 : 0) + subLen;
            /* 截取到的字符串 */
            String code = uuid.substring(start, end);
            /* 对所截取的长度进行16位求和 */
            int count = 0;
            for (char c : code.toCharArray()) {
                count += Integer.valueOf(String.valueOf(c), 16);
            }
            /* 将求和结果转化成36位，并增加到随机码中，36位包含了0-9a-z */
            random += Integer.toString(count % 36, 36);
            start = end;
        }
        /* 返回随机码 */
        return random;
    }
}
