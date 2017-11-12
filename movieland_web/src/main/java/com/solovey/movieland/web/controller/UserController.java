package com.solovey.movieland.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.solovey.movieland.web.util.auth.UserToken;
import com.solovey.movieland.web.util.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller

public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/v1/login", method = POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public UserToken processLogin(@RequestBody String userJson) {
        log.info("Start login user ");
        long startTime = System.currentTimeMillis();

        UserToken userToken = authenticationService.performLogin(userJson);
        log.info("User logged in. It took {} ms", System.currentTimeMillis() - startTime);
        return userToken;

    }

    @RequestMapping(value = "/v1/logout", method = DELETE)
    public ResponseEntity processLogout(@RequestHeader(value = "uuid") String uuid) {
        authenticationService.performLogout(uuid);
        return ResponseEntity.ok("Logout done");
    }

}
