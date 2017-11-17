package com.solovey.movieland.web.util;


import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.util.dto.ExceptionDto;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import com.solovey.movieland.web.util.security.AuthenticationService;
import com.solovey.movieland.web.util.security.Protected;
import com.solovey.movieland.web.util.security.SecurityHttpRequestWrapper;
import com.solovey.movieland.web.util.security.entity.PrincipalUser;
import com.solovey.movieland.web.util.security.exceptions.UserNotFoundException;
import com.solovey.movieland.web.util.security.exceptions.UserTokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.*;


public class RequestInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JsonJacksonConverter jsonJacksonConverter;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String uuid = request.getHeader("uuid");
        String username = null;
        if (uuid != null) {
            try {
                User user = authenticationService.recognizeUser(uuid);
                username = user.getEmail();

                Optional<List<UserRole>> rolesOprional = getRequiredRoles(handler);

                if (rolesOprional.isPresent() && Collections.disjoint(userService.getUserRoles(user.getId()), rolesOprional.get())) {
                    log.warn("User {} does not have required role ", user.getEmail());
                    writeResponceOnFail(response, HttpServletResponse.SC_FORBIDDEN, "User does not have required role");
                    return false;
                } else {
                    ((SecurityHttpRequestWrapper) request).setUserPrincipal(new PrincipalUser(user.getEmail(), user.getId()));
                }

            } catch (UserNotFoundException e) {
                log.warn("Cannot find user with uuid {}", uuid);
                writeResponceOnFail(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return false;
            } catch (UserTokenExpiredException e) {
                log.warn("Expired uuid {}", uuid);
                writeResponceOnFail(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

                return false;
            }
        } else {
            log.info("Request with empty uuid");
        }

        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("nickname", (username == null) ? "guest" : username);

        return true;
    }


    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.clear();
    }

    private Optional<List<UserRole>> getRequiredRoles(Object handler) {
        Method controllerMethod = (((HandlerMethod) handler).getMethod());
        if (controllerMethod.isAnnotationPresent(Protected.class)) {
            return Optional.of(Arrays.asList(controllerMethod.getAnnotation(Protected.class).roles()));
        }

        return Optional.empty();
    }

    private void writeResponceOnFail(HttpServletResponse response, int responceStatus, String message) {
        response.setStatus(responceStatus);
        try (Writer responceWriter = response.getWriter()) {
            responceWriter.write(jsonJacksonConverter.convertObjectToJson(new ExceptionDto(message)));
            responceWriter.flush();
        } catch (IOException e) {
            log.error("Error write reponce {}", e);
            throw new RuntimeException(e);
        }

    }

}
