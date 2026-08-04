package com.hoehn.game.service;

import com.hoehn.game.entities.User;
import com.hoehn.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieves user from the database given the username
    public Optional<User> getMatchingUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    // Saves a new user to the database
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
