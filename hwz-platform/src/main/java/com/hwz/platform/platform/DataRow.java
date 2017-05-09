package com.hwz.platform.platform;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hwz.platform.Conv;

/**
 * 数据行
 * 所有列名都被转换成了小写
 * Created by luoyifan on 2016/3/29.
 */
public class DataRow extends LinkedHashMap<String, Object> {

    public DataRow() {
    }

    public DataRow(Map<? extends String, ?> innerMap) {
        this.putAll(innerMap);
    }

    public long asLong(String colName) {
        return Conv.NL(get(colName));
    }

    public String asStr(String colName) {
        return Conv.NS(get(colName.toLowerCase()));
    }

    public int asInt(String colName) {
        return Conv.NI(get(colName.toLowerCase()));
    }

    public Date asDate(String colName) {
        return Conv.NDT(get(colName.toLowerCase()));
    }

    public boolean asBool(String colName) {
        return Conv.NB(get(colName.toLowerCase()));
    }

    public double asDouble(String colName) {
        return Conv.NDB(get(colName.toLowerCase()));
    }

    public float asFloat(String colName) {
        return Conv.NFloat(get(colName.toLowerCase()));
    }

    public short asShort(String colName) {
        return Conv.NShort(get(colName.toLowerCase()));
    }

    public BigDecimal asBigDec(String colName) {
        return Conv.NDec(get(colName.toLowerCase()));
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key.toString().toLowerCase());
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value.toString().toLowerCase());
    }

    public DataRow asMap(String colName) {
        return new DataRow((Map<String, Object>) this.get(colName));
    }
}
