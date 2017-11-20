package com.solovey.movieland.web.security;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.security.entity.LoginRequest;
import com.solovey.movieland.web.security.entity.UserToken;
import com.solovey.movieland.web.security.cache.UserTokenCache;
import com.solovey.movieland.web.security.exceptions.InvalidLoginPasswordException;
import com.solovey.movieland.web.security.exceptions.UserNotFoundException;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserTokenCache userTokenCache;

    private final UserService userService;

    private final JsonJacksonConverter jsonJacksonConverter;

    @Autowired
    public AuthenticationService(UserTokenCache userTokenCache, UserService userService, JsonJacksonConverter jsonJacksonConverter) {
        this.userTokenCache = userTokenCache;
        this.userService = userService;
        this.jsonJacksonConverter = jsonJacksonConverter;
    }

    public UserToken performLogin(String loginJson) throws UserNotFoundException {
        log.info("Starting login user ");
        long startTime = System.currentTimeMillis();
        LoginRequest loginRequest = jsonJacksonConverter.parseLoginJson(loginJson);
        Optional<User> userOptional = userService.extractUser(loginRequest.getEmail());

        User user = userOptional.orElseThrow(InvalidLoginPasswordException::new);

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidLoginPasswordException();
        }

        String token = userTokenCache.getUserToken(user);
        UserToken userToken = new UserToken(token, user.getNickname());
        log.info("Successful signing up for user {}. It took {} ms", user.getEmail(), System.currentTimeMillis() - startTime);
        return userToken;

    }

    public void performLogout(String token) {
        log.info("Starting logout token {}", token);
        long startTime = System.currentTimeMillis();
        userTokenCache.removeTokenFromCache(token);
        log.info("User logout done. It took {} ms", System.currentTimeMillis() - startTime);
    }

    public User recognizeUser(String token) {
        log.info("Starting recognize user by token {}", token);
        long startTime = System.currentTimeMillis();
        User user = userTokenCache.findUserByToken(token);
        log.info("User {} is recognized . It took {} ms", user, System.currentTimeMillis() - startTime);
        return user;

    }
}
