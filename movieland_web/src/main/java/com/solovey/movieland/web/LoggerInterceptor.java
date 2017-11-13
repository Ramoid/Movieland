package com.solovey.movieland.web;


import com.solovey.movieland.web.util.auth.AuthenticationService;
import com.solovey.movieland.web.util.auth.exceptions.UserNotFoundException;
import com.solovey.movieland.web.util.auth.exceptions.UserTokenExpiredException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


public class LoggerInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uuid = request.getHeader("uuid");
        String username;
        if (uuid != null) {
            try {
                username = authenticationService.recognizeUser(uuid).getEmail();
            } catch (UserNotFoundException e) {
                username = "hacker/expiredUser";
            } catch (UserTokenExpiredException e) {
                username = "expiredUser";
            }
        } else {
            username = "guest";
        }

        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("nickname", username);

        return true;
    }


    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.clear();
    }

}
