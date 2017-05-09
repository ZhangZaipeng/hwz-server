package com.hwz.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 用来辅助生成Form或者QueryString的类
 * Created by luoyf on 2016/5/10.
 */
public class Form extends javax.ws.rs.core.Form {

    private static final Logger LOG = LoggerFactory.getLogger(Form.class);

    public Form() {

    }

    public Form(String queryString) throws UnsupportedEncodingException {
        String[] sp = queryString.split("&");
        if (sp.length <= 0) {
            return;
        }
        for (String v : sp) {
            String[] sp2 = v.split("=");
            if (sp2.length > 1) {
                this.param(URLDecoder.decode(sp2[0], "UTF-8"), URLDecoder.decode(sp2[1], "UTF-8"));
            } else {
                this.param(URLDecoder.decode(v, "UTF-8"), "");
            }
        }
    }

    public String asString(String key) {
        return asMap().getFirst(key);
    }

    public boolean containsParam(String key) {
        return asMap().containsKey(key);
    }

    public String buildFormBody() {
        StringBuilder sb = new StringBuilder();
        String sp = "";
        for (Map.Entry<String, List<String>> entry : asMap().entrySet()) {
            for (String v : entry.getValue()) {
                try {
                    sb.append(sp).append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(v, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error("BuildForm error!", e);
                }
                sp = "&";
            }
        }
        return sb.toString();
    }

    public String buildJson(boolean pretty) {
        if (pretty) {
            return YvanUtil.toJson(asMap());
        } else {
            return YvanUtil.toJson(asMap());
        }
    }

}
