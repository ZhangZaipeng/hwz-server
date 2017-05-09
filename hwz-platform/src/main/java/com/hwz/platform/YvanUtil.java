package com.hwz.platform;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hwz.platform.springmvc.WebContext;

public class YvanUtil {

    private static Gson                 gson;
    private static final sun.misc.BASE64Encoder base64Encoder           = new sun.misc.BASE64Encoder();
    private static final sun.misc.BASE64Decoder base64Decoder           = new sun.misc.BASE64Decoder();

    public static final String                  VIEW_HISTORY_COOKIE_KEY = "client_view_history";
    public static final int                     COOKIE_AGE              = 30 * 24 * 60 * 60;
    public static final String                  COOKIE_PATH             = "/";

    static {
    	gson = new Gson();
    }

    public static String GetRequestBody(HttpServletRequest request, String encoding) {
        int size = request.getContentLength();
        InputStream is = null;
        try {
            is = request.getInputStream();
            byte[] reqBodyBytes = readBytes(is, size);
            return new String(reqBodyBytes, encoding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    /**
     * 获取请求Boy中的内容
     * @param request
     * @return
     */
    public static String GetRequestBody(HttpServletRequest request) {
        int size = request.getContentLength();
        InputStream is = null;
        try {
            is = request.getInputStream();
            byte[] reqBodyBytes = readBytes(is, size);
            return new String(reqBodyBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    

    /**
     * 将Json字符串序列化成指定对象
     */
    public static <T> T jsonToObj(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将Json字符串序列化成 list
     */
    public static List<?> jsonToList(String jsonInput) {
        try {
            return gson.fromJson(jsonInput, List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json字符串序列化成json
     */
    public static Map<String, Object> jsonToMap(String jsonInput) {
        try {
            return gson.fromJson(jsonInput, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 将对象序列化成json
     */
    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 将对象序列化成json
     */
    public static String toJsonPretty(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public static final byte[] readBytes(InputStream is, int contentLen) throws IOException {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            while (readLen != contentLen) {
                readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                if (readLengthThisTime == -1) {// Should not happen.
                    break;
                }
                readLen += readLengthThisTime;
            }
            return message;
        }

        return new byte[] {};
    }

    /**
     * 保存内容到文件
     *
     * @param filePath 文件路径
     * @param content 保存的内容
     */
    public static void saveToFile(String filePath, String content) {
        BufferedWriter bufferedWriter = null;
        OutputStreamWriter outputStreamWriter = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(bufferedWriter);
            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
    
    /**
     * 从文件中读取所有内容
     */
    public static String readFile(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return StringUtils.join(IOUtils.readLines(fis, "UTF-8"), "\r\n");
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }


    public static String encodeBase64(String s) {
        return base64Encoder.encode(s.getBytes());
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String decodeBase64(String s) {
        try {
            byte[] b = base64Decoder.decodeBuffer(s);
            return new String(b, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到当前request请求的所有cookie
     *
     * @return cookie数组
     * @author jianguo.xu
     */
    public static Cookie[] getCookies() {
        HttpServletRequest request = WebContext.currentRequest();
        return request == null ? null : request.getCookies();
    }

    /**
     * 根据cookie名字取得cookie
     *
     * @param name cookie名字
     * @return cookie
     * @author jianguo.xu
     */
    public static Cookie getCookie(String name) {
        Cookie[] cookies = getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String cookName = cookie.getName();
                if (cookName != null && cookName.equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 将cookie写入客户端
     *
     * @param cookie
     * @author jianguo.xu
     */
    public static void writeCookie(Cookie cookie) {
        if (cookie == null) return;
        HttpServletResponse response = WebContext.currentResponse();
        HttpServletRequest request = WebContext.currentRequest();
        /*
         * if (request != null) { String host = request.getHeader("Host"); if (ConfigUtils.WEBSITE.equals(host))
         * cookie.setDomain("." + ConfigUtils.DOMAIN); }
         */
        if (response != null) {
            response.addCookie(cookie);
        }
    }

    public static void removeCookie(String cookieName, String path) {
        HttpServletRequest request = WebContext.currentRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return;

        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(cookieName)) {
                cookie.setMaxAge(0);
                cookie.setPath(path);
                /*
                 * String host = request.getHeader("Host"); if (ConfigUtils.WEBSITE.equals(host)) cookie.setDomain("." +
                 * ConfigUtils.DOMAIN);
                 */
                WebContext.currentResponse().addCookie(cookie);
                break;
            }
        }

    }

    public static String urlEncoding(String value) {
        try {
            byte[] bs = Base64.encodeBase64URLSafe(value.getBytes("UTF-8"));
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode error.", e);
        }
    }


}
