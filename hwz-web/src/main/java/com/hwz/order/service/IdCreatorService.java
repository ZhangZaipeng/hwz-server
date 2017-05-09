package com.hwz.order.service;

public interface IdCreatorService {
	
    long nextValue(String idName, String desc);

    long nextValue(String idName);

    long curValue(String idName, String desc);

    long curValue(String idName);
}
