package com.lunchteam.lunchrestapi.api.dto;

public enum DtoEnum {
    ASC("asc"),
    DESC("desc");

    private final String value;

    DtoEnum(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
