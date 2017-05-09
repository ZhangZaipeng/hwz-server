package com.hwz.order.domain;

public class IdCreator {
    private String            idName;
    private String            idDesc;
    private long              idValue;

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdDesc() {
        return idDesc;
    }

    public void setIdDesc(String idDesc) {
        this.idDesc = idDesc;
    }

    public long getIdValue() {
        return idValue;
    }

    public void setIdValue(long idValue) {
        this.idValue = idValue;
    }
}
