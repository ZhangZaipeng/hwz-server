package com.hwz.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hwz.order.dao.IdCreatorMapper;
import com.hwz.order.domain.IdCreator;

@Service
public class IdCreatorServiceImpl implements IdCreatorService{
    @Autowired
    private IdCreatorMapper idCreatMapper;

    @Override
    public long nextValue(String idName, String desc) {
        IdCreator idCreator = new IdCreator();
        idCreator.setIdDesc(desc);
        idCreator.setIdName(idName);

        idCreatMapper.nextValue(idCreator);
        return idCreator.getIdValue();
    }

    @Override
    public long curValue(String idName, String desc) {
        IdCreator creator = idCreatMapper.curValue(idName);
        if (creator == null) {
            return 0;
        }
        return creator.getIdValue();
    }

    @Override
    public long nextValue(String idName) {

        return this.nextValue(idName, null);
    }

    @Override
    public long curValue(String idName) {
        return this.curValue(idName, null);
    }
    
    public static void main(String[] args) {
        System.out.println(String.format("%08d", null));
    }
}
