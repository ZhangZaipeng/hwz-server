package com.hwz.platform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hwz.platform.platform.JsonWapper;
import com.hwz.platform.springmvc.WebContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class YvanUtil {

    private static ObjectMapper                 objectMapper;
    private static final sun.misc.BASE64Encoder base64Encoder           = new sun.misc.BASE64Encoder();
    private static final sun.misc.BASE64Decoder base64Decoder           = new sun.misc.BASE64Decoder();

    public static final String                  VIEW_HISTORY_COOKIE_KEY = "client_view_history";
    public static final int                     COOKIE_AGE              = 30 * 24 * 60 * 60;
    public static final String                  COOKIE_PATH             = "/";

    static {
        objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();
    }

    public static JsonWapper merge(String mainNode, String updateNode) {
        try {
            return new JsonWapper(objectMapper.writeValueAsString(merge(objectMapper.readTree(mainNode), objectMapper.readTree(updateNode))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode mainJsonNode = mainNode.get(fieldName);
            JsonNode updateJsonNode = updateNode.get(fieldName);

            // if field exists and is an embedded object
            if (mainJsonNode != null && mainJsonNode.isObject()) {
                merge(mainJsonNode, updateNode.get(fieldName));
            } else {
                if (mainJsonNode != null && mainJsonNode.isArray()) {
                    if (updateJsonNode != null) {
                        ArrayNode arrayNode = (ArrayNode) mainJsonNode;
                        if (updateJsonNode.isArray()) {
                            //合并2个数组
                            arrayNode.addAll((ArrayNode) updateJsonNode);
                        } else {
                            arrayNode.add(updateJsonNode);
                        }
                    }
                } else if (mainNode instanceof ObjectNode) {
                    // Overwrite field
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).set(fieldName, value);
                }
            }

        }

        return mainNode;
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

    public static <T> T mapToEntities(Object listMap, Class<T> clazz) {
        try {
            String jsonString = objectMapper.writeValueAsString(listMap);
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json字符串序列化成指定对象
     */
    public static <T> T jsonToObj(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json字符串序列化成 list
     */
    public static List<?> jsonToList(String jsonInput) {
        try {
            return objectMapper.readValue(jsonInput, List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json字符串序列化成json
     */
    public static Map<?, ?> jsonToMap(String jsonInput) {
        try {
            return objectMapper.readValue(jsonInput, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象序列化成json
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象序列化成json
     */
    public static String toJsonPretty(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static <T> T createInstance(Class<T> clazz, String classFullName,
                                       Object... args) throws ClassNotFoundException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor constructor = Class.forName(classFullName).getConstructors()[0];
        return (T) constructor.newInstance(args);
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

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> bean2Map(Object javaBean) {
        Map<K, V> ret = new HashMap<K, V>();
        try {
            Method[] methods = javaBean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(javaBean, (Object[]) null);
                    ret.put((K) field, (V) (null == value ? "" : value));
                }
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
