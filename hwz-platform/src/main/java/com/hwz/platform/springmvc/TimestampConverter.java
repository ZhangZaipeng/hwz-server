package com.hwz.platform.springmvc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by liuzhuang on 2016/5/31.
 */
public class TimestampConverter implements Converter<String, Timestamp> {

    @Override
    public Timestamp convert(String source) {
        if (StringUtils.isEmpty(source)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            return new Timestamp(dateFormat.parse(source).getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
