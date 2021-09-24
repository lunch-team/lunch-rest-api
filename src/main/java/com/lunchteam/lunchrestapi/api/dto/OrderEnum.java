package com.lunchteam.lunchrestapi.api.dto;

public enum OrderEnum {
    ASC("asc"),
    DESC("desc");

    private final String value;

    OrderEnum(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
