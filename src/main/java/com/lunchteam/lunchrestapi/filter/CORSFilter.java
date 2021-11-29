package com.lunchteam.lunchrestapi.filter;

import com.lunchteam.lunchrestapi.property.ApplicationYamlReader;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 외부에서 접근가능하도록 CORS 허용
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    private final ApplicationYamlReader properties;

    public CORSFilter(ApplicationYamlReader properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse
            .setHeader("Access-Control-Allow-Origin",
                properties.getIP() + ":" + properties.getPORT());
        httpServletResponse
            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        httpServletResponse
            .setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse
            .setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse
            .setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("CORS Filter init");
    }

    @Override
    public void destroy() {
        log.debug("CORS Filter destroy");
    }
}
