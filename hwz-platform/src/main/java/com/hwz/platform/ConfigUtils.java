package com.hwz.platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 取系统配置属性
 * 
 * @author jianguo.xu 2012-12-14 下午11:28:13
 */
public final class ConfigUtils {

    private static String              DEFAULT_CONFIG_FILE = "/config.properties";

    private static final Logger        log                 = LoggerFactory.getLogger(ConfigUtils.class);

    private static Map<String, String> propertiesMap;
    
    

    static {
        try {
            initProperty();
        } catch (IOException e) {
            log.error("error at init properties", e);
        }
    }

    /**
     * 初始化配置文件(config.properties)
     * 
     * @throws IOException
     */
    private static void initProperty() throws IOException {
        if (propertiesMap != null) {
            return;
        }
        loadDefaultProperty();
    }

    private static void loadDefaultProperty() throws IOException {
        propertiesMap = new HashMap<String, String>();
        InputStream ins = null;
        Properties properties = new Properties();
        try {
            ins = ConfigUtils.class.getResourceAsStream(DEFAULT_CONFIG_FILE);
            if (ins == null) {
                return;
            }
            properties.load(ins);
        } finally {
            IOUtils.closeQuietly(ins);
        }
        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            propertiesMap.put((String) entry.getKey(), ((String) entry.getValue()).trim());
        }
    }
    
    //
    private static final String getWebRoot() {

        URL url = ConfigUtils.class.getResource("/");
        System.out.println(url);
        String path = url.getPath();
        System.out.println(path);
        if (path.endsWith("/WEB-INF/classes/")) {
            int beginIndex = path.length() - "WEB-INF/classes/".length();
            return path.substring(0, beginIndex);
        }
        System.out.println(path);
        
        return path;
    }

    public static String getString(String proKey) {
        return propertiesMap.get(proKey);
    }

    public static String getString(String proKey, String defaultValue) {
        String value = propertiesMap.get(proKey);
        return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
    }

    private static final boolean getBoolean(String key, boolean defaultValue) {
        String str = getString(key, new Boolean(defaultValue).toString());
        if (str.equalsIgnoreCase("true") || str.equals("1") || str.equals("是") || str.equalsIgnoreCase("yes"))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }
    
}
