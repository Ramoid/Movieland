package com.solovey.movieland.web.security;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.security.AuthenticationService;
import com.solovey.movieland.web.security.cache.UserTokenCache;
import com.solovey.movieland.web.security.entity.LoginRequest;
import com.solovey.movieland.web.security.entity.UserToken;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @Test
    public void testConvertMoviePrice() {

        String inputJson = "{\"email\":\"Rambo@gmail.com\",\"password\":\"testPass\"}";
        String email = "Rambo@gmail.com";
        String password="testPass";
        String encryptedPassword = "$2a$10$Euy7ToIgeY7tOyGELIu00eFTDsG2HLRD2XJdnA/1jSOWvct6hX9Na";
        String token = "666token666";


        User user = new User();
        user.setNickname("Rambo");
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setId(1);

        UserService mockUserService = mock(UserService.class);
        when(mockUserService.extractUser(email)).thenReturn(Optional.of(user));

        UserTokenCache mockUserTokenCache = mock(UserTokenCache.class);
        when(mockUserTokenCache.getUserToken(user)).thenReturn(token);

        JsonJacksonConverter mockJsonJacksonConverter = mock(JsonJacksonConverter.class);
        when(mockJsonJacksonConverter.parseLoginJson(inputJson)).thenReturn(new LoginRequest(email,password));

        AuthenticationService authenticationService = new AuthenticationService(mockUserTokenCache, mockUserService,mockJsonJacksonConverter);

        UserToken userToken = authenticationService.performLogin(inputJson);

        assertEquals(userToken.getNickname(), "Rambo");
        assertEquals(userToken.getUuid(), token);

    }

}