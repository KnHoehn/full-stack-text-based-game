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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Calls the user repository to retrieve the user from the database given the username
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

        // Calls the getMatchingUsernameMethod to find the user in the database given the username and saves the result into matchingUser
        Optional<User> matchingUser = getMatchingUserName(userName);

        // If no username matches, return false
        if (matchingUser.isEmpty()) {
            return false;
        }

        // Retrieves the optional user into a user object
        User user = matchingUser.get();

        try {

            // Decrypts the password from the database and checks to see if it matches the user-entered password

            byte[] decodedSalt = Base64.getDecoder().decode(user.getSalt());

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-512");
            md.update(decodedSalt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            String saltedPassword = Base64.getEncoder().encodeToString(digest);

            // Returns a boolean result if the password was correct or not
            return saltedPassword.equals(user.getPassword());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to decrypt password", e);
        }

    }

    // Saves a new user to the database
    public void createUser(String userName, String password) {


        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // Checks if the username already exists in the database
        Optional<User> matchingUser = getMatchingUserName(userName);

        // If no username matches, continue
        if (matchingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

            try {

                // Hashes the password before saving to the database

                SecureRandom RANDOM = new SecureRandom();

                byte[] salt = new byte[16];
                RANDOM.nextBytes(salt);

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
                throw new RuntimeException("Unable to encrypt password", e);
            }
    }
}
