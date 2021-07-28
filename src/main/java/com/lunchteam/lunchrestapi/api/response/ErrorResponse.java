package com.lunchteam.lunchrestapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {

    private String errorMessage;
    private int errorCode;

    public ErrorResponse(StatusEnum statusEnum) {
        this.errorMessage = statusEnum.msg;
//        this.errorCode = statusEnum.statusCode;
    }

    public ErrorResponse(String msg) {
        this.errorMessage = msg;
    }
}
