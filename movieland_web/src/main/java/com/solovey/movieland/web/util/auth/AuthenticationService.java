package com.solovey.movieland.web.util.auth;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.util.auth.cache.UserTokenCache;
import com.solovey.movieland.web.util.auth.exceptions.BadLoginRequestException;
import com.solovey.movieland.web.util.auth.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserTokenCache userTokenCache;

    private final UserService userService;

    @Autowired
    public AuthenticationService(UserTokenCache userTokenCache, UserService userService) {
        this.userTokenCache = userTokenCache;
        this.userService = userService;
    }

    public UserToken performLogin(String userJson) throws UserNotFoundException {
        log.info("Starting login user {}", userJson);
        long startTime = System.currentTimeMillis();

        try {
            JsonNode root = objectMapper.readTree(userJson);
            String password = root.at("/password").asText();
            String email = root.at("/email").asText();
            User user = userService.extractUser(password, email);
            if (user == null) {
                throw new UserNotFoundException();
            }
            String token = userTokenCache.getUserToken(user);
            UserToken userToken = new UserToken(token, user.getNickname());
            log.info("User token {} is received . It took {} ms", userToken, System.currentTimeMillis() - startTime);
            return userToken;
        } catch (IOException e) {
            log.error("Error parsing incoming json{} exception {}", userJson, e);
            throw new BadLoginRequestException();
        }

    }

    public void performLogout(String token) {
        log.info("Starting logout token {}", token);
        long startTime = System.currentTimeMillis();
        userTokenCache.removeTokenFromCache(token);
        log.info("Logout done. It took {} ms", System.currentTimeMillis() - startTime);
    }

    public User recognizeUser(String token) {
        log.info("Starting recognize user by token {}", token);
        long startTime = System.currentTimeMillis();
        User user = userTokenCache.findUserByToken(token);
        log.info("User {} is recognized . It took {} ms", user, System.currentTimeMillis() - startTime);
        return user;

    }
}
