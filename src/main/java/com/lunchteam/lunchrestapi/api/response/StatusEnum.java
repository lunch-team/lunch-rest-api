package com.lunchteam.lunchrestapi.api.response;

public enum StatusEnum {

    SUCCESS("success"),
    NO_MENU("no_menu"),
    EXISTS_MENU("exist_menu"),
    EXISTS_MENU_TYPE("exists_menu_type"),
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String msg;

    StatusEnum(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    StatusEnum(String msg) {
        this.msg = msg;
    }

    public String get() {
        return this.msg;
    }
}
