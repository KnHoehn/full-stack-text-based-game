package com.hoehn.game.Service;

import com.hoehn.game.Entities.User;
import com.hoehn.game.Repository.UserRepository;
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

    public Optional<User> getMatchingUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
