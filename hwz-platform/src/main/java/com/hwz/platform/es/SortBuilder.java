package com.hwz.platform.es;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hwz.platform.platform.JsonWapper;

/**
 * 用来构造 ElasticSearch 的 sort 逻辑
 * Created by luoyifan on 2016/3/31.
 * https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-request-sort.html
 */
public class SortBuilder {
    private Map<String, Map<String, String>> innerMap = new LinkedHashMap<>();

    public Map<?, ?> build() {
        JsonWapper jw = new JsonWapper();
        for (Map.Entry<String, Map<String, String>> entry : innerMap.entrySet()) {
            jw.set(entry.getKey(), entry.getValue());
        }
        return jw.getInnerMap();
    }

    public boolean isEmpty() {
        return (innerMap.size() <= 0);
    }

    public SortBuilder asc(String field) {
        Map<String, String> v = new LinkedHashMap<>();
        v.put("order", "asc");
        v.put("missing", "_last");
        innerMap.put(field, v);
        return this;
    }

    public SortBuilder desc(String field) {
        Map<String, String> v = new LinkedHashMap<>();
        v.put("order", "desc");
        v.put("missing", "_last");
        innerMap.put(field, v);
        return this;
    }
}
