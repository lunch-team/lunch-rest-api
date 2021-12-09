package com.lunchteam.lunchrestapi.api.exception;

import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenuException extends RuntimeException {

    public MenuException(Long menuId) {
        log.warn("Menu not exist: " + menuId);
    }

    public MenuException(StatusEnum statusEnum) {
        super(statusEnum.get());
    }

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
