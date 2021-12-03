package com.lunchteam.lunchrestapi.api.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenuException extends RuntimeException {

    public MenuException(Long menuId) {
        log.warn("Menu not exist: " + menuId);
    }

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
