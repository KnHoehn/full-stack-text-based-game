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
import java.security.SecureRandom;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final SecureRandom random;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.random = new SecureRandom();
    }

    // Retrieves a user from the database by username
    public Optional<User> getMatchingUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    // Logs in the user if the username and password matches
    public boolean loginUser(String userName, String password) {

        if (userName == null || userName.isBlank()) {
            return false;
        }

        if (password == null || password.isBlank()) {
            return false;
        }

        // Retrieves the user matching the provided username
        Optional<User> matchingUser = getMatchingUserName(userName);

        // Returns false if no username matches
        if (matchingUser.isEmpty()) {
            return false;
        }

        // Retrieves the user from the Optional
        User user = matchingUser.get();

        try {

            // Hashes the entered password with the stored salt and compares it to the stored password

            byte[] decodedSalt = Base64.getDecoder().decode(user.getSalt());

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-512");
            md.update(decodedSalt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            String saltedPassword = Base64.getEncoder().encodeToString(digest);

            // Returns whether the password matches
            return saltedPassword.equals(user.getPassword());

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to hash password.", e);
        }

    }

    // Saves a new user to the database
    public void createUser(String userName, String password) {


        // Ensures username field is not empty
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        // Ensures password field is not empty
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // Checks if the username already exists in the database
        Optional<User> matchingUser = getMatchingUserName(userName);

        if (matchingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

            try {

                // Hashes the password before saving to the database

                byte[] salt = new byte[16];
                random.nextBytes(salt);

                MessageDigest md;
                md = MessageDigest.getInstance("SHA-512");
                md.update(salt);
                byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

                String saltedPassword = Base64.getEncoder().encodeToString(digest);
                String saltedString = Base64.getEncoder().encodeToString(salt);

                // Creates the User object
                User user = new User();
                user.setUserName(userName);
                user.setPassword(saltedPassword);
                user.setSalt(saltedString);

                // Saves the new user to the database
                userRepository.save(user);

            } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("Unable to hash password.", e);
    }
    }
}
