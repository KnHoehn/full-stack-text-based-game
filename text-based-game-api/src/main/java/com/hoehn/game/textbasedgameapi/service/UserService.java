package com.hoehn.game.textbasedgameapi.service;

import com.hoehn.game.textbasedgameapi.entities.User;
import com.hoehn.game.textbasedgameapi.repository.UserRepository;
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
