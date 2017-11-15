package com.solovey.movieland.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.solovey.movieland.web.util.auth.entity.ExceptionDto;
import com.solovey.movieland.web.util.auth.entity.UserToken;
import com.solovey.movieland.web.util.auth.AuthenticationService;
import com.solovey.movieland.web.util.auth.exceptions.BadLoginRequestException;
import com.solovey.movieland.web.util.auth.exceptions.InvalidLoginPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Controller

public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/v1/login", method = POST, consumes = "application/json;", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object processLogin(@RequestBody String loginJson, HttpServletResponse response) {
        log.info("Start login user ");
        long startTime = System.currentTimeMillis();
        try {
            UserToken userToken = authenticationService.performLogin(loginJson);
            response.setStatus(200);
            log.info("User logged in. It took {} ms", System.currentTimeMillis() - startTime);
            return userToken;
        }
        catch (BadLoginRequestException e){
            response.setStatus(400);
            return new ExceptionDto(e.getMessage());
        }
        catch (RuntimeException e){
            response.setStatus(401);
            return new ExceptionDto(e.getMessage());
        }


    }

    @RequestMapping(value = "/v1/logout", method = DELETE)
    public ResponseEntity processLogout(@RequestHeader(value = "uuid") String uuid) {
        log.info("Start logout token {}", uuid);
        authenticationService.performLogout(uuid);
        return ResponseEntity.ok("Logout done");
    }

}
