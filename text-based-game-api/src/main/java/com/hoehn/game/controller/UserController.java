package com.hoehn.game.controller;

import com.hoehn.game.dto.CreateUserRequest;
import com.hoehn.game.dto.LoginUserRequest;
import com.hoehn.game.dto.LoginUserResponse;
import com.hoehn.game.dto.RegisterUserResponse;
import com.hoehn.game.dto.UserResponse;
import com.hoehn.game.entities.User;
import com.hoehn.game.service.JwtService;
import com.hoehn.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Endpoint for retrieving a user from the database given the username
    @GetMapping("/user/{userName}")
    public ResponseEntity<UserResponse> getUserName(@PathVariable String userName) {

        Optional<User> user = userService.getMatchingUserName(userName);

        return user.map(foundUser -> ResponseEntity.ok(
                new UserResponse(foundUser.getUserName())
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint for creating a new user account
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> createUser(@RequestBody CreateUserRequest request) {

        try {

            userService.createUser(
                    request.getUserName(),
                    request.getPassword()
            );

            return ResponseEntity.ok(
                    new RegisterUserResponse(true, "Account created successfully.")
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(new RegisterUserResponse(false, e.getMessage()));
        }
    }

    // Endpoint for logging in a user
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request) {

        boolean authenticated = userService.loginUser(
                request.getUserName(),
                request.getPassword()
        );
        if (!authenticated) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(request.getUserName());

        return ResponseEntity.ok(
                new LoginUserResponse(token)
        );

    }

    // Endpoint for retrieving the logged-in user
    @GetMapping("/user/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(new UserResponse(userName));
    }

}
