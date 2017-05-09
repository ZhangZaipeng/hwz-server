package com.hwz.platform.es;

import java.util.*;

/**
 * 用于构造 ElasticSearch Aggs聚合的range条目
 * Created by luoyifan on 2016/3/31.
 * https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-aggregations-bucket-range-aggregation.html
 * <pre>
 * "range" : {
 *   "field" : "price",
 *   "keyed" : true,
 *   "ranges" : [
 *     { "to" : 50 },
 *     { "from" : 50, "to" : 100 },
 *     { "from" : 100 }
 *   ]
 * }
 * </pre>
 */
public class AggRangeBuilder {

    public Set<Map<String, Object>> innerList = new LinkedHashSet<>();

    public AggRangeBuilder to(Object to) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("to", to);
        innerList.add(value);
        return this;
    }

    public AggRangeBuilder fromTo(Object from, Object to) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("from", from);
        value.put("to", to);
        innerList.add(value);
        return this;
    }

    public AggRangeBuilder from(Object from) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("from", from);
        innerList.add(value);
        return this;
    }

    public Collection<?> build() {
        return innerList;
    }
}
