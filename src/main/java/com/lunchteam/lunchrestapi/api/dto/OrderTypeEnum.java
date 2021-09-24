package com.lunchteam.lunchrestapi.api.dto;

public enum OrderTypeEnum {
    ABC("abc"),
    RECENT("recent"),
    COUNT("count") ;

    private final String value;

    OrderTypeEnum(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
