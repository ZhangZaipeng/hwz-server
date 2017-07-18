package com.hwz.platform.es;

import java.util.*;

import com.google.gson.Gson;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.JsonWapper;

/**
 * ElasticSearch, query builder
 * Created by luoyifan on 2016/3/30.
 */
public class QueryBuilder {
    public final LeafQueryCluase must;
    public final LeafQueryCluase filter;
    public final LeafQueryCluase should;
    public final LeafQueryCluase mustNot;

    public QueryBuilder() {
        must = new LeafQueryCluase(this, "must");
        filter = new LeafQueryCluase(this, "filter");
        should = new LeafQueryCluase(this, "should");
        mustNot = new LeafQueryCluase(this, "must_not");
    }

    public boolean isEmpty() {
        return (must.size() + should.size() + mustNot.size() + filter.size() <= 0);
    }

    public Map<?, ?> build() {
        JsonWapper jw = new JsonWapper();
        if (must.size() + should.size() + mustNot.size() + filter.size() == 0) {
            return new JsonWapper().set("match_all", new LinkedHashMap<>()).getInnerMap();
        }
        if (must.size() == 1 && should.size() + mustNot.size() + filter.size() == 0) {
            return must.innerList.get(0);
        }
        if (should.size() == 1 && must.size() + mustNot.size() + filter.size() == 0) {
            return should.innerList.get(0);
        }

        if (filter.size() > 0) {
            if (filter.size() == 1) {
                jw.set("bool", "filter", filter.innerList.get(0));
            } else {
                jw.set("bool", "filter", filter.innerList);
            }
        }
        if (must.size() > 0) {
            if (must.size() == 1) {
                jw.set("bool", "must", must.innerList.get(0));
            } else {
                jw.set("bool", "must", must.innerList);
            }
        }
        if (should.size() > 0) {
            if (should.size() == 1) {
                jw.set("bool", "should", should.innerList.get(0));
            } else {
                jw.set("bool", "should", should.innerList);
            }
        }
        if (mustNot.size() > 0) {
            if (filter.size() == 1) {
                jw.set("bool", "mustNot", mustNot.innerList.get(0));
            } else {
                jw.set("bool", "mustNot", mustNot.innerList);
            }
        }
        return jw.getInnerMap();
    }

    public static void main(String[] args) {
//        Map<?, ?> query = new QueryBuilder()
//                .must.matchQueryOperator("car_match", "wew2016", "and")
//                .filter.term("item_status", "1")
//                .filter.term("deleted", "0")
//                .build();

//        Map<?, ?> query = new QueryBuilder()
//                .must.matchQueryOperator("car_match", "wew2016", "and")
//                .build();

//        Map<?, ?> query = new QueryBuilder().build();

        /*
        Map<?, ?> query = new QueryBuilder()
                .must.terms("item_id", Arrays.asList("1603211912571028", "1603220413986037"))
                .build();

        JsonWapper jw = new JsonWapper();
        jw.set("from", 0)
                .set("size", 20)
                .set("_source", new String[]{"id", "item_id", "item_name", "item_brand", "item_serie", "item_status", "deleted"})
                .set("query", query);
        */

        String brand = "111";
        String serie = "222";
        String model = "333";

        //" type:0 OR (type:1 AND brand_code:" + brand + ") OR (type:2 AND serie_code:" + serie + ") OR model_code:" + model
        QueryBuilder qb2 = new QueryBuilder();
        qb2.should.term("type", "0");
        qb2.should.bool(new QueryBuilder().must.term("type", "1").must.term("brand_code", brand).build());
        qb2.should.bool(new QueryBuilder().must.term("type", "2").must.term("serie_code", serie).build());
        qb2.should.term("model_code", model);

        QueryBuilder qb = new QueryBuilder();
        qb.filter.bool(qb2.build());

        // System.out.println(YvanUtil.toJsonPretty(qb.build()));
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("test",1);
        System.out.println("1");
        Gson gson = new Gson();
        System.out.println(gson.toJson(map));
        System.out.println(gson.toJson(qb.build()));
//        System.out.println(
//                new StringBuilder()
//                        .append("curl -XGET 'http://10.0.14.225:9200/sdt-test/item/_search?pretty' -d'").append(SystemUtils.LINE_SEPARATOR)
//                        .append(jw.toString(true))
//                        .append("'").append(SystemUtils.LINE_SEPARATOR)
//        );
    }

    public static class LeafQueryCluase {

        private final String cluase;
        private final QueryBuilder root;
        private final List<Map<?, ?>> innerList = new ArrayList<>(3);

        private LeafQueryCluase(QueryBuilder root, String cluase) {
            this.cluase = cluase;
            this.root = root;
        }

        public QueryBuilder bool(Map<?, ?> subQuery) {
            innerList.add(new JsonWapper().set("bool", subQuery).getInnerMap());
            return this.root;
        }

        /**
         * <pre>
         * "term" : {
         *   "message" : "this is a test"
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-term-query.html
         */
        public QueryBuilder term(String field, String value) {
            innerList.add(new JsonWapper().set("term", field, value).getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "terms" : {
         *   "user" : ["kimchy", "elasticsearch"]
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-terms-query.html
         */
        public QueryBuilder terms(String field, Collection<?> value) {
            innerList.add(new JsonWapper().set("terms", field, value).getInnerMap());
            return root;
        }

        /**
         * 设置坐标系和距离查询条件
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/lat-lon.html
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-point.html
         *
         * @param field    字段名
         * @param distance 距离，例如 1km, 200m
         * @param lon      经度 [73°33′E 至 135°05′E]
         * @param lat      纬度 [3°51′N 至 53°33′N]
         */
        public QueryBuilder geoDistance(String field, String distance, Object lon, Object lat) {
            innerList.add(new JsonWapper()
                    .set("geo_distance", "distance", distance)
                    .set("geo_distance", "optimize_bbox", "indexed")
                    .set("geo_distance", field, "lon", lon)
                    .set("geo_distance", field, "lat", lat).getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "terms" : {
         *   "user" : ["kimchy", "elasticsearch"]
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-terms-query.html
         */
        public QueryBuilder terms(String field, String[] value) {
            innerList.add(new JsonWapper().set("terms", field, value).getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "match" : {
         *   "message" : "this is a test"
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-query.html
         */
        public QueryBuilder match(String field, String query) {
            innerList.add(new JsonWapper().set("match", field, query).getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "match": {
         *   "car_type": {
         *     "query": "标准",
         *     "operator": "and"
         *   }
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-query.html
         */
        public QueryBuilder matchQueryOperator(String field, String query, String operator) {
            innerList.add(new JsonWapper()
                            .set("match", field, "query", query)
                            .set("match", field, "operator", operator)
                            .getInnerMap()
            );
            return root;
        }

        /**
         * <pre>
         * "range" : {
         *   "age" : { "gt" : 10, "lt" : 20 }
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-range-query.html
         */
        public QueryBuilder range(String field, String from, boolean fromEqual, String to, boolean toEqual) {
            JsonWapper jw = new JsonWapper();
            if (fromEqual) {
                jw.set("range", field, "gte", from);
            } else {
                jw.set("range", field, "gt", from);
            }

            if (toEqual) {
                jw.set("range", field, "lte", to);
            } else {
                jw.set("range", field, "lt", to);
            }

            innerList.add(jw.getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "range" : {
         *   "age" : { "gte" : 10 }
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-range-query.html
         */
        public QueryBuilder rangeGt(String field, String from, boolean fromEqual) {
            JsonWapper jw = new JsonWapper();
            if (fromEqual) {
                jw.set("range", field, "gte", from);
            } else {
                jw.set("range", field, "gt", from);
            }

            innerList.add(jw.getInnerMap());
            return root;
        }

        /**
         * <pre>
         * "range" : {
         *   "age" : { "lte" : 10 }
         * }
         * </pre>
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-range-query.html
         */
        public QueryBuilder rangeLt(String field, String to, boolean toEqual) {
            JsonWapper jw = new JsonWapper();

            if (toEqual) {
                jw.set("range", field, "lte", to);
            } else {
                jw.set("range", field, "lt", to);
            }

            innerList.add(jw.getInnerMap());
            return root;
        }

        public int size() {
            return innerList.size();
        }
    }


}
