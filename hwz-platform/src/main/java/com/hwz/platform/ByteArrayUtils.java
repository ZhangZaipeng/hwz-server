package com.hwz.platform;

/**
 * Created by liuzhuang on 2016/5/23.
 */
public class ByteArrayUtils {
    public static int byteIndexOf(byte[] b, String s, int start) {
        return byteIndexOf(b, s.getBytes(), start);
    }

    public static int byteIndexOf(byte[] b, byte[] s, int start) {
        int i;
        if (s.length == 0) {
            return 0;
        }

        int max = b.length - s.length;
        if (max < 0) {
            return -1;
        }

        if (start > max) {
            return -1;
        }

        if (start < 0) {
            start = 0;
        }

        //在b中找到s的第一个元素
        search:
        for (i = start; i <= max; i++) {
            if (b[i] == s[0]) {
                //找到了s中的第一个元素后，比较剩余的部分是否相等
                int k = 1;
                while (k < s.length) {
                    if (b[k + i] != s[k]) {
                        continue search;
                    }
                    k++;
                }
                return i;
            }
        }
        return -1;
    }

    public static byte[] subBytes(byte[] b, int from, int end) {
        byte[] result = new byte[end - from];
        System.arraycopy(b, from, result, 0, end - from);
        return result;
    }
}
