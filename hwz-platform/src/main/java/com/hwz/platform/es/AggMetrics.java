package com.hwz.platform.es;

/**
 * agg 下的 metrics 操作符
 * max/min/sum/avg 等
 * Created by luoyifan on 2016/3/31.
 */
public class AggMetrics {
    private final String operat;
    private final String field;

    public AggMetrics(String operat, String field) {
        this.operat = operat;
        this.field = field;
    }

    public String getOperat() {
        return operat;
    }

    public String getField() {
        return field;
    }
}
