package com.hwz.api.service;

import org.springframework.stereotype.Service;

/**
 * Created by ZhangZaipeng on 2018/3/2 0002.
 */
@Service
public class TestServiceImpl implements TestService{

    @Override
    public String testService(String p1) {
        return "hello --> " + p1;
    }

}
