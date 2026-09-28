package com.hoehn.game.service;

import com.hoehn.game.entities.User;
import com.hoehn.game.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService createUserService() {
        return new UserService(userRepository);
    }

    @Test
    void getMatchingUserName() {
        User user = new User();
        user.setUserName("testUser");

        when(userRepository.findByUserName("testUser"))
                .thenReturn(Optional.of(user));

        UserService userService = createUserService();

        Optional<User> result = userService.getMatchingUserName("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUserName());
    }

    @Test
    void getMatchingUserNameUserNotFound() {
        when(userRepository.findByUserName("missingUser"))
                .thenReturn(Optional.empty());

        UserService userService = createUserService();

        Optional<User> result = userService.getMatchingUserName("missingUser");

        assertTrue(result.isEmpty());
    }

    @Test
    void loginUserWithNullUsername() {
        UserService userService = createUserService();

        assertFalse(userService.loginUser(null, "password"));
    }

    @Test
    void loginUserWithBlankUsername() {
        UserService userService = createUserService();

        assertFalse(userService.loginUser("   ", "password"));
    }

    @Test
    void loginUserWithNullPassword() {
        UserService userService = createUserService();

        assertFalse(userService.loginUser("testUser", null));
    }

    @Test
    void loginUserWithBlankPassword() {
        UserService userService = createUserService();

        assertFalse(userService.loginUser("testUser", "   "));
    }

    @Test
    void createUserWithNullUsername() {
        UserService userService = createUserService();

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(null, "password")
        );
    }

    @Test
    void createUserWithBlankUsername() {
        UserService userService = createUserService();

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("   ", "password")
        );
    }

    @Test
    void createUserWithNullPassword() {
        UserService userService = createUserService();

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("testUser", null)
        );
    }

    @Test
    void createUserWithBlankPassword() {
        UserService userService = createUserService();

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("testUser", "   ")
        );
    }

    @Test
    void loginUserWithUserNotFound() {
        when(userRepository.findByUserName("missingUser"))
                .thenReturn(Optional.empty());

        UserService userService = createUserService();

        assertFalse(userService.loginUser("missingUser", "password"));
    }

    @Test
    void loginUserWithCorrectPassword() {
        User user = new User();
        user.setUserName("testUser");

        String salt = "dGVzdFNhbHQ=";
        user.setSalt(salt);

        String password = "password";

        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-512");
            md.update(java.util.Base64.getDecoder().decode(salt));

            byte[] digest = md.digest(
                    password.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            user.setPassword(
                    java.util.Base64.getEncoder().encodeToString(digest)
            );
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        when(userRepository.findByUserName("testUser"))
                .thenReturn(Optional.of(user));

        UserService userService = createUserService();

        assertTrue(userService.loginUser("testUser", password));
    }

    @Test
    void loginUserWithIncorrectPassword() {
        User user = new User();
        user.setUserName("testUser");

        String salt = "dGVzdFNhbHQ=";
        user.setSalt(salt);

        String password = "password";

        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-512");
            md.update(java.util.Base64.getDecoder().decode(salt));

            byte[] digest = md.digest(
                    password.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            user.setPassword(
                    java.util.Base64.getEncoder().encodeToString(digest)
            );
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        when(userRepository.findByUserName("testUser"))
                .thenReturn(Optional.of(user));

        UserService userService = createUserService();

        assertFalse(userService.loginUser("testUser", "wrongPassword"));
    }

    @Test
    void createUserWithExistingUsername() {
        User existingUser = new User();
        existingUser.setUserName("testUser");

        when(userRepository.findByUserName("testUser"))
                .thenReturn(Optional.of(existingUser));

        UserService userService = createUserService();

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("testUser", "password")
        );

        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void createUserSuccessfully() {
        when(userRepository.findByUserName("testUser"))
                .thenReturn(Optional.empty());

        UserService userService = createUserService();

        userService.createUser("testUser", "password");

        verify(userRepository).save(org.mockito.ArgumentMatchers.argThat(user ->
                user.getUserName().equals("testUser")
                        && user.getPassword() != null
                        && !user.getPassword().isBlank()
                        && user.getSalt() != null
                        && !user.getSalt().isBlank()
        ));
    }
}