package com.hwz.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FreemarkDateConvert implements TemplateMethodModel {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Object obj = arguments.get(0);
        if (obj == null) {
            return "";
        }

        long value = System.currentTimeMillis();
        if (obj instanceof Date) {
            value = ((Date) obj).getTime();
        } else if (obj instanceof Timestamp) {
            value = ((Timestamp) obj).getTime();
        }
        return SDF.format(new Date(value));
    }
}
