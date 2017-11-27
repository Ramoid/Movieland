package com.solovey.movieland.web.controller;


import com.solovey.movieland.web.security.AuthenticationService;
import com.solovey.movieland.web.security.entity.UserToken;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest {


    @Test
    public void performLoginTest() throws Exception {
        UserToken userToken = new UserToken("666token666", "Rambo");
        String inputJson = "{\"email\":\"Rambo@gmail.com\",\"password\":\"testPass\"}";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.performLogin(inputJson)).thenReturn(userToken);

        UserController controller = new UserController(mockAuthenticationService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/login").
                contentType("application/json;charset=UTF-8").
                content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.uuid", is("666token666")))
                .andExpect(jsonPath("$.nickname", is("Rambo")));
    }


}
