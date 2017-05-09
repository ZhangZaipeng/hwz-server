package com.hwz.platform;

import java.util.Map;

/**
 * 用来向客户端抛出 Json 对象格式的异常
 * Created by luoyifan on 2016/3/30.
 */
public class JsonMessageException extends RuntimeException {

    private final Map<?, ?> innerMessage;

    public JsonMessageException(String message) {
        this.innerMessage = YvanUtil.jsonToMap(message);
    }

    public JsonMessageException(String message, Throwable cause) {
        super("", cause);
        this.innerMessage = YvanUtil.jsonToMap(message);
    }

    public Map<?, ?> getInnerMessage() {
        return innerMessage;
    }
}
