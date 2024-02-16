package org.sayar.net.Security;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse res, org.springframework.security.access.AccessDeniedException e) throws IOException, ServletException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}


