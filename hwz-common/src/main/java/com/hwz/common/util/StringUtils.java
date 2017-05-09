package com.hwz.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author jianghui
 * @version 1.0
 * 
 */
public final class StringUtils {

    public static final String EMPTY_STRING = "";

    private StringUtils(){

    }

    private static final String EMPTY = "";

    /**
     * 判断是否为null或空字符串。如果不为null，在判断是否为空字符串之前会调用trim()。
     * 
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals(EMPTY);
    }

    /**
     * 消除html标签。
     * 
     * @param src
     * @return
     */
    public static String escapeHtml(String src) {
        if (src == null) return null;
        return src.replaceAll("<[a-zA-Z/][.[^<]]*>", "");
    }

    /**
     * 消除html特殊标记。如&nbsp;
     * 
     * @param src
     * @return
     */
    public static String escapeBlank(String src) {
        if (src == null) return null;
        return src.replaceAll("&[a-zA-Z]+;", "");
    }

    /**
     * 消除html标签和特殊标记
     * 
     * @param src
     * @return
     */
    public static String escapeHtmlAndBlank(String src) {
        return escapeBlank(escapeHtml(src));
    }

    /**
     * 消除html标签和特殊标记，并把空字符串替换成 &NBSP;
     */
    public static String escapeHtmlAndRelaceNullToNBSP(Object value) {
        if (value == null) {
            return "&nbsp;";
        } else {
            return escapeHtmlAndBlank(value.toString());
        }
    }

    /**
     * 消除html标签和特殊标记，并把空字符串替换成 &NBSP;
     */
    public static String escapeHtmlAndRelaceNullToNBSP(String value) {
        if (isNullOrEmpty(value)) {
            return "&nbsp;";
        } else {
            return escapeHtmlAndBlank(value.toString());
        }
    }

    public static String escapeHtmlAndBlank(Object value) {
        if (value == null) {
            return "";
        } else {
            return escapeHtmlAndBlank(value.toString());
        }
    }

    /**
     * 复制字符串
     * 
     * @param src 源字符串
     * @param times 复制次数
     * @return
     */
    public static String repeat(String src, int times) {
        StringBuffer buffer = new StringBuffer(src);
        for (int i = 0; i < times - 1; i++)
            buffer.append(src);
        return buffer.toString();
    }

    public static String[] printSegmentation(String str, int numberOfCharacters) {
        double number0 = str.length() / numberOfCharacters;
        int quantity;
        if (str.length() % numberOfCharacters == 0) quantity = (int) number0;
        else quantity = (int) (number0 + 1);
        String[] results = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            String str0;
            if (i == quantity - 1) str0 = str;
            else {
                str0 = str.substring(0, numberOfCharacters);
                str = str.substring(numberOfCharacters);
            }
            results[i] = str0;
        }
        return results;
    }

    /**
     * 去掉脚本信息
     * 
     * @param str
     * @return
     */
    public static String removeScript(String str) {
        if (!StringUtils.isNullOrEmpty(str)) {
            Pattern p = Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = p.matcher(str);
            return matcher.replaceAll("");
        }
        return null;
    }

    /**
     * 转义html特殊标签
     * 
     * @param src
     * @return
     */
    public static String escapeSpecialLabel(String src) {
        if (isNullOrEmpty(src)) return null;
        return src.replaceAll("&", "&amp;").replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;").replaceAll("\r\n",
                                                                                                           "<br/>").replaceAll("\n",
                                                                                                                               "<br/>").replaceAll(" ",
                                                                                                                                                   "&nbsp;").replaceAll("\"",
                                                                                                                                                                        "&quot;");
    }

/**
	 * 反转义html特殊标签
	 * @author xujianguo
	 * @param processBracket 是否处理尖括号'<' 和'>'
	 */
    public static String unEscapeSpecialLabel(String src, boolean processBracket) {
        if (isNullOrEmpty(src)) return null;
        src = src.replaceAll("(?i)&amp;", "&").replaceAll("(?i)<br\\s*/?\\s*>", "\r\n").replaceAll("(?i)&nbsp;", " ").replaceAll("(?i)&quot;",
                                                                                                                                 "\"");
        if (processBracket) src = src.replaceAll("(?i)&lt;", "\\<").replaceAll("(?i)&gt;", "\\>");
        return src;
    }

    public static String escapeSpecialChar(String str) {
        str = escapeHtml(escapeBlank(str));
        // String
        // regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_ 《》]";
        String regEx = "[^a-zA-Z0-9\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String removeParameter(String queryString, String parameter) {
        StringBuffer buffer = new StringBuffer();
        if (StringUtils.isNullOrEmpty(queryString) || StringUtils.isNullOrEmpty(parameter)) {
            return queryString;
        }

        String[] parameterPairs = queryString.split("&");
        if (parameterPairs.length <= 0) {
            return "";
        }

        for (int i = 0; i < parameterPairs.length; i++) {
            String[] kvs = parameterPairs[i].split("=");
            if (kvs.length <= 0) {
                continue;
            }
            if (kvs[0].equals(parameter)) continue;
            buffer.append(kvs[0]);
            if (kvs.length == 2) buffer.append("=").append(kvs[1]);
            buffer.append("&");
        }
        if (buffer.length() > 0) buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * 按字节码删除字符串<br/>
     * 如果开始的字节索引是双字节的字符的中间位将被截取<br/>
     * String str = "零壹贰叁肆";System.out.println(deleteByteLength(st,3,6));//零叁肆<br/>
     * 如果结束的字节索引是双字节的字符的中间位将不被截取<br/>
     * String str = "零壹贰叁肆";System.out.println(deleteByteLength(st,2,7));//零叁肆<br/>
     * 
     * @author xujianguo
     * @param inputSrc 被截取的字符串
     * @param startByteIndex 开始的字节索引
     * @param endByteIndex 结束的字节索引
     * @return
     */
    public static String deleteByteLength(String inputSrc, int startByteIndex, int endByteIndex) {
        StringBuilder sb = new StringBuilder(inputSrc);
        int inputByteLength = inputSrc.getBytes().length;
        if (startByteIndex < 0) startByteIndex = 0;
        if (endByteIndex > inputByteLength) endByteIndex = inputByteLength;
        char[] inputChars = inputSrc.toCharArray();
        int startByteLength = 0;
        int startIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            startByteLength += charByteLength;
            if (startByteLength > startByteIndex) break;
            startIndex++;
        }
        int endByteLength = 0;
        int endIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            endByteLength += charByteLength;
            if (endByteLength > endByteIndex) break;
            endIndex++;
        }
        return sb.delete(startIndex, endIndex).toString();
    }

    /**
     * 判断是否是中文
     * 
     * @return
     */
    public static boolean isChinese(char c) {
        return String.valueOf(c).getBytes().length == 2;
    }

    /**
     * 判断字符串中是否包含中文
     * 
     * @return
     */
    public static boolean containsChinese(String str) {
        if (isNullOrEmpty(str)) return false;
        for (int i = 0; i < str.length(); i++) {
            if (isChinese(str.charAt(i))) return true;
        }
        return false;
    }

    /**
     * 根据第一和第二‘#’来获取话题
     * 
     * @param str
     * @return
     */
    public static String[] getTopic(String str) {
        String[] title = new String[0];
        int start = -1;
        int end = -1;
        if (org.apache.commons.lang.StringUtils.isBlank(str) || str.indexOf("#") == -1) {
            return title;
        }
        char[] ArrayStr = str.toCharArray();
        for (int i = 0; i < ArrayStr.length; i++) {
            char strChar = ArrayStr[i];
            if ('#' == strChar) {
                if (start == -1) {
                    start = i;
                } else {
                    end = i;
                }

            }
            if (start != -1 && end != -1 && start != end) {
                String split = str.substring(start, end + 1);
                title = (String[]) ArrayUtils.add(title, split);
                return (String[]) ArrayUtils.addAll(title, getTopic(str.substring(end + 1)));

            }
        }
        return title;
    }

    /**
     * 切去字符串长度
     * 
     * @param value
     * @param length
     * @param suffix 后缀表示
     * @return
     */
    public static String replace(String value, int length, String suffix) {
        String str = "";
        if (isNullOrEmpty(value)) {
            return str;
        }
        if (length > 0 && length < value.length()) {
            str = value.substring(0, length);
            if (!isNullOrEmpty(suffix)) {
                str += suffix;
            }
        } else {
            str = value;
        }

        return str;
    }

    public static boolean isBlank(String value) {
        return isNullOrEmpty(value);
    }

    public static boolean isNotBlank(String value) {
        return !isNullOrEmpty(value);
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    public static String join(Collection<?> array, String separator) {
        if (array == null) {
            return null;
        }
        Object[] arrayObj = new Object[array.size()];
        array.toArray(arrayObj);
        return join(arrayObj, separator, 0, array.size());
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
        // (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Splits the provided text into an array, using whitespace as the separator. Whitespace is defined by
     * {@link Character#isWhitespace(char)}.
     * </p>
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are treated as one separator. For
     * more control over the split use the StrTokenizer class.
     * </p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>.
     * </p>
     * 
     * <pre>
     * StringUtils.split(null)       = null
     * StringUtils.split("")         = []
     * StringUtils.split("abc def")  = ["abc", "def"]
     * StringUtils.split("abc  def") = ["abc", "def"]
     * StringUtils.split(" abc ")    = ["abc"]
     * </pre>
     * 
     * @param str the String to parse, may be null
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator specified. This is an alternative to using StringTokenizer.
     * </p>
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are treated as one separator. For
     * more control over the split use the StrTokenizer class.
     * </p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>.
     * </p>
     * 
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
     * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     * 
     * @param str the String to parse, may be null
     * @param separatorChar the character used as the delimiter
     * @return an array of parsed Strings, <code>null</code> if null String input
     * @since 2.0
     */
    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separators specified. This is an alternative to using StringTokenizer.
     * </p>
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are treated as one separator. For
     * more control over the split use the StrTokenizer class.
     * </p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A <code>null</code> separatorChars splits on
     * whitespace.
     * </p>
     * 
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     * 
     * @param str the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * <p>
     * Splits the provided text into an array with a maximum length, separators specified.
     * </p>
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are treated as one separator.
     * </p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A <code>null</code> separatorChars splits on
     * whitespace.
     * </p>
     * <p>
     * If more than <code>max</code> delimited substrings are found, the last returned string includes all characters
     * after the first <code>max - 1</code> returned strings (including separator characters).
     * </p>
     * 
     * <pre>
     * StringUtils.split(null, *, *)            = null
     * StringUtils.split("", *, *)              = []
     * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     * 
     * @param str the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, <code>null</code> splits on whitespace
     * @param max the maximum number of elements to include in the array. A zero or negative value implies no limit
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    /**
     * Performs the logic for the <code>split</code> and <code>splitPreserveAllTokens</code> methods that return a
     * maximum array length.
     * 
     * @param str the String to parse, may be <code>null</code>
     * @param separatorChars the separate character
     * @param max the maximum number of elements to include in the array. A zero or negative value implies no limit.
     * @param preserveAllTokens if <code>true</code>, adjacent separators are treated as empty token separators; if
     * <code>false</code>, adjacent separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * Performs the logic for the <code>split</code> and <code>splitPreserveAllTokens</code> methods that do not return
     * a maximum array length.
     * 
     * @param str the String to parse, may be <code>null</code>
     * @param separatorChar the separate character
     * @param preserveAllTokens if <code>true</code>, adjacent separators are treated as empty token separators; if
     * <code>false</code>, adjacent separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
