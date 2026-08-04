package com.hoehn.game.controller;

import com.hoehn.game.entities.User;
import com.hoehn.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for retrieving a user from the database
    @GetMapping("/user/{userName}")
    public ResponseEntity<User> getUserName(@PathVariable String userName) {
        Optional<User> user = userService.getMatchingUserName(userName);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint for inserting a new user into the database
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    // Endpoint for logging in a user
    @GetMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestBody User user) {

        boolean authenticated = userService.loginUser(
                user.getUserName(),
                user.getPassword()
        );

        return ResponseEntity.ok(authenticated);
    }
}
