package com.hoehn.game.service;

import com.hoehn.game.entities.User;
import com.hoehn.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

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

        try {

            // Encrypts the password before saving to the database

            SecureRandom RANDOM = new SecureRandom();

            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] digest = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));

            String saltedPassword = Base64.getEncoder().encodeToString(digest);
            String saltedString = Base64.getEncoder().encodeToString(salt);

            user.setPassword(saltedPassword);
            user.setSalt(saltedString);

            return userRepository.save(user);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to encrypt password", e);
        }

    }
}
