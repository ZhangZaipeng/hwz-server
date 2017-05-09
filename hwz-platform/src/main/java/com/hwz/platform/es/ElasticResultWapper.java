package com.hwz.platform.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.JsonWapper;

/**
 * 用于帮助访问 ElasticSearch 返回的查询结果
 * Created by luoyifan on 2016/4/8.
 */
public class ElasticResultWapper {
    @JsonProperty
    public final long took;

    @JsonProperty
    public final long total;

    @JsonProperty
    public final boolean timeOut;

    @JsonProperty("nav")
    public final ElasticResultAggWapper aggs;

    public ElasticResultWapper(RestFulResult restFulResult) {
        JsonWapper jw = restFulResult.getJsonWapper();

        this.took = jw.asLong("took");
        this.timeOut = jw.asBoolean("timed_out");
        this.total = jw.asLong("hits", "total");

        if (jw.contains("aggregations")) {
            aggs = new ElasticResultAggWapper(jw.asDic("aggregations"));
        } else {
            aggs = null;
        }
    }

    public String toString(boolean pretty) {
        if (pretty) {
            return YvanUtil.toJsonPretty(this);
        } else {
            return YvanUtil.toJson(this);
        }
    }

    @Override
    public String toString() {
        return this.toString(true);
    }
}
