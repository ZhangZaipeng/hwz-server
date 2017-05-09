package com.hwz.order.dao;
import com.hwz.order.domain.IdCreator;
import com.hwz.platform.mybatis.DefaultMapper;

public interface IdCreatorMapper extends DefaultMapper {
    void nextValue(IdCreator idCreator);

    IdCreator curValue(String idName);
}
