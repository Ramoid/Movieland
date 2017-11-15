package com.solovey.movieland.web.util;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.web.util.security.AuthenticationService;
import com.solovey.movieland.web.util.security.SecurityHttpRequestWrapper;
import com.solovey.movieland.web.util.security.entity.PrincipalUser;
import com.solovey.movieland.web.util.security.exceptions.UserNotFoundException;
import com.solovey.movieland.web.util.security.exceptions.UserTokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


public class RequestInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uuid = request.getHeader("uuid");
        String username;
        if (uuid != null) {
            try {
                User user = authenticationService.recognizeUser(uuid);
                username = user.getEmail();
                ((SecurityHttpRequestWrapper) request).setUserPrincipal(new PrincipalUser(user.getEmail(), user.getId()));

            } catch (UserNotFoundException e) {
                username = "hacker/expiredUser";
                log.warn("Cannot find user with uuid {}", uuid);
            } catch (UserTokenExpiredException e) {
                username = "expiredUser";
                log.warn("Expired uuid {}", uuid);
            }
        } else {
            username = "guest";
            log.info("Request with empty uuid", uuid);
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
