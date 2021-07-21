package com.lunchteam.lunchrestapi.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Slf4j
@Component
public class CommonInterceptor extends WebContentInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws ServletException {
        String requestURI = request.getRequestURI();
        log.info("Request URI >>> " + requestURI);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler,
        ModelAndView modelAndView
    ) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        response.setHeader("Cache-Control", "no-cache");
    }
}
