package com.hwz.platform.es;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.DataRow;
import com.hwz.platform.platform.JsonWapper;
import com.hwz.platform.platform.Pagination;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 客户端搜索 ES 的类
 * Created by luoyifan on 2016/3/31.
 * https://www.elastic.co/guide/cn/elasticsearch/guide/current/foreword_id.html
 */
public class SearchAction {
    private final Logger log;
    public String index;
    public String type;
    public long size = 10;
    public long from = 0;
    public Object source = null;

    public final QueryBuilder query = new QueryBuilder();
    public final AggBuilder agg = new AggBuilder();
    public final SortBuilder sort = new SortBuilder();

    private String parentType;
    private QueryBuilder hasParent;

    private String childType;
    private QueryBuilder hasChild;

    public SearchAction(Logger log) {
        this.log = log;
    }

    /**
     * 根据 Child 查询 Parent
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-has-child-query.html
     *
     * @param childType 子索引Type值
     * @param qb        搜索条件
     */
    public SearchAction hasChild(String childType, QueryBuilder qb) {
        this.childType = childType;
        this.hasChild = qb;
        return this;
    }

    /**
     * 根据 Parent 查询 Child
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-has-parent-query.html
     *
     * @param parentType 父索引Type值
     * @param qb         搜索条件
     */
    public SearchAction hasParent(String parentType, QueryBuilder qb) {
        this.parentType = parentType;
        this.hasParent = qb;
        return this;
    }

    public RestFulResult search(JestClient jestClient) throws IOException {
        JsonWapper jw = build();
        RestFulAction sq = new RestFulAction.Builder(jw)
                .setUrl("/" + index + "/" + type + "/_search")
                .setGetMethod()
                .build();
        return jestClient.execute(sq);
    }

    public JsonWapper build() {
        JsonWapper jw = new JsonWapper();
        jw.set("from", from);
        jw.set("size", size);
        if (source != null) {
            jw.set("_source", source);
        }
        if (!query.isEmpty()) {
            jw.set("query", query.build());
        }
        if (!agg.isEmpty()) {
            jw.set("aggs", agg.build());
        }
        if (!sort.isEmpty()) {
            jw.set("sort", sort.build());
        }
        if (hasChild != null) {
            jw.set("has_child", "type", childType);
            jw.set("has_child", "query", hasChild.build());
        }
        if (hasParent != null) {
            jw.set("has_parent", "parent_type", parentType);
            jw.set("has_parent", "query", hasParent.build());
        }
        return jw;
    }

    public static void main(String[] args) throws IOException {
        final Collection<String> servers = Arrays.asList("http://10.0.14.225:9200", "http://10.0.14.226:9200", "http://10.0.14.227:9200", "http://10.0.14.229:9200");
        final int CONN_TIMEOUT = 5 * 1000;
        final int READ_TIMEOUT = 60 * 1000;
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(servers).connTimeout(CONN_TIMEOUT).readTimeout(READ_TIMEOUT).multiThreaded(true).build());
        JestClient jestClient = factory.getObject();


        SearchAction action = new SearchAction(LoggerFactory.getLogger(SearchAction.class));
        action.index = "sdt-test";
        action.type = "item";
        action.from = 0;
        action.size = 0;
        //sa.source = Arrays.asList("id", "item_country", "item_type", "item_id", "item_name", "item_brand", "item_serie", "item_status", "deleted");
//        sa.agg.terms("item_brand").stats("market_price").max("market_price").min("market_price")
//                .terms("item_serie").max("market_price").min("market_price");
//        sa.agg.terms("item_country").minDocCount(10).size(Integer.MAX_VALUE).orderCountDesc();

//        sa.agg.terms("item_brand").valueCount("item_serie").max("market_price").min("market_price")
//                .terms("item_serie").max("market_price").min("market_price")
//                .terms("city_id").max("market_price").min("market_price").max("view_fee").min("view_fee").max("cruise_value").min("cruise_value");
//        sa.agg.terms("item_serie").max("market_price").min("market_price");
//        sa.agg.terms("item_country").stats("market_price").max("market_price").min("market_price")
//                .terms("item_serie").max("market_price").min("market_price");
        action.query.filter.terms("item_type", Arrays.asList("20", "30"));
        action.agg.terms("item_brand")
                .terms("item_serie")
                .terms("city_id").max("market_price").min("market_price").max("view_fee").min("view_fee").max("cruise_value").min("cruise_value");

//        sa.agg.range("market_price", new AggRangeBuilder().to(10000).fromTo(10000, 100000).fromTo(100001, 200000).fromTo(200001, 300000).from(300000).build())
//                .terms("item_brand");
//        sa.agg.terms("item_country").minDocCount(10).size(Integer.MAX_VALUE).orderCountDesc();
//        sa.sort.asc("id");

        System.out.println(action.build().toString(true));
        System.out.println();
        System.out.println();
        System.out.println();

        Pagination pagination = new Pagination(10, 2);

        RestFulResult r = action.search(jestClient);
        ElasticResultWapper w = new ElasticResultWapper(r);
        List<DataRow> rr = w.aggs.getFlatMap("item_brand", pagination);
        System.out.println("record count:" + pagination.getCount());
        System.out.println(YvanUtil.toJsonPretty(rr));
    }
}
