package com.hwz.platform.es;

import org.apache.commons.lang3.StringUtils;

import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.DataRow;
import com.hwz.platform.platform.JsonWapper;
import com.hwz.platform.platform.Pagination;

import java.util.*;

/**
 * 帮助访问 ElasticSearchResult 的 aggregations 节
 * Created by luoyifan on 2016/4/8.
 */
public class ElasticResultAggWapper {
    private final JsonWapper rootJw;
    private Map<String, List<DataRow>> flatMap;

    public ElasticResultAggWapper(JsonWapper aggs) {
        this.rootJw = aggs;
    }

    public List<DataRow> getFlatMap(String aggFieldName) {
        return getFlatMap().get(aggFieldName);
    }

    /**
     * 按照分页的方法访问 Agg之后的记录
     *
     * @param aggFieldName agg字段名
     * @param pagination   排序属性
     * @return 返回结果记录集
     */
    public List<DataRow> getFlatMap(String aggFieldName, Pagination pagination) {
        List<DataRow> fullRet = getFlatMap().get(aggFieldName);
        pagination.setCount(fullRet.size());
        List<DataRow> ret = new ArrayList<>(pagination.getCount());
        return fullRet.subList(pagination.getBegin(), pagination.getEnd());
    }

    public synchronized Map<String, List<DataRow>> getFlatMap() {
        if (this.flatMap == null) {
            this.flatMap = new LinkedHashMap<>();
            for (String groupKey : rootJw.keys()) {
                //遍历所有 group_by
                if (!groupKey.startsWith("group_by")) {
                    continue;
                }

                List<DataRow> rowList = new ArrayList<>();
                //进入下一级递归
                rowList.addAll(recursionGroup(new DataRow(), groupKey, rootJw.asDic(groupKey)));
                this.flatMap.put(groupKey.split("@")[2], rowList);
            }
        }
        return this.flatMap;
    }

    public static List<DataRow> recursionGroup(DataRow protoDataRow, String groupKey, JsonWapper groupBody) {
        List<DataRow> rows = new ArrayList<>();
        String[] spt = groupKey.split("@");
        String namespace = spt[1];
        String fileName = spt[2];

        // 分析每一个分组对象
        for (Object buckObj : groupBody.asList("buckets")) {
            JsonWapper buck = new JsonWapper((Map) buckObj);

            boolean hasValue = false;
            DataRow dataRow = (DataRow) protoDataRow.clone();
            Set<String> recursionKeys = new LinkedHashSet<>();  //准备需要进一步递归的 group
            // 产生这一个分组项的 key 和 doc_count 值
            dataRow.put("key@" + namespace + "@" + fileName, buck.asStr("key"));
            dataRow.put("doc_count@" + namespace + "@" + fileName, buck.asStr("doc_count"));
            // 分析每一个属性
            for (String bucketKey : buck.keys()) {
                if (bucketKey.startsWith("group_by@")) {
                    recursionKeys.add(bucketKey);
                } else if (bucketKey.startsWith("max@") || bucketKey.startsWith("min@") || bucketKey.startsWith("sum@") || bucketKey.startsWith("avg@") || bucketKey.startsWith("value_count@")) {
                    dataRow.put(bucketKey, buck.get(bucketKey, "value"));
                    hasValue = true;
                } else if (bucketKey.startsWith("stats@")) {
                    //拆解成各项属性 count/min/max/avg/sum
                    String bucketNamespace = bucketKey.split("@")[1];
                    String bucketField = bucketKey.split("@")[2];
                    dataRow.put(StringUtils.join(Arrays.asList("count", bucketNamespace, bucketField), "@"), buck.get(bucketKey, "count"));
                    dataRow.put(StringUtils.join(Arrays.asList("min", bucketNamespace, bucketField), "@"), buck.get(bucketKey, "min"));
                    dataRow.put(StringUtils.join(Arrays.asList("max", bucketNamespace, bucketField), "@"), buck.get(bucketKey, "max"));
                    dataRow.put(StringUtils.join(Arrays.asList("avg", bucketNamespace, bucketField), "@"), buck.get(bucketKey, "avg"));
                    dataRow.put(StringUtils.join(Arrays.asList("sum", bucketNamespace, bucketField), "@"), buck.get(bucketKey, "sum"));
                    hasValue = true;
                }
            }

            // 属性中除了递归，还包含有值
            if (hasValue) {
                rows.add(dataRow);
            }

            //准备递归这个 buck项的子项
            for (String recursionKey : recursionKeys) {
                rows.addAll(recursionGroup(dataRow, recursionKey, buck.asDic(recursionKey)));
            }
        }
        return rows;
    }

    public String toString(boolean pretty) {
        if (pretty) {
            return YvanUtil.toJsonPretty(getFlatMap());
        } else {
            return YvanUtil.toJson(getFlatMap());
        }
    }

    @Override
    public String toString() {
        return this.toString(true);
    }

}
