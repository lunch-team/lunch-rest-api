package com.lunchteam.lunchrestapi.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles an access denied failure.
     *
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException in the event of an IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        // 권한 없이 접근할 때 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
