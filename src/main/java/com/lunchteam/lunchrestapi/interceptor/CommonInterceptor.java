package com.lunchteam.lunchrestapi.interceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull Object handler
    ) throws Exception {
        log.info("[" + request.getRemoteAddr() + "] [REQ " + request.getMethod() + "] : "
            + request.getRequestURI());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
