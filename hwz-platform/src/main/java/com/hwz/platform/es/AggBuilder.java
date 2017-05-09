package com.hwz.platform.es;

import com.hwz.platform.platform.JsonWapper;
import com.hwz.platform.platform.Pagination;
import com.mysql.jdbc.StringUtils;

import java.util.*;

/**
 * 用来构造 ElasticSearch 的 Aggs逻辑
 * Created by luoyifan on 2016/3/31.
 * <p/>
 * count
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-valuecount-aggregation.html
 */
public class AggBuilder {

    private Collection<Agg> aggList = new ArrayList<>();
    private Pagination pagination;

    public Map<?, ?> build() {
        Map<?, ?> ret = new LinkedHashMap<>();
        for (Agg agg : aggList) {
            agg.build(ret);
        }
        return ret;
    }

    public boolean isEmpty() {
        return (aggList.size() <= 0);
    }

    public Pagination getPagination() {
        return pagination;
    }

    public AggBuilder setPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    /**
     * <pre>
     * https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-aggregations-bucket-terms-aggregation.html
     * </pre>
     */
    public Agg terms(String field) {
        Agg agg = new Agg("terms");
        agg.namespace = field;
        agg.attrList.put("field", field);
        agg.attrList.put("size", Integer.MAX_VALUE);
        aggList.add(agg);
        return agg;
    }

    /**
     * 用于构造 ElasticSearch Aggs聚合的range条目
     * 可以使用 RangeBuilder 构造
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
    public Agg range(String field, Collection<?> value) {
        Agg agg = new Agg("range");
        agg.namespace = field;
        agg.attrList.put("field", field);
        agg.attrList.put("keyed", true);
        agg.attrList.put("ranges", value);
        agg.attrList.put("size", Integer.MAX_VALUE);
        aggList.add(agg);
        return agg;
    }

    public static class Agg {

        private final String parentAttName;
        private String namespace;
        private List<AggMetrics> metricsList = new ArrayList<>();
        private Map<String, Object> attrList = new LinkedHashMap<>();
        private List<Map.Entry<String, String>> orderList = new ArrayList<>();
        private Agg subBucket;

        private Agg(String parentAttName) {
            this.parentAttName = parentAttName;
        }

        //min_doc_count
        public Agg minDocCount(int value) {
            attrList.put("min_doc_count", value);
            return this;
        }

        public Agg size(int value) {
            attrList.put("size", value);
            return this;
        }

        /**
         * "include" : ".*sport.*",
         * "include" : ["mazda", "honda"]
         */
        public Agg include(Object value) {
            attrList.put("include", value);
            return this;
        }

        /**
         * "exclude" : "water_.*"
         * "exclude" : ["rover", "jensen"]
         */
        public Agg exclude(Object value) {
            attrList.put("exclude", value);
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-aggregations-bucket-terms-aggregation.html#search-aggregations-bucket-terms-aggregation-order
         */
        public Agg order(String m1, String m2) {
            Map.Entry<String, String> kv = new HashMap.SimpleEntry<String, String>(m1, m2);
            orderList.add(kv);
            return this;
        }

        //"order" : { "_term" : "asc" }
        public Agg orderTermAsc() {
            order("_term", "asc");
            return this;
        }

        //"order" : { "_term" : "desc" }
        public Agg orderTermDesc() {
            order("_term", "desc");
            return this;
        }

        //"order" : { "_count" : "asc" }
        public Agg orderCountAsc() {
            order("_count", "asc");
            return this;
        }

        //"order" : { "_count" : "desc" }
        public Agg orderCountDesc() {
            order("_count", "desc");
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-max-aggregation.html
         */
        public Agg max(String field) {
            metricsList.add(new AggMetrics("max", field));
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-min-aggregation.html
         */
        public Agg min(String field) {
            metricsList.add(new AggMetrics("min", field));
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-sum-aggregation.html
         */
        public Agg sum(String field) {
            metricsList.add(new AggMetrics("sum", field));
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-avg-aggregation.html
         */
        public Agg avg(String field) {
            metricsList.add(new AggMetrics("avg", field));
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-valuecount-aggregation.html
         */
        public Agg valueCount(String field) {
            metricsList.add(new AggMetrics("value_count", field));
            return this;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-stats-aggregation.html
         */
        public Agg stats(String field) {
            metricsList.add(new AggMetrics("stats", field));
            return this;
        }

        public Agg terms(String field) {
            subBucket = new Agg("terms");
            if (StringUtils.isNullOrEmpty(this.namespace)) {
                subBucket.namespace = field;
            } else {
                subBucket.namespace = this.namespace + "-" + field;
            }
            subBucket.attrList.put("field", field);
            subBucket.attrList.put("size", Integer.MAX_VALUE);
            return subBucket;
        }

        public Agg range(String field, Collection<?> value) {
            subBucket = new Agg("range");
            if (StringUtils.isNullOrEmpty(this.namespace)) {
                subBucket.namespace = field;
            } else {
                subBucket.namespace = this.namespace + "-" + field;
            }
            subBucket.attrList.put("field", field);
            subBucket.attrList.put("keyed", true);
            subBucket.attrList.put("ranges", value);
            subBucket.attrList.put("size", Integer.MAX_VALUE);
            return subBucket;
        }

        public void build(Map<?, ?> map) {
            JsonWapper jw = new JsonWapper(map);
            String fieldName = (String) attrList.get("field");
            JsonWapper jwChild = jw.asDic("group_by@" + this.namespace + "@" + fieldName, parentAttName);
            if (attrList.size() > 0) {
                for (Map.Entry<String, Object> attEntry : attrList.entrySet()) {
                    jwChild.set(attEntry.getKey(), attEntry.getValue());
                }

                if (orderList.size() == 1) {
                    jwChild.set("order", orderList.get(0));
                } else if (orderList.size() > 1) {
                    jwChild.set("order", orderList);
                }
            }
            if (metricsList.size() > 0) {
                JsonWapper jwChildAggs = jw.asDic("group_by@" + this.namespace + "@" + fieldName, "aggs");
                for (AggMetrics item : metricsList) {
                    jwChildAggs.set(item.getOperat() + "@" + this.namespace + "@" + item.getField(), item.getOperat(), "field", item.getField());
                }
            }
            if (subBucket != null) {
                subBucket.build(jw.asDic("group_by@" + this.namespace + "@" + fieldName, "aggs").getInnerMap());
            }
        }
    }
}
