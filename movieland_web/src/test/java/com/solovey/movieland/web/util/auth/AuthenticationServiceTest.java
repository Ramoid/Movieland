package com.solovey.movieland.web.util.auth;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.util.auth.cache.UserTokenCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @Test
    public void testConvertMoviePrice() {

        String inputJson = "{\"email\":\"Rambo@gmail.com\",\"password\":\"testPass\"}";
        String email = "Rambo@gmail.com";
        String password = "testPass";
        String token = "666token666";

        User user = new User();
        user.setNickname("Rambo");
        user.setEmail(email);
        user.setPassword(password);
        user.setId(1);

        UserService mockUserService = mock(UserService.class);
        when(mockUserService.extractUser(password, email)).thenReturn(user);

        UserTokenCache mockUserTokenCache = mock(UserTokenCache.class);
        when(mockUserTokenCache.getUserToken(user)).thenReturn(token);

        AuthenticationService authenticationService = new AuthenticationService(mockUserTokenCache, mockUserService);

        UserToken userToken = authenticationService.performLogin(inputJson);

        assertEquals(userToken.getNickname(), "Rambo");
        assertEquals(userToken.getUuid(), token);

    }

}