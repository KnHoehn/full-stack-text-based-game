package com.hoehn.game.controller;

import com.hoehn.game.entities.User;
import com.hoehn.game.service.JwtService;
import com.hoehn.game.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getUserNameFound() throws Exception {
        User user = new User();
        user.setUserName("testUser");

        when(userService.getMatchingUserName("testUser"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testUser"));
    }

    @Test
    void getUserNameNotFound() throws Exception {
        when(userService.getMatchingUserName("unknownUser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/user/unknownUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserSuccess() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "userName": "testUser",
                        "password": "password123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Account created successfully."));

        verify(userService).createUser("testUser", "password123");
    }

    @Test
    void createUserFailure() throws Exception {
        doThrow(new IllegalArgumentException("Username already exists."))
                .when(userService)
                .createUser("testUser", "password123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "userName": "testUser",
                        "password": "password123"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Username already exists."));

        verify(userService).createUser("testUser", "password123");
    }

    @Test
    void loginUserSuccess() throws Exception {
        when(userService.loginUser("testUser", "password123"))
                .thenReturn(true);

        when(jwtService.generateToken("testUser"))
                .thenReturn("test-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "userName": "testUser",
                        "password": "password123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));

        verify(userService).loginUser("testUser", "password123");
        verify(jwtService).generateToken("testUser");
    }

    @Test
    void loginUserFailure() throws Exception {
        when(userService.loginUser("testUser", "wrongPassword"))
                .thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "userName": "testUser",
                        "password": "wrongPassword"
                    }
                    """))
                .andExpect(status().isUnauthorized());

        verify(userService).loginUser("testUser", "wrongPassword");
        verify(jwtService, never()).generateToken("testUser");
    }

    @Test
    void getCurrentUser() throws Exception {
        Authentication authentication = org.mockito.Mockito.mock(
                Authentication.class
        );

        when(authentication.getName())
                .thenReturn("testUser");

        mockMvc.perform(get("/user/me")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testUser"));
    }
}